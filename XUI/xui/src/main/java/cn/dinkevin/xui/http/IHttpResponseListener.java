package cn.dinkevin.xui.http;

import java.util.List;
import java.util.Map;

/**
 * HttpPoster 请求回复接口
 * Created by ChengPengFei on 2016/11/10 0010.</br>
 */

public interface IHttpResponseListener extends IHttpError{

    /**
     * HttpPoster 请求接收到服务器回复回调
     * @param header
     * @param body
     */
    void onHttpResponse(Map<String, List<String>> header, byte[] body);
}
