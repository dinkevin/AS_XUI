package cn.dinkevin.xui.contact;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.Collections;
import java.util.List;
import cn.dinkevin.xui.R;
import cn.dinkevin.xui.fragment.AbstractFragment;
import cn.dinkevin.xui.widget.LetterIndexSideBar;

/**
 * 联系人列表 Fragment </br>
 * Created by ChengPengFei on 2016/10/25 0025.</br>
 */

public class ContactListViewFragment extends AbstractFragment {

    private ListView lsv_list;
    private TextView txt_letter;
    private LetterIndexSideBar view_letter;
    private ContactAdapter<ContactBean> contactAdapter;
    private PingYinComparator comparator;
    private CharacterParser characterParser;
    private List<? extends ContactBean> tempData;

    @Override
    protected int getFragmentLayout() {
        return R.layout.xui_view_contact;
    }

    @Override
    protected void initialWidget() {
        super.initialWidget();

        Fresco.initialize(getActivity());

        lsv_list = findViewInRootView(R.id.xui_list_view);
        txt_letter = findViewInRootView(R.id.txt_letterToast);
        view_letter = findViewInRootView(R.id.view_letter);

        contactAdapter = new ContactAdapter<>(getActivity());
        view_letter.setTextView(txt_letter);
        view_letter.setOnTouchingLetterChangedListener(new LetterIndexSideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String letter) {
                int position  = contactAdapter.getPositionForSection(letter.charAt(0));
                if(position != -1)
                    lsv_list.setSelection(position);
            }
        });
        comparator = new PingYinComparator();
        characterParser = CharacterParser.getInstance();
        lsv_list.setAdapter(contactAdapter);

        if(null != tempData)
            updateData(tempData);
    }

    /**
     * 绑定列表单击响应事件
     * @param listener
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener){
        lsv_list.setOnItemClickListener(listener);
    }

    /**
     * 更新数据列表
     * @param data
     */
    public void updateData(List<? extends ContactBean> data){

        tempData = data;
        if(null != contactAdapter && null != data){

            for(int i = 0; i < data.size(); i++){
                ContactBean contact = data.get(i);

                //汉字转换成拼音
                String pinyin = characterParser.getSelling(contact.getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if(sortString.matches("[A-Z]")){
                    contact.setSortLetters(sortString.toUpperCase());
                }else{
                    contact.setSortLetters("#");
                }
            }
            Collections.sort(data,comparator);

            // 更新列表数据源
            contactAdapter.clearViewCache();
            contactAdapter.getDataSource().clear();
            contactAdapter.getDataSource().addAll(data);
            contactAdapter.notifyDataSetChanged();

            tempData = null;
        }
    }
}
