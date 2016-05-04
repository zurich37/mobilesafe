package com.zurich.mobile.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

/**
 * 锁屏清理进程
 * Created by weixinfei on 16/5/3.
 */
public class AutoKillService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private InnerScreenOffReceiver screenOffReceiver ;


    private class InnerScreenOffReceiver extends BroadcastReceiver {
        private static final String TAG = "InnerScreenOffReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"屏幕被锁定了。。。");
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> infos = am.getRunningAppProcesses();
            for(ActivityManager.RunningAppProcessInfo info: infos){
                am.killBackgroundProcesses(info.processName);
            }
        }
    }

    @Override
    public void onCreate() {
        screenOffReceiver = new InnerScreenOffReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenOffReceiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(screenOffReceiver);
        screenOffReceiver = null;
        super.onDestroy();
    }

}
