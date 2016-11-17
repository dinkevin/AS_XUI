package cn.dinkevin.xui.http;

/**
 * Created by ChengPengFei on 2016/11/9 0009.</br>
 */

public interface ICancelable {

    /**
     * 取消
     */
    void cancel();

    /**
     * 是否已经取消
     * @return
     */
    boolean isCanceled();
}
