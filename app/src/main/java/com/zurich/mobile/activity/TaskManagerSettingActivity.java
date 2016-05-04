package com.zurich.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
public class TaskManagerSettingActivity extends FragmentActivity {
    @Bind(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @Bind(R.id.setting_task_show_system)
    SettingLayout settingTaskShowSystem;
    @Bind(R.id.setting_task_auto_kill)
    SettingLayout settingTaskAutoKill;

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
        tvToolbarTitle.setText("进程管理");
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if (settingTaskShowSystem.getCheckBox()){
                    intent.putExtra("is_show_sys", true);
                    setResult(RESULT_OK, intent);
                }else {
                    intent.putExtra("is_show_sys", false);
                    setResult(RESULT_OK, intent);
                }
                onBackPressed();
            }
        });
    }

    private void initView() {
        settingTaskShowSystem.findViewById(R.id.setting_check).setEnabled(false);
        settingTaskAutoKill.findViewById(R.id.setting_check).setEnabled(false);
        settingTaskShowSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingTaskShowSystem.setCheckBox(!settingTaskShowSystem.getCheckBox());
                settingTaskShowSystem.setTvSubName(settingTaskShowSystem.getCheckBox() ? "显示系统进程" : "不显示系统进程");
                SharedPreferenceUtil.setSysTaskVisiblePrefs(getBaseContext(), settingTaskShowSystem.getCheckBox());
            }
        });

        settingTaskAutoKill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (settingTaskAutoKill.getCheckBox()){
                    settingTaskAutoKill.setCheckBox(false);
                    settingTaskAutoKill.setTvSubName("锁屏清理进程没有开启");
                }else {
                    settingTaskAutoKill.setCheckBox(true);
                    Intent intent = new Intent(TaskManagerSettingActivity.this, AutoKillService.class);
                    startService(intent);
                    settingTaskAutoKill.setTvSubName("锁屏清理进程已经开启");
                }
            }
        });
    }

    private void initData() {
        Boolean isShowSysTask = SharedPreferenceUtil.getSysTaskVisiblePrefs(getBaseContext(), false);
        settingTaskShowSystem.setCheckBox(isShowSysTask);
        settingTaskShowSystem.setTvSubName(settingTaskShowSystem.getCheckBox() ? "显示系统进程" : "不显示系统进程");

        boolean result = ServiceStatusUtils.isServiceRunning(this, "com.zurich.mobilesafe.com.zurich.mobile.service.AutoKillService");
        settingTaskAutoKill.setCheckBox(result);
        settingTaskAutoKill.setTvSubName(result ? "锁屏清理进程已经开启" : "锁屏清理进程没有开启");
    }

}
