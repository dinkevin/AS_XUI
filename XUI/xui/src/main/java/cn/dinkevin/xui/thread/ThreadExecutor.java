package cn.dinkevin.xui.thread;

import android.content.Context;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.dinkevin.xui.util.HandlerUtil;

/**
 * 线程工具类</br>
 * 在调用此方法中的接口前要一定要调用 {@link #initial(Context)} 进行初始化</br>
 * 建议在 Application 实现类中的 onCreate 方法中调用 {@link #initial(Context)}，{@link #initial(Context)} 调用一次即可。
 * @author chengpengfei
 */
public class ThreadExecutor {

	private ThreadExecutor() {}

	/**
	 * 全局上下文环境
	 */
	private static Context globalContext;
	
	/**
	 * 声明线程池,默认最多只能有 7 个线程同时执行
	 */
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(7);

	/**
	 * 初始化，此接口在 Application 实现类的 onCreate 中调用。
	 * @param context
	 */
	public static void initial(Context context) {
		globalContext = context;
	}
	
	/**
	 * 获取上下文环境
	 * @return
	 */
	public static Context getContext(){
		return globalContext;
	}

	/**
	 * 判断是否在主线程
	 * 
	 * @return
	 */
	public static boolean isOnMainThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

	/**
	 * Throws an {@link IllegalArgumentException} if called on a
	 * thread other than the main thread.
	 */
	public static void assertMainThread() {
		if (!isOnMainThread()) {
			throw new IllegalArgumentException("You must call this method on the main thread");
		}
	}

	/**
	 * Throws an {@link IllegalArgumentException} if called on the
	 * main thread.
	 */
	public static void assertBackgroundThread() {
		if (!isOnBackgroundThread()) {
			throw new IllegalArgumentException("YOu must call this method on a background thread");
		}
	}

	/**
	 * Returns {@code true} if called on the main thread, {@code false}
	 * otherwise.
	 */
	public static boolean isOnBackgroundThread() {
		return !isOnMainThread();
	}
	
	/**
	 * 在主线程执行此代码片断
	 * @param runnable
	 * @return true -> 添加成功；false -> 添加失败
	 */
	public static boolean runOnMainThread(Runnable runnable){
		return HandlerUtil.getInstance().post(runnable);
	}
	
	/**
	 * 在主线程执行此代码片断，在指定延时间后执行
	 * @param runnable
	 * @param delay 延时时间 大于 0
	 * @return true -> 添加成功；false -> 添加失败
	 */
	public static boolean runOnMainThread(Runnable runnable,long delay){
		if(delay < 1) return false;
		return HandlerUtil.getInstance().postDelayed(runnable,delay);
	}
	
	/**
	 * 添加到线程池中执行
	 * @param runnable
	 * @return true -> 添加成功；false -> 添加失败
	 */
	public static boolean runInThreadPool(Runnable runnable){
		if(threadPool.isShutdown()) return false;
		threadPool.execute(runnable);
		return true;
	}
	
	/**
	 * 此接口调用的时候要慎重，关闭线程池后，则无法正常调用 {@link #runInThreadPool(Runnable)} 
	 */
	public static void shutdownThreadPool(){
		threadPool.shutdown();
	}
}
