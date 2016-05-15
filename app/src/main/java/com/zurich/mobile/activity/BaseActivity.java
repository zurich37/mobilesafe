package com.zurich.mobile.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zurich.mobile.skin.ThemeTool;

/**
 * 父Activity
 * Created by weixinfei on 16/5/14.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // 创建状态栏的管理实例
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        // 激活状态栏设置
        tintManager.setStatusBarTintEnabled(true);
        // 激活导航栏设置
        tintManager.setNavigationBarTintEnabled(true);
        // 设置一个颜色给系统栏
        if (SharedPreferenceUtil.getThemeMode()) {
            tintManager.setTintColor(getResources().getColor(R.color.colorPrimaryDarkDarkTheme));
        } else {
            tintManager.setTintColor(getResources().getColor(R.color.colorPrimary));
        }*/

        ThemeTool.changeTheme(this);
    }
}
