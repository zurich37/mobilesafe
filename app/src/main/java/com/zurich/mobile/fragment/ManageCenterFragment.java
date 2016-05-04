package com.zurich.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.activity.GoogleInstallerActivity;
import com.zurich.mobile.activity.PackageClearActivity;
import com.zurich.mobile.activity.SettingActivity;

/**
 * 管理中心
 * Created by weixinfei on 2016/3/20.
 */
public class ManageCenterFragment extends AppBaseFragment implements View.OnClickListener {
    private Context mContext;

    private LinearLayout llUpdateIconArea;
    private RelativeLayout rlCallAccess;
    private TextView appUpdateInfo;

    private ScrollView scRootView;
    private TextView tvDownloadInfo;
    private TextView tvUnloadInfo;
    private ContentObserver mDownloadChange;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDisableDelay(true);
        mContext = getContext();
    }

    @Override
    public int getContentViewLayoutId() {
        return R.layout.fragment_manage_center;
    }

    @Override
    public void onInitViews(View view, Bundle savedInstanceState) {

        scRootView = (ScrollView) findViewById(R.id.sc_root_view);
        RelativeLayout rlAppUpdate = (RelativeLayout) findViewById(R.id.rl_call_access);
        rlCallAccess = (RelativeLayout) findViewById(R.id.rl_call_access);
        appUpdateInfo = (TextView) findViewById(R.id.app_update_info);

        RelativeLayout rlDownload = (RelativeLayout) findViewById(R.id.rl_download);
        RelativeLayout rlUnload = (RelativeLayout) findViewById(R.id.rl_unload);
        RelativeLayout rlApkClearn = (RelativeLayout) findViewById(R.id.rl_apk_clearn);
        RelativeLayout rlFastPass = (RelativeLayout) findViewById(R.id.rl_fast_pass);

        //下载管理
        tvDownloadInfo = (TextView) findViewById(R.id.tv_download_info);

        //软件卸载
        tvUnloadInfo = (TextView) findViewById(R.id.tv_unload_info);

        rlDownload.setOnClickListener(this);
        rlUnload.setOnClickListener(this);
        rlApkClearn.setOnClickListener(this);
        rlFastPass.setOnClickListener(this);
        rlAppUpdate.setOnClickListener(this);

        //8个普通功能
        TextView tvFavorite = (TextView) findViewById(R.id.tv_favorite);
        tvFavorite.setOnClickListener(this);
        TextView tvBackup = (TextView) findViewById(R.id.tv_backup);
        tvBackup.setOnClickListener(this);
        TextView tvGoogle = (TextView) findViewById(R.id.tv_google);
        tvGoogle.setOnClickListener(this);
        TextView tvSetting = (TextView) findViewById(R.id.tv_setting);
        tvSetting.setOnClickListener(this);

        mDownloadChange = new DownloadCountObserver(new Handler());

    }

    @Override
    public boolean isReadyData() {
        return false;
    }

    @Override
    public void onShowData() {
    }

    @Override
    public void onLoadData() {
        showData();
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
//        Intent intent;
        switch (v.getId()){
//            case R.id.rl_update://可更新
//                intent = new Intent(mContext, AppUpdateActivity.class);
//                startActivity(intent);
//                UMengConstant.addUMengLog(mContext, UMengConstant.EVENT_ENTER_UPDATE_MANAGER, null, null);
//                ClientLogger.addActionManageToolClickLog(mContext, "update");
//                break;
//            case R.id.rl_download://下载管理
//                intent = new Intent(mContext, DownloadHistoryActivity.class);
//                intent.putExtra(AppChinaConstants.EXTRA_LIST_TYPE, MANAGE_TYPE_DOWNLOADING);
//                intent.putExtra(AppChinaConstants.EXTRA_FROM_PAGE, AppChinaConstants.MANANGE_PAGE);
//                startActivity(intent);
//                SmartLogger.beginTransaction().umeng(UMengConstant.EVENT_ENTER_DOWNLOAD_MANAGER)
//                        .addUmengParam(UMengConstant.EVENT_ENTER_DOWNLOAD_MANAGER, "管理中心")
//                        .commit(mContext);
//                ClientLogger.addActionManageToolClickLog(mContext, "download");
//                break;
//            case R.id.rl_unload://软件卸载
//                intent = new Intent(mContext, AppManageActivity.class);
//                intent.putExtra(AppChinaConstants.EXTRA_LIST_TYPE, MANAGE_TYPE_INSTALLED);
//                startActivity(intent);
//
//                // 友盟统计
//                UMengConstant.addUMengLog(mContext, UMengConstant.EVENT_ENTER_UNINSTALL_MANAGER, null, null);
//                ClientLogger.addActionManageToolClickLog(mContext, "uninstall");
//                break;
            case R.id.rl_apk_clearn://安装包清理
                startActivity(new Intent(getContext(), PackageClearActivity.class));
                break;
                //            case R.id.rl_fast_pass://免流量快传
//                GlobalUtil.startFastPass(mContext);
//                SmartLogger.beginTransaction().
//                        umeng(UMengConstant.WAYS_FOR_SHARE).
//                        addUmengParam(UMengConstant.MENU_SHARE_CLICK, UMengConstant.MENU_SHARE_CLICK).
//                        commit(mContext);
//                break;
//            case R.id.tv_hongbao://抢红包
//                HongBaoActivity.launchForHongbao(mContext, 0, "manageTools", 0, -1);
//                PreferenceUtil.putBoolean(mContext, "qianghongbao_show_new_flag", false);
//                SmartLogger.beginTransaction().umeng(UMengConstant.EVENT_QIANGHONGBAO)
//                        .addUmengParam(UMengConstant.EVENT_QIANGHONGBAO, "enter_manager")
//                        .commit(mContext);
//                ClientLogger.addActionManageToolClickLog(mContext, "qianghongbao");
//                break;
//            case R.id.tv_skin://更换皮肤
//                startActivity(new Intent(mContext, SkinManageActivity.class));
//                UMengConstant.addUMengLog(mContext, UMengConstant.EVENT_ENTER_SKIN_MANAGE, null, null);
//                ClientLogger.addActionManageToolClickLog(mContext, "skin");
//                break;
//            case R.id.tv_favorite://云收藏
//                if (!Account.getInstance(mContext).isAccountLogin()) {
//                    LoginActivity.launchForResult(getActivity(), 0, AppChinaConstants.ACTIVITY_MANAGER_TOOLS);
//                    break;
//                }
//
//                intent = new Intent(mContext, AppFavoritesListActivity.class);
//                startActivity(intent);
//
//                UMengConstant.addUMengLog(mContext, UMengConstant.EVENT_ENTER_FAVORITE_MANAGER, null, null);
//
//                ClientLogger.addActionManageToolClickLog(mContext, "favorite");
//                break;
//            case R.id.tv_backup://备份
//                if (mSDCardMounted) {
//                    intent = new Intent(mContext, AppBackupListActivity.class);
//                    int mBackupedCount = 0;
//                    intent.putExtra(AppBackupListActivity.EXTRA_BACKUPED_COUNT, mBackupedCount);
//                    startActivity(intent);
//
//                    UMengConstant.addUMengLog(mContext, UMengConstant.EVENT_ENTER_BACKUP_MANAGER, null, null);
//
//                } else {
//                    GlobalUtil.showToast(mContext, R.string.sd_unmounted);
//                }
//
//                ClientLogger.addActionManageToolClickLog(mContext, "backup");
//                break;
//            case R.id.tv_move://应用搬家
//                if (mSDCardMounted) {
//                    intent = new Intent(mContext, SoftwareRemovalActivity.class);
//                    startActivity(intent);
//
//                    UMengConstant.addUMengLog(mContext, UMengConstant.EVENT_ENTER_REMOVAL_MANAGER, null, null);
//
//                } else {
//                    GlobalUtil.showToast(mContext, R.string.sd_unmounted);
//                }
//
//                ClientLogger.addActionManageToolClickLog(mContext, "removal");
//                break;
            case R.id.tv_google://谷歌检测
                startActivity(new Intent(mContext, GoogleInstallerActivity.class));
                break;
//            case R.id.tv_scan://扫一扫
//                Runnable task = new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            System.gc();
//                            Thread.sleep(500);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        Intent QrIntent = new Intent(getActivity(), CaptureActivity.class);
//                        startActivity(QrIntent);
//                        UMengConstant.addUMengLog(getActivity(), UMengConstant.EVENT_ENTER_QR, null, null);
//                    }
//                };
//                CommonThreadPoolFactory.getDefaultExecutor().submit(task);
//                break;
            case R.id.tv_setting://设置
                SettingActivity.launch(mContext);
                break;
//            default:
//                break;
        }
    }

    @Override
    public void onPause() {
        mContext.getContentResolver().unregisterContentObserver(mDownloadChange);
        super.onPause();
    }

    private class DownloadCountObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public DownloadCountObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
        }
    }
}
