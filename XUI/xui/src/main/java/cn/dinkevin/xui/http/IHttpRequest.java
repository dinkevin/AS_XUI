package cn.dinkevin.xui.http;

import java.util.List;
import java.util.Map;

/**
 * HttpPoster 请求接口 </br>
 * Created by ChengPengFei on 2016/11/10 0010.</br>
 */

public interface IHttpRequest extends ICancelable{

    /**
     * 设置 HttpPoster 请求回调
     * @param listener
     */
    void setOnHttpResponseListener(IHttpResponseListener listener);

    /**
     * 获取 HttpPoster 请求回调
     * @return
     */
    IHttpResponseListener getOnHttpResponseListener();

    /**
     * 设置请求参数
     * @param key
     * @param value
     */
    void setRequestParams(String key, String value);

    /**
     * 设置请求参数
     * @param params
     */
    void setRequestParams(Map<String, String> params);

    /**
     * 设置请求 body
     * @param body
     */
    void setRequestBody(byte[] body);

    /**
     * 设置请求 头信息
     * @param key
     * @param value
     */
    void setRequestHeader(String key, String value);

    /**
     * 设置请求 头信息
     * @param header
     */
    void setRequestHeader(Map<String, String> header);

    /**
     * 开始 HttpPoster 网络请求（此方法为同步）
     */
    void start();

    /**
     * 获取 HttpPoster 请求异常
     * @return
     */
    HttpException getException();

    /**
     * 获取服务器回复 responseHeader
     * @return
     */
    Map<String,List<String>> getResponseHeader();

    /**
     * 获取服务器回复 responseBody
     * @return
     */
    byte[] getResponseBody();

    /**
     * 网络请求地址
     * @return
     */
    String getUrl();

    /**
     * Http 请求方法
     * @return
     */
    HttpRequestMethod getMethod();
}
