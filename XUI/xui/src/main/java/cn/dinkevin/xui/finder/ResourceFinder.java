package cn.dinkevin.xui.finder;

import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.dinkevin.xui.util.LogUtil;

/**
 * 资源 Finder 工具类</br>
 * 在调用此方法中的接口前要一定要调用 {@link #initial(Context)} 进行初始化</br>
 * 建议在 Application 实现类中的 onCreate 方法中调用 {@link #initial(Context)}，{@link #initial(Context)} 调用一次即可。
 * @author chengpengfei
 */
public class ResourceFinder {
	
	private ResourceFinder(){}
	
	private static String className;
	private static Map<String, Map<String, Object>> resourceMap = new HashMap<>();

	/**
	 * 在 Application 实现类中的 onCreate 方法中调用
	 * @param context
	 */
	public static void initial(Context context) {
		if (TextUtils.isEmpty(className))
			className = context.getPackageName() + ".R";
		
		if (className != null) {
			if (resourceMap.size() == 0) {
				Class<?>[] classArray;
				try {
					classArray = Class.forName(className).getClasses();

					if (classArray != null) {
						for (Class<?> cls : classArray) {
							String typeName = cls.getSimpleName().toLowerCase(Locale.getDefault());

							Map<String, Object> fieldMap = resourceMap.get(typeName);
							if(null == fieldMap){
								fieldMap = new HashMap<>();
								resourceMap.put(typeName, fieldMap);
							}
							
							Field[] fields = cls.getFields();
							for (Field f : fields) {
								fieldMap.put(f.getName().toLowerCase(Locale.getDefault()), f.get(null));
							}
						}
					}
				} catch (Exception e) {
					LogUtil.e("ResourceFinder initial Error",e.getMessage());
				}
			}
		}
	}

	/**
	 * 查找指定名称的资源
	 * @param type
	 * @param name
     * @return
     */
	private static Object searchResource(String type, String name) {
		
		if (className != null) {
			if (resourceMap.containsKey(type)) {
				if (resourceMap.get(type).containsKey(name)) {
					return resourceMap.get(type).get(name);
				}
			}
		}
		return null;
	}

	/**
	 * 获取资源Id
	 * @param resType 资源类型 {@link ResType}
	 * @param name 资源名称
	 * @return
	 */
	public static int getResourceId(ResType resType, String name) {
		Object obj = searchResource(resType.toString(), name);
		if (obj == null)
			throw new RuntimeException("getData resource id error:(packageName=" + className + " type=" + resType + " name=" + name);

		return (Integer) obj;
	}

	/**
	 * 读取字符串
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getString(Context context, String name) {
		int i = getResourceId(ResType.STRING, name);
		return context.getString(i);
	}

	/**
	 * 读取属性数组
	 * @param name
	 * @return
	 */
	public static int[] getStyleableAttributes(String name) {
		Object obj = searchResource(ResType.STYLEABLE.toString(), name);
		if (obj != null) {
			return (int[]) obj;
		}
		return null;
	}

	/**
	 * 资源类型
	 */
	public enum ResType {
		LAYOUT, ID, DRAWABLE, STYLE, STRING, COLOR, DIMEN, RAW, ANIM, STYLEABLE;

		@Override
		public String toString() {
			return this.name().toLowerCase(Locale.getDefault());
		}
	}
}
