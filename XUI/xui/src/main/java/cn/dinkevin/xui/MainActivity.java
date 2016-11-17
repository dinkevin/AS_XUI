package cn.dinkevin.xui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import cn.dinkevin.xui.contact.ContactBean;
import cn.dinkevin.xui.contact.ContactListViewFragment;

public class MainActivity extends AppCompatActivity {

    ContactListViewFragment contactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactFragment = new ContactListViewFragment();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container,contactFragment).commit();

        ContactBean c1 = new ContactBean();
        ContactBean c2 = new ContactBean();
        ContactBean c3 = new ContactBean();
        ContactBean c4 = new ContactBean();
        ContactBean c5 = new ContactBean();
        c1.setName("安妮");
        c2.setName("宝强");
        c3.setName("李小龙");
        c4.setName("李浩");
        c5.setName("小七");
        c1.setIcon("http://img0.imgtn.bdimg.com/it/u=3964021315,663969033&fm=11&gp=0.jpg");
        c2.setIcon("http://img0.imgtn.bdimg.com/it/u=1855475426,1636913956&fm=11&gp=0.jpg");
        c3.setIcon("http://img1.imgtn.bdimg.com/it/u=1029423419,1598914982&fm=11&gp=0.jpg");
        c4.setIcon("http://img4.imgtn.bdimg.com/it/u=4256812062,1295661188&fm=11&gp=0.jpg");
        c5.setIcon("http://img5.imgtn.bdimg.com/it/u=2025858304,2293090079&fm=11&gp=0.jpg");

        List<ContactBean> data = new ArrayList<>();
        data.add(c5);
        data.add(c3);
        data.add(c1);
        data.add(c4);
        data.add(c2);
        contactFragment.updateData(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
