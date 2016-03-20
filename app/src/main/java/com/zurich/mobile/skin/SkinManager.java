package com.zurich.mobile.skin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * 皮肤管理器，用来更改皮肤和获取所有皮肤
 */
public class SkinManager {
    private static final String KEY_SKIN = "PREF_KEY_SKIN";
    private static final String KEY_USER_CUSTOM_PRIMARY_COLOR = "PREF_KEY_COLOR_PRIMARY";
    private static SkinManager instance;

    private SharedPreferences preferences;
    private Skin currentSkin;

    public static SkinManager with(Context context) {
        if (instance == null) {
            synchronized (SkinManager.class) {
                if (instance == null) {
                    instance = new SkinManager(context);
                }
            }
        }
        return instance;
    }

    private SkinManager(Context context) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);

        String currentSkinName = preferences.getString(KEY_SKIN, Skin.LU_CAO.name());

        // 兼容旧版，支持解析LIGHT、DARK、DEFAULT，对应新版不同主题
        if ("LIGHT".equals(currentSkinName) || "DARK".equals(currentSkinName)) {
            currentSkin = Skin.USER_CUSTOM;
        } else if ("DEFAULT".equals(currentSkinName)) {
            currentSkin = Skin.LU_CAO;
        } else {
            try {
                currentSkin = Skin.valueOf(currentSkinName);
            } catch (Exception e) {
                e.printStackTrace();
                currentSkin = Skin.LU_CAO;
            }
        }

        int userCustomColorPrimary = preferences.getInt(KEY_USER_CUSTOM_PRIMARY_COLOR, -1);
        if (userCustomColorPrimary != -1) {
            Skin.USER_CUSTOM.setPrimaryColor(userCustomColorPrimary);
        }
    }

    public void setSkin(Skin newSkin) {
        if (newSkin != null) {
            currentSkin = newSkin;
            preferences.edit().putString(KEY_SKIN, currentSkin.name()).commit();
        }
    }

    public Skin getSkin() {
        return currentSkin;
    }

    public void setUserCustomPrimaryColor(int userCustomPrimaryColor){
        preferences.edit().putInt(KEY_USER_CUSTOM_PRIMARY_COLOR, userCustomPrimaryColor).commit();
        Skin.USER_CUSTOM.setPrimaryColor(userCustomPrimaryColor);
    }
}
