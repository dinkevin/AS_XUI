package cn.dinkevin.xui.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

/**
 * 通用的数据列表数据源适配器类
 * 
 * @author chengpengfei
 * @param <T> 对象类
 * @param <V> 对象类对应的 ViewHolder 显示类
 */
public abstract class ViewHolderAdapter<T, V extends ViewHolder<T>> extends AbstractAdapter<T> {

	/**
	 * 构造器
	 * @param context
	 */
	public ViewHolderAdapter(Context context) {
		super(context);
	}
	
	/**
	 * 构造器，传入数据
	 * @param context
	 * @param data
	 */
	public ViewHolderAdapter(Context context,List<T> data) {
		super(context,data);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected View createItemView(int position) {
		View view = createViewHolder(position).inflate(context, null, false);
		V holder = (V) view.getTag();
		holder.setData(getItem(position));
		return view;
	}

	/**
	 * 创建 ViewHolder
	 * @param position 列表索引
	 * @return
     */
	protected abstract V createViewHolder(int position);
}
