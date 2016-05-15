package com.zurich.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by weixinfei on 16/5/9.
 */
public class HeadlessSmsSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("cky","HeadlessSmsSendService: "+intent);
        return null;
    }
}
