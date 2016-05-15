package com.zurich.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

import com.zurich.mobile.utils.SharedPreferenceUtil;

/**
 * sim卡改变广播接收者
 * Created by weixinfei on 16/5/10.
 */
public class BootCompleteReceiver extends BroadcastReceiver {

    private TelephonyManager tm;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 检查当前手机的sim卡， 读取原来绑定的sim卡 ，如果发现两个sim卡不一致 说明手机可能被盗了。需要偷偷的发送报警信息。
        boolean protecting = SharedPreferenceUtil.getProtectConfigPrefs();
        if (protecting) {
            String bindsim = SharedPreferenceUtil.getBindNumberPrefs();
            tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String realsim = tm.getSimSerialNumber();
            if (bindsim.equals(realsim)) {
                // 就是你的手机和你的卡
            } else {
                String safenumber = SharedPreferenceUtil.getSafePhoneNumberPrefs();
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(safenumber, null, "sim card changed!", null, null);
            }
        }
    }
}
