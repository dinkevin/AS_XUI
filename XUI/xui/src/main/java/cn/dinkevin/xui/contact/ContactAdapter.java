package cn.dinkevin.xui.contact;

import android.content.Context;
import android.widget.SectionIndexer;
import java.util.List;

import cn.dinkevin.xui.adapter.ViewHolderAdapter;

/**
 * 联系人列表数据源
 * Created by ChengPengFei on 2016/10/24 0024.</br>
 */

public class ContactAdapter<K extends ContactBean> extends ViewHolderAdapter<K,ContactViewHolder<K>> implements SectionIndexer{

    public ContactAdapter(Context context){
        super(context);
    }

    public ContactAdapter(Context context, List<K> data){
        super(context,data);
    }

    /**
     * 更新联系人列表数据
     * @param newData
     */
    public void updateData(List<K> newData){
        this.data = newData;
        clearViewCache();
        notifyDataSetChanged();
    }

    @Override
    protected ContactViewHolder createViewHolder(int position) {
        ContactViewHolder holder = new ContactViewHolder();

        // 字母分类显示判断
        int section = getSectionForPosition(position);
        int sectionPosition = getPositionForSection(section);
        if(position == sectionPosition)
            holder.switchToLetterModel();

        return holder;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据分类的首字母的 Char ASCII 值获取其第一次出现该首字母的位置
     * @param section
     * @return
     */
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = this.data.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取指定位置的分类的首字母的 ASCII 值
     * @param position
     * @return
     */
    @Override
    public int getSectionForPosition(int position) {
        return this.data.get(position).getSortLetters().charAt(0);
    }
}
