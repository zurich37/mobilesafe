package com.zurich.mobile.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.zurich.mobile.R;
import com.zurich.mobile.service.GPSService;
import com.zurich.mobile.utils.SharedPreferenceUtil;

/**
 * 短信广播接收者
 * Created by weixinfei on 16/5/9.
 */
public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static String TAG = "SmsReceiver";

    public SmsReceiver() {
        Log.i("cky", "new SmsReceiver");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Cursor cursor = null;
        try {
            if (SMS_RECEIVED.equals(intent.getAction())) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] objs = (Object[]) intent.getExtras().get("pdus");
                    for (Object obj : objs) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                        String body = smsMessage.getMessageBody();
                        String sender = smsMessage.getOriginatingAddress();
                        if ("#*location*#".equals(body)) {
                            Log.i(TAG, "返回手机的位置信息");
                            Intent i = new Intent(context, GPSService.class);
                            context.startService(i);
                            SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
                            String lastlocation = SharedPreferenceUtil.getLocationPrefs();
                            if (TextUtils.isEmpty(lastlocation)) {
                                SmsManager.getDefault().sendTextMessage(sender, null, "getting location...", null, null);
                            } else {
                                SmsManager.getDefault().sendTextMessage(sender, null, lastlocation, null, null);
                            }
                            abortBroadcast();
                        } else if ("#*alarm*#".equals(body)) {
                            Log.i(TAG, "播放报警音乐");
                            MediaPlayer mediaplayer = MediaPlayer.create(context, R.raw.ylzs);
                            mediaplayer.setVolume(1.0f, 1.0f);
                            //mediaplayer.setLooping(true);
                            mediaplayer.start();
                            abortBroadcast();
                        } else if ("#*wipedata*#".equals(body)) {
                            Log.i(TAG, "清除手机数据");
                            //dpm.wipedata();
                            abortBroadcast();
                        } else if ("#*lockscreen*#".equals(body)) {
                            Log.i(TAG, "远程锁屏");
                            abortBroadcast();
                            //dpm.locknow();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("cky", "Exception : " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }
}
