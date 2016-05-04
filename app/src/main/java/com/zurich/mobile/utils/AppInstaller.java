package com.zurich.mobile.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * 应用安装器
 * Created by weixinfei on 16/5/4.
 */
public class AppInstaller {
    public static void install(Activity activity, String filePath) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
}
