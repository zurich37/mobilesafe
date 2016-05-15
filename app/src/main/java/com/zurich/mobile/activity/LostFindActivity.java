package com.zurich.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.SharedPreferenceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置安全防盗页面
 * Created by weixinfei on 16/4/19.
 */
public class LostFindActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_lostfind_number)
    TextView tvLostfindNumber;
    @Bind(R.id.iv_lostfind_status)
    ImageView ivLostfindStatus;
    @Bind(R.id.tv_lostfind_reset)
    TextView tvLostfindReset;
    @Bind(R.id.lost_find_tool_bar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_find);
        ButterKnife.bind(this);
        Boolean isConfiged = SharedPreferenceUtil.getLostFindConfigPrefs();
        if (isConfiged) {
            initActionBar();
            boolean protecting = SharedPreferenceUtil.getProtectConfigPrefs();
            if (protecting) {
                ivLostfindStatus.setImageResource(R.drawable.ic_lock_outline);
            } else {
                ivLostfindStatus.setImageResource(R.drawable.ic_lock_open);
            }
            tvLostfindNumber.setText(SharedPreferenceUtil.getSafePhoneNumberPrefs());
        } else {
            Intent intent = new Intent(getBaseContext(), SetupSafeActivty.class);
            startActivity(intent);
            finish();
        }

        configViews();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.safe_lost_find));
        mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.toolbar_back_normal);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    //设置menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.men_action_settings:
                SettingActivity.launch(LostFindActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void configViews() {
        tvLostfindReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_lostfind_reset) {
            Intent intent = new Intent(getBaseContext(), SetupSafeActivty.class);
            startActivity(intent);
        }
    }
}
