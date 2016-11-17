package cn.dinkevin.xui.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTTP 请求基类 </br>
 * Created by ChengPengFei on 2016/11/10 0010.</br>
 */

public abstract class BasicHttpRequest implements IHttpRequest{

    // HTTP请求，服务器回复回调
    protected IHttpResponseListener responseListener;

    // Http 请求，服务器回复的 header 与 body 数据
    protected Map<String,List<String>> responseHeader;
    protected byte[] responseBody;

    // Http 请求，服务器请求的 header 、参数 与 body
    protected Map<String,String> requestHeader = new HashMap<>();
    protected Map<String,String> requestParams = new HashMap<>();

    protected byte[] requestBody;

    // HttpPoster 请求网络路径
    protected String url;

    // HttpPoster 请求异常
    protected HttpException httpException;

    // Http 请求是否取消标识
    protected boolean canceled;

    // Http 请求方法
    protected HttpRequestMethod httpMethod;

    public BasicHttpRequest(String url){
        this(url, HttpRequestMethod.GET);
    }

    public BasicHttpRequest(String url, HttpRequestMethod method){
        this.url = url;
        this.httpMethod = method;
    }

    @Override
    public void setOnHttpResponseListener(IHttpResponseListener listener) {
        this.responseListener = listener;
    }

    @Override
    public void setRequestParams(String key, String value) {
        this.requestParams.put(key,value);
    }

    @Override
    public void setRequestParams(Map<String, String> params) {
        if(null != params){
            this.requestParams.putAll(params);
        }
    }

    @Override
    public void setRequestHeader(String key, String value) {
        this.requestHeader.put(key,value);
    }

    @Override
    public void setRequestHeader(Map<String, String> header) {
        if(null != header)
            this.requestHeader.putAll(header);
    }

    @Override
    public void setRequestBody(byte[] body) {
        this.requestBody = body;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public void cancel() {
        this.canceled = true;
    }

    @Override
    public boolean isCanceled() {
        return this.canceled;
    }

    @Override
    public HttpException getException() {
        return this.httpException;
    }

    @Override
    public Map<String, List<String>> getResponseHeader() {
        return this.responseHeader;
    }

    @Override
    public byte[] getResponseBody() {
        return this.responseBody;
    }

    @Override
    public IHttpResponseListener getOnHttpResponseListener() {
        return this.responseListener;
    }

    @Override
    public HttpRequestMethod getMethod() {
        return this.httpMethod;
    }
}
