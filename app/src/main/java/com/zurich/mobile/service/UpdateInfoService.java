package com.zurich.mobile.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by weixi_000 on 2016/3/9.
 * 版本更新的服务
 */
public class UpdateInfoService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
