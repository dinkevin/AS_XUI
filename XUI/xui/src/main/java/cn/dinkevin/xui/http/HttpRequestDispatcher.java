package cn.dinkevin.xui.http;

import android.os.Process;

import java.util.concurrent.BlockingDeque;

import cn.dinkevin.xui.util.LogUtil;
import cn.dinkevin.xui.util.HandlerUtil;

/**
 * HttpPoster 请求调度 </br>
 * Created by ChengPengFei on 2016/10/31 0031.</br>
 */

public class HttpRequestDispatcher extends Thread {

    // 上传请求队列
    private BlockingDeque<IHttpRequest> pushRequestQueue;

    // 退出标识
    private boolean quit = false;

    public HttpRequestDispatcher(BlockingDeque<IHttpRequest> pushRequestQueue){
        this.pushRequestQueue = pushRequestQueue;
    }

    @Override
    public void run() {

        // 设置当前线程的优先级
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (!quit){

            if(pushRequestQueue.size() < 1)
                continue;

            IHttpRequest request;
            try {
                request = pushRequestQueue.take();
            } catch (InterruptedException e) {
                if (!quit)
                    return;
                continue;
            }

            if (request.isCanceled()) {
                LogUtil.d(request + " is canceled");
                continue;
            }

            // 执行网络请求
            request.start();

            // 执行 HttpPoster 回复
            HandlerUtil.getInstance().post(new HttpResponseThread(request));
        }
    }

    /**
     * HttpPoster 请求回复
     */
    class HttpResponseThread implements Runnable {

        IHttpRequest request;

        HttpResponseThread(IHttpRequest request){
            this.request = request;
        }

        @Override
        public void run() {
            if(null != this.request){
                IHttpResponseListener listener = this.request.getOnHttpResponseListener();
                if(null != listener){
                    HttpException exception = this.request.getException();
                    if(null != exception){
                        listener.onHttpError(exception);
                    }
                    else{
                        listener.onHttpResponse(request.getResponseHeader(),request.getResponseBody());
                    }
                }
            }
        }
    }

    /**
     * 取消
     */
    public void quit(){
        this.quit = true;
        interrupt();
    }

    /**
     * 是否退出
     * @return
     */
    public boolean isQuit(){
        return quit;
    }
}
