package com.zurich.mobile.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.zurich.mobile.R;
import com.zurich.mobile.fragment.SettingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置页面
 * Created by weixinfei on 2016/3/20.
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.toolbar_preference)
    Toolbar mToolbar;
    @Bind(R.id.content_frame)
    FrameLayout contentFrame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        initToolbar();

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingFragment()).commit();
    }

    private void initToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.title_activity_setting));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_back_normal);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
