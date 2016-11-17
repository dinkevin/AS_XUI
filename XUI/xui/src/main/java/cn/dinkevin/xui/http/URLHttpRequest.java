package cn.dinkevin.xui.http;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Iterator;

import cn.dinkevin.xui.util.LogUtil;
import cn.dinkevin.xui.util.FileUtil;
import cn.dinkevin.xui.util.JSONUtil;
import cn.dinkevin.xui.util.StringUtil;

/**
 * 基于安卓原生的 URLConnection 封装的 HTTP 请求类 </br>
 * Created by ChengPengFei on 2016/11/10 0010.</br>
 */

public class URLHttpRequest extends BasicHttpRequest {

    protected URL httpUrl;
    protected HttpURLConnection httpConnection;

    // 网络输入流与输出流
    protected OutputStream outputStream;
    protected InputStream inputStream;

    // 接收区的缓存长度
    protected static final int RECEIVE_BUFFER_LENGTH = 1024 * 1024 ;
    protected ByteBuffer receiveBuffer;
    protected int receiveBufferPoint;

    protected byte[] requestParamsBuffer;

    public URLHttpRequest(String url) {
        super(url);
    }

    public URLHttpRequest(String url, HttpRequestMethod method){
        super(url,method);
    }

    /**
     * 创建一个 Post 方法的 HTTP 请求
     * @param url
     * @return
     */
    public static URLHttpRequest createPostRequest(String url){
        return new URLHttpRequest(url, HttpRequestMethod.POST);
    }

    @Override
    public void start() {

        LogUtil.d("------ http request start ------");
        LogUtil.d("url : ",this.url);
        // 网络地址检查
        try{
            this.httpUrl = new URL(StringUtil.encodeUrl(this.url));
        }catch (Exception e){
            LogUtil.e(this.url,"address format", Log.getStackTraceString(e));
            this.httpException = new HttpException("address format error",e);
            return;
        }

        // 尝试打开网络连接
        try{
            this.httpConnection = (HttpURLConnection)this.httpUrl.openConnection();

            switch (this.httpMethod){
                case GET:
                    this.httpConnection.setRequestMethod("GET");
                    this.httpConnection.setDoOutput(false);
                    LogUtil.d("method : GET");
                    break;

                case POST:
                    this.httpConnection.setRequestMethod("POST");
                    this.httpConnection.setDoOutput(true);
                    LogUtil.d("method : POST");
                    break;
            }
            this.httpConnection.setDoInput(true);
            this.httpConnection.setUseCaches(false);
            this.httpConnection.setRequestProperty("Charset", "UTF-8");
            LogUtil.d("Charset : UTF-8");
            this.httpConnection.setConnectTimeout(1000 * 10);   // 设置超时为10秒

            // 添加头数据
            Iterator<String> it = this.requestHeader.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                String value = this.requestHeader.get(key);
                this.httpConnection.setRequestProperty(key,value);
                LogUtil.d(key,":",value);
            }

            this.httpConnection.connect();
        }catch (IOException e){
            LogUtil.e(this.url,"connect", Log.getStackTraceString(e));
            this.httpException = new HttpException("open connection failed",e);
            return;
        }

        // 判断当前请求方式为 POST
        if(this.httpMethod == HttpRequestMethod.POST){

            // 判断提交的数据 content-type 为 json
            String contentType = this.requestHeader.get(HttpHeader.HEAD_KEY_CONTENT_TYPE);
            if(this.requestParams.size() > 0)
            {
                if(HttpHeader.HEAD_VALUE_ACCEPT_APPLICATION_JSON.equals(contentType)){
                    String bodyBuffer = JSONUtil.gson.toJson(this.requestParams);
                    if(!TextUtils.isEmpty(bodyBuffer)){
                        this.requestParamsBuffer = bodyBuffer.getBytes(Charset.forName("utf-8"));
                    }
                    LogUtil.d("body :",bodyBuffer);
                }
                // 其它的类型默认为字符拼接方式
                else{
                    String[] keys = new String[this.requestParams.size()];
                    this.requestParams.keySet().toArray(keys);
                    StringBuffer bodyBuffer = new StringBuffer(keys[0]);
                    bodyBuffer.append("=");
                    bodyBuffer.append(this.requestParams.get(keys[0]));
                    for(int i = 1;i < keys.length;i++){
                        bodyBuffer.append("&");
                        bodyBuffer.append(keys[i]);
                        bodyBuffer.append("=");
                        bodyBuffer.append(this.requestParams.get(keys[i]));
                    }
                    this.requestParamsBuffer = bodyBuffer.toString().getBytes(Charset.forName("utf-8"));

                    LogUtil.d("body :",bodyBuffer);
                }
            }

            // 打开网络输出流
            try{
                this.outputStream = this.httpConnection.getOutputStream();
                if(null != this.requestParamsBuffer)
                    this.outputStream.write(this.requestParamsBuffer);
                if(null != this.requestBody){
                    this.outputStream.write(this.requestBody);
                    LogUtil.df("body",new String(this.requestBody));
                }
                this.outputStream.flush();
            }catch (IOException e){
                LogUtil.d("get output stream", Log.getStackTraceString(e));
                this.httpException = new HttpException("get output stream error",e);
                this.httpConnection.disconnect();
                return;
            }finally {
                FileUtil.closeStream(this.outputStream);
            }
        }

        // 读取 HTTP 请求状态信息
        try{
            int status = this.httpConnection.getResponseCode();
            if(status >= 400){
                this.inputStream = this.httpConnection.getErrorStream();
                String message = FileUtil.printToBuffer(this.inputStream).toString();
                LogUtil.e("http response -> status:",status,"message:",message);
                this.httpException = new HttpException(status,message);
                return;
            }
        }catch (IOException e){
            LogUtil.e("response code", Log.getStackTraceString(e));
            return;
        }finally {
            FileUtil.closeStream(this.inputStream);
        }

        // 打开网络输入流
        try{
            this.inputStream = this.httpConnection.getInputStream();

            String responseMessage = this.httpConnection.getResponseMessage();
            int responseCode = this.httpConnection.getResponseCode();
            LogUtil.d("---- response ---->");
            LogUtil.d("response code:",responseCode," message:",responseMessage);

            if(responseCode != 200){
                this.httpException = new HttpException(responseCode,responseMessage);
                return;
            }

            // 申请接收区缓存
            if(null == this.receiveBuffer){
                this.receiveBuffer = ByteBuffer.allocate(RECEIVE_BUFFER_LENGTH);
            }
            this.receiveBuffer.clear();
            this.receiveBuffer.rewind();
            this.receiveBufferPoint = 0;

            // 读取数据
            byte[] readBuffer = new byte[1024];
            int readCount;
            while ((readCount = this.inputStream.read(readBuffer)) != -1){
                this.receiveBuffer.put(readBuffer,0,readCount);
                receiveBufferPoint += readCount;
            }

            // 将接收缓存区的数据读取到 responseBody 中
            this.receiveBuffer.flip();
            this.responseBody = new byte[this.receiveBufferPoint];
            this.receiveBuffer.get(this.responseBody,0,receiveBufferPoint);
            LogUtil.d("receive data length ->",receiveBufferPoint);
        }catch (IOException e){
            this.httpException = new HttpException("read output input error",e);
            LogUtil.e("read input stream", Log.getStackTraceString(e));
        }finally {
            FileUtil.closeStream(this.inputStream);
            this.httpConnection.disconnect();
        }

        LogUtil.d("------ http request end ------");
    }
}
