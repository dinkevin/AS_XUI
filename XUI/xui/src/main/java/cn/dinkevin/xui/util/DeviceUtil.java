package cn.dinkevin.xui.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import cn.dinkevin.xui.thread.ThreadExecutor;

/**
 * 设备相关工具类</br>
 * Created by ChengPengFei on 2016/10/30 0030.</br>
 */

public final class DeviceUtil {

    private static String g_deviceInfo;

    /**
     * 计算设备唯一码
     * @return 32位字母
     */
    public static String getUniqueCode(){
        if(TextUtils.isEmpty(g_deviceInfo)){
            String deviceInfo = readDeviceInfo();
            g_deviceInfo = MD5Util.calculate(deviceInfo);
        }
        return g_deviceInfo;
    }

    /**
     * 读取设备信息
     * @return
     */
    private static String readDeviceInfo(){

        StringBuffer buffer = new StringBuffer();
        Context context = ThreadExecutor.getContext();
        if(null == context){
            LogUtil.e("ThreadExecutor need initial");
        }

        // 尝试读取 MAC 地址
        try{
            WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
            // 确认 Wifi 打开状态
            if(!wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(true);
            }

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(null == wifiInfo || TextUtils.isEmpty(wifiInfo.getMacAddress())){
                return buffer.toString();
            }
            buffer.append("wifi:");
            buffer.append(wifiInfo.getMacAddress());
        }catch (Exception e){
            LogUtil.ef("readDeviceInfo, get wifi mac",e.getMessage());
        }

        // 尝试读取 IMEI
        try{
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            buffer.append(TextUtils.isEmpty(imei) ? "" : "imei:"+imei);

            String serialNumber = telephonyManager.getSimSerialNumber();
            buffer.append(TextUtils.isEmpty(serialNumber) ? "" : "sn:"+serialNumber);
        }catch (Exception e){
            LogUtil.ef("make unique code, get imei",e.getMessage());
        }

        // 尝试读取蓝牙地址
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(null != bluetoothAdapter){
            String bluetoothAddress = bluetoothAdapter.getAddress();
            buffer.append("bt:");
            buffer.append(bluetoothAddress);
        }

        // 获取系统首次启动时生成的 android_id
        String androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        buffer.append(TextUtils.isEmpty(androidId) ? "":"id:" + androidId);

        // 读取设备品牌、基板名称、设备制造商、设备驱动名称
        buffer.append("brand:" + Build.BRAND);
        buffer.append("board:" + Build.BOARD);
        buffer.append("device:" + Build.DEVICE);
        return buffer.toString();
    }
}
