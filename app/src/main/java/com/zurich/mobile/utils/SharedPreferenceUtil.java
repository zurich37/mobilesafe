package com.zurich.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by weixinfei
 */
public class SharedPreferenceUtil {
    public static final String SETTING_PREFS_NAME = "setting";

    public static final String PACKAGE_INFOS_VERSION_NAME = "version_name";


    public static void setSettingPrefs(Context context, String key, String value) {

        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString(key, value).commit();
    }

    public static void removeShare(Context context, String key) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString(key, "0").commit();
    }

    public static String getSettingPrefs(Context context, String key, String defValue) {
        String result = context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString(key, defValue);
        return result;
    }
}
