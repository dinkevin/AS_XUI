package cn.dinkevin.xui.util;

import android.widget.Toast;
import cn.dinkevin.xui.thread.ThreadExecutor;

/**
 * Toast 工具类</br>
 * 在调用此方法中的接口前要一定要调用对 {@link ThreadExecutor} 进行初始化</br>
 * @author chengpengfei
 */
public class ToastUtil {
	
	private ToastUtil(){}
	
	/**
	 * 调用系统的 Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	 * @param stringId
	 */
	public static void showShort(int stringId){
		
		String message = ThreadExecutor.getContext().getString(stringId);
		showShort(message);
	}
	
	/**
	 * 调用系统的 Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	 * @param message
	 */
	public static void showShort(final String message){
		
		if(ThreadExecutor.isOnMainThread()) 
			Toast.makeText(ThreadExecutor.getContext(), message, Toast.LENGTH_SHORT).show();
		else
			ThreadExecutor.runOnMainThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ThreadExecutor.getContext(), message, Toast.LENGTH_SHORT).show();
				}
			});
	}
	
	/**
	 * 调用系统的 Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	 * @param stringId
	 */
	public static void showLong(int stringId){
		String message = ThreadExecutor.getContext().getString(stringId);
		showLong(message);
	}
	
	/**
	 * 调用系统的 Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	 * @param message
	 */
	public static void showLong(final String message){
		if(ThreadExecutor.isOnMainThread())
			Toast.makeText(ThreadExecutor.getContext(), message, Toast.LENGTH_LONG).show();
		else
			ThreadExecutor.runOnMainThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ThreadExecutor.getContext(), message, Toast.LENGTH_LONG).show();
				}
			});
	}
}
