package cn.dinkevin.xui.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 映射工具类
 * @author chengpengfei
 */
public final class ReflectUtil{

	private ReflectUtil() {}

	/**
	 * 获取指定对象中的属性
	 * @param obj
	 * @param fieldName
     * @return
     */
	public static Field getField(Object obj,String fieldName){
		try{
			return obj.getClass().getField(fieldName);
		}catch (NoSuchFieldException e){
			LogUtil.e(obj.getClass().getName(),"no field",fieldName);
			return null;
		}
	}

	/**
	 * 获取指定对象中的方法
	 * @param obj
	 * @param methodName
     * @return 如果不存在对应名称的方法，则返回 null
     */
	public static Method getMethod(Object obj, String methodName){
		try{
			return obj.getClass().getMethod(methodName);
		}catch (NoSuchMethodException e){
			LogUtil.e(obj.getClass().getName(),"no method",methodName);
			return null;
		}
	}

	/**
	 *
	 * @param obj
	 * @param method
	 * @param args 参数
     * @return 执行结果
     */
	public static Object invoke(Object obj,Method method,Object... args){
		try{
			method.setAccessible(true);
			return method.invoke(obj,args);
		}catch (InvocationTargetException e){
			LogUtil.e(obj.getClass().getName(),"invoke InvocationTargetException",e.getMessage());
		}catch (IllegalAccessException e){
			LogUtil.e(obj.getClass().getName(),"invoke IllegalAccessException",e.getMessage());
		}
		return null;
	}

	/**
	 *
	 * @param obj
	 * @param methodName
	 * @param args
     * @return
     */
	public static Object invoke(Object obj,String methodName,Object... args){
		Method method = getMethod(obj,methodName);
		if(null != method){
			return invoke(obj,method,args);
		}
		return null;
	}
}
