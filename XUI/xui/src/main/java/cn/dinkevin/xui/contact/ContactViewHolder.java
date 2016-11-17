package cn.dinkevin.xui.contact;

import android.view.View;
import android.widget.TextView;
import com.facebook.drawee.view.SimpleDraweeView;

import cn.dinkevin.xui.R;
import cn.dinkevin.xui.adapter.ViewHolder;

/**
 * 联系人列表 Item ViewHolder </br>
 * Created by ChengPengFei on 2016/10/25 0025.</br>
 */
public class ContactViewHolder<T extends ContactBean> extends ViewHolder<T> {

    private SimpleDraweeView sdv_icon;  // 联系人头像
    private TextView txt_name;          // 联系人名字
    private TextView txt_letter;        // 分类字母

    private boolean switchLetter = false;

    @Override
    protected int getItemLayout() {
        return R.layout.xui_item_contact;
    }

    @Override
    protected void initWidgets() {
        sdv_icon = viewFinder.findViewById(R.id.sdv_icon);
        txt_name = viewFinder.findViewById(R.id.txt_name);
        txt_letter = viewFinder.findViewById(R.id.txt_letter);

        invalid();
    }

    /**
     * 切换到联系人分类字母显示模式
     */
    public void switchToLetterModel(){
        switchLetter = true;
        invalid();
    }

    @Override
    public void invalid() {
        T data = getData();
        if(null != data && null != txt_letter && null != sdv_icon && null != txt_name){

            if(switchLetter){
                txt_letter.setVisibility(View.VISIBLE);
                sdv_icon.setVisibility(View.GONE);
                txt_name.setVisibility(View.GONE);
                txt_letter.setText(String.valueOf(data.getSortLetters().toUpperCase().charAt(0)));
                switchLetter = false;
            }
            else{
                txt_letter.setVisibility(View.GONE);
                sdv_icon.setVisibility(View.VISIBLE);
                txt_name.setVisibility(View.VISIBLE);
                sdv_icon.setImageURI(data.getIcon());
                txt_name.setText(data.getName());
            }
        }
    }
}
