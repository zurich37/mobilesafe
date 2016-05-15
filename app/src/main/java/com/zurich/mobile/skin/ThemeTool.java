package com.zurich.mobile.skin;

import android.app.Activity;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.SharedPreferenceUtil;

/**
 * 更换皮肤工具类
 * Created by weixinfei on 16/5/14.
 */
public class ThemeTool {
    public static void changeTheme(Activity activity) {
        if (SharedPreferenceUtil.getThemeMode()) {
            activity.setTheme(R.style.AppThemeDark);
        }
    }
}
