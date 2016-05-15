package com.zurich.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zurich.mobile.R;
import com.zurich.mobile.service.AutoKillService;
import com.zurich.mobile.utils.ServiceStatusUtils;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.widget.SettingLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 进程管理设置
 * Created by weixinfei on 16/5/3.
 */
public class TaskManagerSettingActivity extends BaseActivity {
    @Bind(R.id.setting_task_show_system)
    SettingLayout settingTaskShowSystem;
    @Bind(R.id.setting_task_auto_kill)
    SettingLayout settingTaskAutoKill;
    @Bind(R.id.toolbar_task_setting)
    Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmanager_setting);
        ButterKnife.bind(this);

        initActionBar();

        initView();

        initData();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.safe_task_manager));
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
                Intent intent = new Intent();
                if (settingTaskShowSystem.getCheckBox()) {
                    intent.putExtra("is_show_sys", true);
                    setResult(RESULT_OK, intent);
                } else {
                    intent.putExtra("is_show_sys", false);
                    setResult(RESULT_OK, intent);
                }
                onBackPressed();
                return true;
            case R.id.men_action_settings:
                SettingActivity.launch(TaskManagerSettingActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        settingTaskShowSystem.findViewById(R.id.setting_check).setEnabled(false);
        settingTaskAutoKill.findViewById(R.id.setting_check).setEnabled(false);
        settingTaskShowSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingTaskShowSystem.setCheckBox(!settingTaskShowSystem.getCheckBox());
                settingTaskShowSystem.setTvSubName(settingTaskShowSystem.getCheckBox() ? "显示系统进程" : "不显示系统进程");
                SharedPreferenceUtil.setSysTaskVisiblePrefs(settingTaskShowSystem.getCheckBox());
            }
        });

        settingTaskAutoKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingTaskAutoKill.getCheckBox()) {
                    settingTaskAutoKill.setCheckBox(false);
                    settingTaskAutoKill.setTvSubName("锁屏清理进程没有开启");
                } else {
                    settingTaskAutoKill.setCheckBox(true);
                    Intent intent = new Intent(TaskManagerSettingActivity.this, AutoKillService.class);
                    startService(intent);
                    settingTaskAutoKill.setTvSubName("锁屏清理进程已经开启");
                }
            }
        });
    }

    private void initData() {
        Boolean isShowSysTask = SharedPreferenceUtil.getSysTaskVisiblePrefs();
        settingTaskShowSystem.setCheckBox(isShowSysTask);
        settingTaskShowSystem.setTvSubName(settingTaskShowSystem.getCheckBox() ? "显示系统进程" : "不显示系统进程");

        boolean result = ServiceStatusUtils.isServiceRunning(this, "com.zurich.mobilesafe.com.zurich.mobile.service.AutoKillService");
        settingTaskAutoKill.setCheckBox(result);
        settingTaskAutoKill.setTvSubName(result ? "锁屏清理进程已经开启" : "锁屏清理进程没有开启");
    }

}
