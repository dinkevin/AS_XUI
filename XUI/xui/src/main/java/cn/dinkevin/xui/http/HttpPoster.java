package cn.dinkevin.xui.http;

import android.content.Context;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.cache.DBCacheStore;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * HTTP 请求库 </br>
 * Created by ChengPengFei on 2016/11/10 0010.</br>
 */

public final class HttpPoster {

    // HTTP 请求队列
    private static BlockingDeque<IHttpRequest> requestQueue = new LinkedBlockingDeque<>();

    // HTTP 请求调度
    private static HttpRequestDispatcher requestDispatcher;

    private HttpPoster(){}

    /**
     * 初始化
     * @param context
     */
    public static void initial(Context context){

        // 不使用缓存
        NoHttp.Config config = new NoHttp.Config().setCacheStore(new DBCacheStore(context).setEnable(false));
        NoHttp.initialize(context, config);
        Logger.setDebug(true);

        // 启动 HTTP 请求调度
        requestDispatcher = new HttpRequestDispatcher(requestQueue);
        requestDispatcher.start();
    }

    /**
     * 添加 HTTP 网络请求
     * @param request
     */
    public static void add(IHttpRequest request){
        requestQueue.add(request);
    }
}
