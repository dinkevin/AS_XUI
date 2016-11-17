package cn.dinkevin.xui.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 字符串处理工具类
 * @author chengpengfei
 *
 */
public final class StringUtil {

	private StringUtil(){}
	
	/**
	 * 截取字符串
	 * @param source 
	 * @param MaxLength 最大长度
	 * @param suffix 如果给定的字符长度大于指定的最大长度，零取后所加的后缀
	 * @return
	 */
	public static String cutString(@NonNull String source, @NonNull int MaxLength, String suffix){
		if(source != null)
		{
			if(source.length() > MaxLength){
				int index = MaxLength - (suffix != null ? suffix.length():0);
				if(index > 0){
					return source.substring(0,index);
				}
			}
		}
		return source;
	}
	
	/**
	 * 判断该字段是否为空，
	 * @param str 如果 str 为 null或者长度为 0 则返回 null
	 */
	public static boolean isEmpty(String str){
		if(str == null || str.trim().length() < 1){
			return true;
		}
		return false;
	}

	/**
	 * 处理网络路径中包含非 ASCII 码的文字
	 * @param url
	 * @return
     */
	public static String encodeUrl(String url){
		StringBuffer buffer = new StringBuffer();
		try{
			for(int i = 0;i < url.length();i++){
				char ch = url.charAt(i);
				String encodeCh = String.valueOf(ch);
				if(ch > 127){
					encodeCh = URLEncoder.encode(encodeCh, "utf-8");
				}
				buffer.append(encodeCh);
			}
		}catch (UnsupportedEncodingException e) {
			LogUtil.e(url, Log.getStackTraceString(e));
		}
		return buffer.toString();
	}

	public static String printToHex(byte[] data){
		StringBuffer buffer = new StringBuffer();
		if(null != data){
			for(byte b : data){
				int temp = b & 0xff;
				String ch = Integer.toHexString(temp);
				if(ch.length() < 2)
					ch = "0" + ch;
				buffer.append(ch);
			}
		}
		return buffer.toString();
	}
}
