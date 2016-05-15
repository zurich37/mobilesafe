package com.zurich.mobile.app;

import android.app.Application;
import android.content.Context;

/**
 *  全局应用程序上下文
 * Created by weixinfei on 16/5/14.
 */
public class MobileApplication extends Application {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }
}
