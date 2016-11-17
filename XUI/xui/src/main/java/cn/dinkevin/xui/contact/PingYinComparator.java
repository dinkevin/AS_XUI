package cn.dinkevin.xui.contact;

import java.util.Comparator;

/**
 * 拼音对比排序 </br>
 * Created by ChengPengFei on 2016/10/25 0025.</br>
 */

public class PingYinComparator implements Comparator<ContactBean>{

    @Override
    public int compare(ContactBean contactA, ContactBean contactB) {

        // 对 ListView里面的数据根据ABCDEFG...来排序
        if(contactB.getSortLetters().equals("#")){
            return -1;
        }else if(contactA.getSortLetters().equals("#")){
            return 1;
        }else{
            return contactA.getSortLetters().compareTo(contactB.getSortLetters());
        }
    }
}
