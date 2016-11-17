package cn.dinkevin.xui.contact;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 联系人实体类
 * Created by ChengPengFei on 2016/10/25 0025.</br>
 */

public class ContactBean implements Parcelable{

    private int id;         // 联系人ID
    private String name;    // 联系人名字
    private String icon;    // 联系人头像
    private String sortLetters;     // 联系人显示拼音首字母

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取名字
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名字
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取头像
     * @return
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置头像
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取联系人拼音首字母
     * @return
     */
    public String getSortLetters() {
        return sortLetters;
    }

    /**
     *  设置联系人拼音首字母
     * @param sortLetters
     */
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public static final Parcelable.Creator<ContactBean> CREATOR = new Creator<ContactBean>() {

        @Override
        public ContactBean createFromParcel(Parcel source) {
            ContactBean c = new ContactBean();
            c.setId(source.readInt());
            c.setName(source.readString());
            c.setIcon(source.readString());
            c.setSortLetters(source.readString());
            return c;
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(sortLetters);
    }
}
