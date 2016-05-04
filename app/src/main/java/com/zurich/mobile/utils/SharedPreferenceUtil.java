package com.zurich.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by weixinfei
 */
public class SharedPreferenceUtil {
    public static final String SETTING_PREFS_NAME = "setting";

    public static final String PACKAGE_INFOS_VERSION_NAME = "version_name";


    public static void setSettingPrefs(Context context, String value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString("setting", value).commit();
    }

    public static String getSettingPrefs(Context context, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString("setting", defValue);
    }

    public static void removeShare(Context context, String key) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString(key, "0").commit();
    }

    /*安全检查标志*/
    public static void setSafeSettingPrefs(Context context, int value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putInt("check_safe_setting", value).commit();
    }

    public static int getSafeSettingPrefs(Context context, int defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getInt("check_safe_setting", defValue);
    }

    /*设置手机防盗密码*/
    public static void setSafePasswordPrefs(Context context, String value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString("password", value).commit();
    }

    public static String getSafePasswordPrefs(Context context, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString("password", defValue);
    }

    /*设置手机防盗完成的标志*/
    public static void setLostFindConfigPrefs(Context context, Boolean value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putBoolean("safe_config", value).commit();
    }

    public static Boolean getLostFindConfigPrefs(Context context, Boolean defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getBoolean("safe_config", defValue);
    }

    /*防盗保护是否开启*/
    public static void setProtectConfigPrefs(Context context, Boolean value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putBoolean("protecting", value).commit();
    }

    public static Boolean getProtectConfigPrefs(Context context, Boolean defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getBoolean("protecting", defValue);
    }

    /*绑定sim卡*/
    public static void setBindNumberPrefs(Context context, String value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString("sim", value).commit();
    }

    public static String getBindNumberPrefs(Context context, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString("sim", defValue);
    }

    /*设置安全号码*/
    public static void setSafePhoneNumberPrefs(Context context, String value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putString("safenumber", value).commit();
    }

    public static String getSafePhoneNumberPrefs(Context context, String defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getString("safenumber", defValue);
    }

    /*是否显示系统进程*/
    public static void setSysTaskVisiblePrefs(Context context, Boolean value) {
        SharedPreferences prefrence = context.getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefrence.edit();
        editor.putBoolean("showsystem", value).commit();
    }

    public static Boolean getSysTaskVisiblePrefs(Context context, Boolean defValue) {
        return context.getSharedPreferences(SETTING_PREFS_NAME, 0).getBoolean("showsystem", defValue);
    }

}
