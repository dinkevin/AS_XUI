package cn.dinkevin.xui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象数据源适配器基类
 * @author chengpengfei
 */
public abstract class AbstractAdapter<T> extends BaseAdapter{
	
	protected List<T> data = new ArrayList<>();
	protected Context context;
	protected LayoutInflater inflater;
	protected volatile SparseArray<View> viewCache = new SparseArray<View>();		// View 缓存，用于加快浏览速度
	
	public AbstractAdapter(Context context){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}
	
	/**
	 * 构造器，传入数据
	 * @param context
	 * @param data
	 */
	public AbstractAdapter(Context context,List<T> data) {
		this.data = data;
		this.context = context;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public T getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * 获取 Adapter 对应的列表数据
	 * @return
	 */
	public List<T> getDataSource(){
		return data;
	}

	/**
	 * 设置 Adapter 对应的列表数据
	 * @param newData
     */
	public void setDataSource(List<T> newData){
		this.data = newData;
	}
	
	/**
	 * 清空 viewCache
	 */
	public void clearViewCache(){
		viewCache.clear();
	}

	/**
	 * 通知数据源对应的列表进行数据更新，如果需要清空已经存在的 viewCache ，请调用 {@link #clearViewCache()}
	 */
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	/**
	 * 生成 Item View，生成的 View 将被放入 viewCache 中，</br>
	 * 如果想要重生成全部的 view，请调用 {@link #clearViewCache()}。</br>
	 * 载入布局文件可以调用 {@link #inflateLayout(int)}
	 * @param position 列表索引
	 * @return Item 对应的 View
	 */
	protected abstract View createItemView(int position);
	
	/**
	 * 载入指定的布局 View
	 * @param layoutResId 布局资源 Id
	 * @return View
	 */
	protected View inflateLayout(int layoutResId){
		return inflater.inflate(layoutResId, null);
	}
	
	/**
	 * 获取 Item 绑定的 ViewHolder
	 * @param position Item 索引
	 * @return ViewHolder
	 */
	@SuppressWarnings("unchecked")
	public <H extends ViewHolder<T>> H getViewHolder(int position){
		View view = getView(position);
		if(null != view){
			Object tag = view.getTag();
			if(null != tag && tag instanceof ViewHolder){
				return (H)tag;
			}
		}
		return null;
	}
	
	/**
	 * 获取指定位置 Item 对应的 View
	 * @param position Item 索引
	 * @return Item 对应的 View
	 */
	public View getView(int position){
		return getView(position, null, null);
	}
	
	@Override
	public synchronized View getView(int position, View view, ViewGroup parent){
		view = viewCache.get(position);
		if(null == view)
		{
			view = createItemView(position);
			viewCache.put(position, view);
		}
		return view;
	}
}
