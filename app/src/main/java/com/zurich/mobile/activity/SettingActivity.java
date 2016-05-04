package com.zurich.mobile.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zurich.mobile.R;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.PackageInfoUtil;
import com.zurich.mobile.widget.SettingLayout;

/**
 * 设置页面
 * Created by weixinfei on 2016/3/20.
 */
public class SettingActivity extends AppCompatActivity {
    private Activity mActivity;
    //检查更新
    private BroadcastReceiver mDialogReceiver;
    private final int DIALOG_ID_CHECKING = 10;
    private final int DIALOG_ID_ROOTING = 16;
    private Dialog noUpgradeDialog = null;
    private String mFromPage;
    private int mPushId;
    private String mPushType;
    private int setting_index;
    private static final String SETTING_INDEX = "setting_index";
    private static final int SETTING_LOCATION = 10; // 安装空间选择的锚点
    private static final int SETTING_AUTO_APK_DELETE = 9;// 自动删除安装包的锚点
    private boolean hasUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("设置");

        hasUpdate =getIntent().getBooleanExtra("hasUpdate",false);
        mActivity = SettingActivity.this;
        initView();
        handlePushIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handlePushIntent(intent);
        super.onNewIntent(intent);
    }

    private void handlePushIntent(Intent intent) {
        if (intent == null) {
            return;
        }
    }

    private void initView() {

        SettingLayout settingUpdate = (SettingLayout) findViewById(R.id.setting_update);
        String versionName ="版本："+ PackageInfoUtil.getSelfVersionName(this);
        settingUpdate.setTvSubName(versionName);
//        settingUpdate.setOnClickListener(this);
//        SettingLayout settingInvitation = (SettingLayout) findViewById(R.id.setting_invitation);
//        settingInvitation.setOnClickListener(this);
//        SettingLayout settingAbout = (SettingLayout) findViewById(R.id.setting_about);
//        settingAbout.setOnClickListener(this);
    }


//    @Override
//    public void onClick(View v) {
//        int vId = v.getId();
//        switch (vId) {
//            case R.id.setting_install:
//                setUmengLog("install_setup");
//                SettingInstallActivity.launch(mActivity);
//                break;
//            case R.id.setting_download:
//                setUmengLog("download_setup");
//                SettingDownloadActivity.launch(mActivity);
//                break;
//            case R.id.setting_notify:
//                setUmengLog("notify_setup");
//                SettingNotifyActivity.launch(mActivity);
//                break;
//            case R.id.setting_general:
//                setUmengLog("general_setup");
//                SettingGeneralActivity.launch(mActivity);
//                break;
//            case R.id.setting_flow:
//                setUmengLog("flow_setup");
//                SettingFlowlActivity.launch(mActivity);
//                break;
//            case R.id.setting_update:
//                setUmengLog("check_update");
//                checkUpdate();
//                break;
//            case R.id.setting_invitation:
//                setUmengLog("invitation_install");
//                startActivity(new Intent(this, InviteInstallActivity.class));
//                break;
//            case R.id.setting_about:
//                setUmengLog("about_yyh");
//                AboutActivity.startAboutActivity(this);
//                break;
//        }
//    }

    private void checkUpdate() {
        if (!GlobalUtils.isOnline(mActivity)) {
            GlobalUtils.showToast(mActivity, "网络连接失败");
            return;
        }
        mDialogReceiver = new BroadcastReceiver() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction().equals(
//                        AppChinaConstants.ACTION_POP_UPGRADE)) {
//                    removeDialog(DIALOG_ID_CHECKING);
//                } else if (intent.getAction().equals(
//                        AppChinaConstants.ACTION_NO_UPGRADE)) {
//                    removeDialog(DIALOG_ID_CHECKING);
//                    if (noUpgradeDialog != null) {
//                        noUpgradeDialog.dismiss();
//                    }
//                    AppChinaDialog.Builder builder = new AppChinaDialog.Builder(
//                            mActivity);
//                    builder.setTitle(R.string.warning);
//                    builder.setMessage(getString(R.string.no_upgrade));
//                    builder.setPositiveButton(R.string.ok);
//                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            noUpgradeDialog = null;
//                        }
//                    });
//                    noUpgradeDialog = builder.show();
//                }
//                if (mDialogReceiver != null) {
//                    unregisterReceiver(mDialogReceiver);
//                    mDialogReceiver = null;
//                }
            }
        };
//
//        registerDialogReceiver();
//        sendCheckUpgradeRequest();
    }



//    private void sendCheckUpgradeRequest() {
//        Intent intent = new Intent();
//        intent.setClass(mActivity, CheckUpgradeService.class);
//        intent.putExtra(CheckUpgradeService.EXTRA_FIRST_LAUNCH_AFTER_INSTALL,
//                false);
//        intent.putExtra(CheckUpgradeService.EXTRA_MANUL_CHECK, true);
//        startService(intent);
//    }
//
//    private void registerDialogReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(AppChinaConstants.ACTION_POP_UPGRADE);
//        filter.addAction(AppChinaConstants.ACTION_NO_UPGRADE);
//        registerReceiver(mDialogReceiver, filter);
//    }

    public static void launch(Context activity) {
        //activity.startActivity(new Intent(activity, SettingActivity.class));
        activity.startActivity(new Intent(activity, SettingActivity.class));
    }

}
