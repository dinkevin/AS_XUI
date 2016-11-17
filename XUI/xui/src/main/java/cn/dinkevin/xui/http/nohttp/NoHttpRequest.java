package cn.dinkevin.xui.http.nohttp;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.CacheMode;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.Iterator;
import java.util.Map;

import cn.dinkevin.xui.http.BasicHttpRequest;
import cn.dinkevin.xui.http.HttpException;
import cn.dinkevin.xui.http.HttpRequestMethod;
import cn.dinkevin.xui.http.IHttpResponseListener;

/**
 * 基于 NoHttp 框架库封装的实现的 HTTP 网络请求
 * Created by ChengPengFei on 2016/11/10 0010.</br>
 */

public class NoHttpRequest extends BasicHttpRequest {

    // HttpPoster 请求
    protected Request<byte[]> httpRequest;

    public NoHttpRequest(String url){
        this(url, HttpRequestMethod.GET);
    }

    public NoHttpRequest(String url, HttpRequestMethod method){
        super(url,method);

        if(method == HttpRequestMethod.POST){
            this.httpRequest = NoHttp.createByteArrayRequest(this.url, RequestMethod.POST);
        }else{
            this.httpRequest = NoHttp.createByteArrayRequest(this.url, RequestMethod.GET);
        }
        this.httpRequest.setCacheMode(CacheMode.ONLY_REQUEST_NETWORK);
    }

    @Override
    public void setOnHttpResponseListener(IHttpResponseListener listener) {
        this.responseListener = listener;
    }

    /**
     * 设置 HttpPoster 请求 Header 中提交数据类型
     * @param contentType
     */
    public void setContentType(String contentType){
        this.httpRequest.setContentType(contentType);
    }

    @Override
    public void setRequestHeader(String key, String value) {
        this.httpRequest.addHeader(key,value);
    }

    @Override
    public void setRequestHeader(Map<String, String> header) {
        if(null != header){
            Iterator<String> it = header.keySet().iterator();
            while (it.hasNext()){
                String key = it.next();
                this.httpRequest.addHeader(key,header.get(key));
            }
        }
    }

    @Override
    public void setRequestParams(String key, String value) {
        this.httpRequest.set(key,value);
    }

    @Override
    public void setRequestParams(Map<String, String> params) {
        this.httpRequest.set(params);
    }

    @Override
    public void setRequestBody(byte[] body) {
        this.requestBody = body;
    }

    @Override
    public void start(){
        Response<byte[]> response = NoHttp.startRequestSync(this.httpRequest);

        // 错误检查
        Exception exception = response.getException();
        if(null != exception){
            int code = response.getHeaders().getResponseCode();
            this.httpException = new HttpException(code,exception.getMessage());
        }

        this.responseHeader = response.getHeaders().toResponseHeaders();
        this.responseBody = response.get();
    }

    @Override
    public void cancel() {
        this.httpRequest.cancel();
    }

    @Override
    public boolean isCanceled() {
        return this.httpRequest.isCanceled();
    }
}
