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

    public static String getSettingPrefs(Context context, String key, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString(key, defValue);
    }

    public static void removeShare(Context context, String key) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString(key, "0").commit();
    }

    /*安全检查标志*/
    public static void setSafeSettingPrefs(Context context, String key, int value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putInt(key, value).commit();
    }

    public static int getSafeSettingPrefs(Context context, String key, int defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getInt(key, defValue);
    }

    /*设置手机防盗密码*/
    public static void setSafePasswordPrefs(Context context, String key, String value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString(key, value).commit();
    }

    public static String getSafePasswordPrefs(Context context, String key, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString(key, defValue);
    }

    /*设置手机防盗完成的标志*/
    public static void setLostFindConfigPrefs(Context context, String key, Boolean value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putBoolean(key, value).commit();
    }

    public static Boolean getLostFindConfigPrefs(Context context, String key, Boolean defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getBoolean(key, defValue);
    }

    /*防盗保护是否开启*/
    public static void setProtectConfigPrefs(Context context, String key, Boolean value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putBoolean(key, value).commit();
    }

    public static Boolean getProtectConfigPrefs(Context context, String key, Boolean defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getBoolean(key, defValue);
    }

    /*绑定sim卡*/
    public static void setBindNumberPrefs(Context context, String key, String value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString(key, value).commit();
    }

    public static String getBindNumberPrefs(Context context, String key, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString(key, defValue);
    }

    /*设置安全号码*/
    public static void setSafePhoneNumberPrefs(Context context, String key, String value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString(key, value).commit();
    }

    public static String getSafePhoneNumberPrefs(Context context, String key, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString(key, defValue);
    }
}
