package cn.dinkevin.xui.http;

/**
 * 请求服务器异常 </br>
 * Created by ChengPengFei on 2016/11/14 0014.</br>
 */

public interface IHttpError {

    /**
     * 请求服务器异常回调
     * @param e
     */
    void onHttpError(HttpException e);
}
