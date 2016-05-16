package com.zurich.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zurich.mobile.app.MobileApplication;

/**
 * Created by weixinfei
 */
public class SharedPreferenceUtil {
    public static final String SETTING_PREFS_NAME = "setting";

    public static final String PACKAGE_INFOS_VERSION_NAME = "version_name";

    private static SharedPreferences getSharedPreferences() {
        return MobileApplication.getContext().getSharedPreferences(SETTING_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key, String defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(key, value).commit();
    }

    public static int getInt(Context context, String key, int defValue) {
        return PreferenceManager.getDefaultSharedPreferences(context).getInt(
                key, defValue);
    }

    public static void putInt(Context context, String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putInt(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, Boolean value) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, value);
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(key, value).commit();
    }

    public static void setLocalVersionPrefs(String value) {
        getSharedPreferences().edit().putString("version", value).apply();
    }

    public static String getLocalVersionPrefs() {
        return getSharedPreferences().getString("version", "");
    }

    /*安全检查标志*/
    public static void setSafeSettingPrefs(int value) {
        getSharedPreferences().edit().putInt("check_safe_setting", value).apply();
    }

    public static int getSafeSettingPrefs() {
        return getSharedPreferences().getInt("check_safe_setting", 0);
    }

    /*设置手机防盗密码*/
    public static void setSafePasswordPrefs(String value) {
        getSharedPreferences().edit().putString("password", value).apply();
    }

    public static String getSafePasswordPrefs() {
        return getSharedPreferences().getString("password", null);
    }

    /*设置手机防盗完成的标志*/
    public static void setLostFindConfigPrefs(Boolean value) {
        getSharedPreferences().edit().putBoolean("safe_config", value).apply();
    }

    public static Boolean getLostFindConfigPrefs() {
        return getSharedPreferences().getBoolean("safe_config", false);
    }

    /*防盗保护是否开启*/
    public static void setProtectConfigPrefs(Boolean value) {
        getSharedPreferences().edit().putBoolean("protecting", value).apply();
    }

    public static Boolean getProtectConfigPrefs() {
        return getSharedPreferences().getBoolean("protecting", false);
    }

    /*绑定sim卡*/
    public static void setBindNumberPrefs(String value) {
        getSharedPreferences().edit().putString("sim", value).apply();
    }

    public static String getBindNumberPrefs() {
        return getSharedPreferences().getString("sim", null);
    }

    /*设置安全号码*/
    public static void setSafePhoneNumberPrefs(String value) {
        getSharedPreferences().edit().putString("safenumber", value).apply();
    }

    public static String getSafePhoneNumberPrefs() {
        return getSharedPreferences().getString("safenumber", "");
    }

    /*是否显示系统进程*/
    public static void setSysTaskVisiblePrefs(Boolean value) {
        getSharedPreferences().edit().putBoolean("showsystem", value).apply();
    }

    public static Boolean getSysTaskVisiblePrefs() {
        return getSharedPreferences().getBoolean("showsystem", false);
    }

    /*保存GPS位置*/
    public static void setLocationPrefs(String value) {
        getSharedPreferences().edit().putString("lastlocation", value).apply();
    }

    public static String getLocationPrefs() {
        return getSharedPreferences().getString("lastlocation", "");
    }

    /*保存toast背景*/
    public static void setToastPrefs(int value) {
        getSharedPreferences().edit().putInt("which", value).apply();
    }

    public static int getToastPrefs() {
        return getSharedPreferences().getInt("which", 0);
    }

    /*保存toast位置*/
    public static void setToastLoactionPrefs(String value) {
        getSharedPreferences().edit().putString("toast_location", value).apply();
    }

    public static String getToastLoactionPrefs() {
        return getSharedPreferences().getString("toast_location", "");
    }

    /*是否自动更新*/
    public static void setAutoUpdatePrefs(Boolean value) {
        getSharedPreferences().edit().putBoolean("auto_update", value).apply();
    }

    public static Boolean getAutoUpdatePrefs() {
        return getSharedPreferences().getBoolean("auto_update", false);
    }

    /*是否开启黑名单拦截*/
    public static void setBlackInterceptPrefs(boolean value) {
        getSharedPreferences().edit().putBoolean("black_intercept", value).apply();
    }

    public static Boolean getBlackInterceptPrefs() {
        return getSharedPreferences().getBoolean("black_intercept", false);
    }

    /*是否开启归属地显示*/
    public static void setSettingLocationPrefs(Boolean value) {
        getSharedPreferences().edit().putBoolean("setting_location", value).apply();
    }

    public static Boolean getSettingLocationPrefs() {
        return getSharedPreferences().getBoolean("setting_location", false);
    }

    /*是否开启骚扰拦截*/
    public static void setPrivacyPrefs(Boolean value) {
        getSharedPreferences().edit().putBoolean("setting_provacy", value).apply();
    }

    public static Boolean getPrivacyPrefs() {
        return getSharedPreferences().getBoolean("setting_provacy", false);
    }

    /*骚扰拦截密码*/
    public static void setPrivacyPassPrefs(String value) {
        getSharedPreferences().edit().putString("setting_provacy_password", value).apply();
    }

    public static String getPrivacyPassPrefs() {
        return getSharedPreferences().getString("setting_provacy_password", "");
    }


    /**
     * 夜间模式
     *
     * @param isDarkMode true为夜间模式
     */
    public static void setDarkMode(boolean isDarkMode) {
        getSharedPreferences().edit().putBoolean("theme_mode", isDarkMode).apply();
    }

    public static boolean getThemeMode() {
        return getSharedPreferences().getBoolean("theme_mode", false);
    }
}
