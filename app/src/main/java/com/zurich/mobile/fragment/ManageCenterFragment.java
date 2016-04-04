package com.zurich.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.activity.GoogleInstallerActivity;
import com.zurich.mobile.activity.SettingActivity;
import com.zurich.mobile.utils.GlobalUtils;

/**
 * Created by weixinfei on 2016/3/20.
 */
public class ManageCenterFragment extends AppBaseFragment implements View.OnClickListener {
    private Context mContext;

    public static final String MANAGE_TYPE_DOWNLOADING = "downloadingList";
    public static final String MANAGE_TYPE_INSTALLED = "installedList";
    public static final String MANAGE_TYPE_AUTO_UPDATE = "autoupdateList";
    private Boolean isIconNeedShow = true;

    private LinearLayout llUpdateIconArea;
    private TextView appUpdateInfo;

    // 应用安装数据相关
    private int mDownloadCount;
    private int mUpdateCount;
    private int mInstalledCount;

    // sdcard and phone
    private boolean mSDCardMounted;
    private boolean hasUpdate;

    private ScrollView scRootView;
    private TextView tvDownloadInfo;
    private TextView tvUnloadInfo;
    private ContentObserver mDownloadChange;
//    private DiskInfoAdapter mDiskInfoAdapter;
    private LinearLayout llDiskInfo;

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
        RelativeLayout rlAppUpdate = (RelativeLayout) findViewById(R.id.rl_update);
        ImageView ivAppUpdateMore = (ImageView) findViewById(R.id.app_update_more);
        llUpdateIconArea = (LinearLayout) findViewById(R.id.ll_update_icon_area);
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
        TextView tvUpdate = (TextView) findViewById(R.id.tv_hongbao);
        tvUpdate.setOnClickListener(this);
        TextView tvSkin = (TextView) findViewById(R.id.tv_skin);
        tvSkin.setOnClickListener(this);
        TextView tvFavorite = (TextView) findViewById(R.id.tv_favorite);
        tvFavorite.setOnClickListener(this);
        TextView tvBackup = (TextView) findViewById(R.id.tv_backup);
        tvBackup.setOnClickListener(this);
        TextView tvMove = (TextView) findViewById(R.id.tv_move);
        tvMove.setOnClickListener(this);
        TextView tvGoogle = (TextView) findViewById(R.id.tv_google);
        tvGoogle.setOnClickListener(this);
        TextView tvScan = (TextView) findViewById(R.id.tv_scan);
        tvScan.setOnClickListener(this);
        TextView tvSetting = (TextView) findViewById(R.id.tv_setting);
        tvSetting.setOnClickListener(this);

        mDownloadChange = new DownloadCountObserver(new Handler());

        //更换皮肤强制刷新一次
        isIconNeedShow = true;
        showUpdateContent();

        //手机状态信息
        llDiskInfo = (LinearLayout)findViewById(R.id.ll_disk_info);
//        mDiskInfoAdapter = new DiskInfoAdapter(mContext, true);
//        initSdcardInfoAdapter();
    }

    @Override
    public boolean isReadyData() {
        return false;
    }

    @Override
    public void onShowData() {
        showUpdateContent();
        refreshMainFunction();
    }

    @Override
    public void onLoadData() {
        showData();
    }

    /**
     * 顶部应用更新内容
     */
    private void showUpdateContent() {
        String str = null;
        SpannableStringBuilder builder = null;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        if (mUpdateCount > 0){
            str = "等" + mUpdateCount + "个应用可更新";
            builder = new SpannableStringBuilder(str);
            builder.setSpan(redSpan, 1, (mUpdateCount + "").length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            appUpdateInfo.setText(builder);
        }else {
            str = "没有可更新的应用";
            appUpdateInfo.setText(str);
            llUpdateIconArea.removeAllViews();
        }

//        ArrayList<String> updateAppIconUrl = getUpdateAppIconUrl();

//        if (updateAppIconUrl != null && isIconNeedShow){
//            llUpdateIconArea.removeAllViews();
//            for (int i = 0; i < 4 && i < updateAppIconUrl.size(); i++){
//                if (updateAppIconUrl.get(i) != null){
//                    RoundedImageView ivIconView = new RoundedImageView(mContext);
//                    ivIconView.setLayoutParams(new LinearLayout.LayoutParams(DeviceUtil.dp2Px(mContext, 22), DeviceUtil.dp2Px(mContext, 22)));
//                    ivIconView.setPadding(DeviceUtil.dp2Px(mContext, 4), 0, 0, 0);
//                    ivIconView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//                    ivIconView.setCornerRadius(DeviceUtil.dp2Px(mContext, 3));
//                    ivIconView.setImageUrl(updateAppIconUrl.get(i), i);
//                    llUpdateIconArea.addView(ivIconView);
//                }
//            }
//            isIconNeedShow = false;
//        }
    }

    /**
     * 加载待更新APP列表Icon
     */
//    private ArrayList<String> getUpdateAppIconUrl() {
//        Cursor cursorUpdate = mContext.getContentResolver().query(
//                LocalAppInfo.LocalAppInfoColumns.CONTENT_URI, null,
//                LocalAppInfo.LocalAppInfoColumns.allUpdadteSelection, null,
//                LocalAppInfo.LocalAppInfoColumns.COLUMN_IS_IGNORED_UPDATE + " ASC"
//        );
//
//        int updateAvailable = mUpdateCount;
//        if (updateAvailable > 0) {
//            ArrayList<String> mIconUrlData = new ArrayList<String>();
//            while (cursorUpdate.moveToNext()) {
//                int iconUrlIndex = cursorUpdate.getColumnIndex(LocalAppInfo.LocalAppInfoColumns.COLUMN_UPDATE_ICON_URL);
//                String iconUrl = cursorUpdate.getString(iconUrlIndex);
//                mIconUrlData.add(iconUrl);
//            }
//            cursorUpdate.close();
//
//            return mIconUrlData;
//        }
//        return null;
//    }

    @Override
    public void onResume() {
        super.onResume();
//        mContext.getContentResolver().registerContentObserver(Downloads.CONTENT_URI, true, mDownloadChange);
//        mContext.getContentResolver().registerContentObserver(LocalAppInfo.LocalAppInfoColumns.CONTENT_URI, true, mDownloadChange);
//        refreshData();
        mSDCardMounted = GlobalUtils.haveSDCard();
    }

    /**
     * 获取下载数据变化，刷新界面
     */
//    private void refreshData() {
//
//        new AsyncTask<Void,Void, Boolean>(){
//            @Override
//            protected Boolean doInBackground(Void... params) {
//                boolean isChanged = getDownloadsCount();
//                isChanged = getUpdateAvailableCount() || isChanged;
//                isChanged = getLocalAppCount() || isChanged;
//                return isChanged;
//            }
//
//            @Override
//            protected void onPostExecute(Boolean isChanged) {
//                if (isChanged){
//                    isIconNeedShow = true;
//                    refreshMainFunction();
//                    showUpdateContent();
//                    initSdcardInfoAdapter();
//                }
//            }
//        }.execute();
//
//    }

    /**
     * 获取下载计数
     *
     * @return，是否改变
     */
//    private boolean getDownloadsCount() {
//        boolean isChanged = false;
//        Cursor cursor = mContext.getContentResolver().query(Downloads.CONTENT_URI, new String[]{Downloads.Impl._ID},
//                Downloads.Impl.COLUMN_STATUS + "<? OR " + Downloads.Impl.COLUMN_STATUS + ">=?",
//                new String[]{String.valueOf(200), String.valueOf(300)}, null);
//        if (cursor != null) {
//            isChanged = !(mDownloadCount == cursor.getCount());
//            mDownloadCount = cursor.getCount();
//            cursor.close();
//            cursor = null;
//        }
//        return isChanged;
//    }

    /**
     * 获取更新计数
     *
     * @return，是否改变
     */
//    private boolean getUpdateAvailableCount() {
//        boolean isChanged = false;
//        if (GlobalUtil.isCheckUpdateFinish(mContext)) {
//            Cursor cursor = mContext.getContentResolver().query(
//                    LocalAppInfo.LocalAppInfoColumns.CONTENT_URI, null,
//                    LocalAppInfo.LocalAppInfoColumns.allUngnoredSelection, null, null);
//            if (cursor != null) {
//                isChanged = !(mUpdateCount == cursor.getCount());
//                mUpdateCount = cursor.getCount();
//                cursor.close();
//                cursor = null;
//            }
//        }
//        return isChanged;
//    }

    /**
     * 获取已安装计数（不包括系统应用）
     *
     * @return，是否改变
     */
//    private boolean getLocalAppCount() {
//        boolean isChanged = false;
//        int newCount = LocalAppInfo.getLocalAppCount(mContext);
//        isChanged = !(mInstalledCount == newCount);
//        mInstalledCount = newCount;
//        return isChanged;
//    }


    private void refreshMainFunction() {
        String str;
        SpannableStringBuilder builder;
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
        str = mDownloadCount + "个" + "应用正在下载";
        builder = new SpannableStringBuilder(str);
        builder.setSpan(redSpan, 0, (mDownloadCount + "个").length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvDownloadInfo.setText(builder);

        str = "已安装" + mInstalledCount + "个" + "软件";
        builder = new SpannableStringBuilder(str);
        builder.setSpan(redSpan, 3, (mInstalledCount + "个").length() + 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvUnloadInfo.setText(builder);
    }

    /**
     * 显示手机状态
     */
//    private void initSdcardInfoAdapter() {
//
//        if (mDiskInfoAdapter != null){
//            mDiskInfoAdapter.notifyDataSetChanged();
//            llDiskInfo.removeAllViews();
//            int count = mDiskInfoAdapter.getCount();
//            for (int i = 0; i < count; i++){
//                llDiskInfo.addView(mDiskInfoAdapter.getView(i, null, llDiskInfo));
//            }
//        }
//    }

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
//            case R.id.rl_apk_clearn://安装包清理
//                PackageManagerActivity.launch(getActivity());
//                // 友盟统计
//                UMengConstant.addUMengLog(mContext, UMengConstant.EVENT_ENTER_APK_MANAGER, null, null);
//                ClientLogger.addActionManageToolClickLog(
//                        mContext, "apk");
//                break;
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
                SettingActivity.launch(mContext, hasUpdate);
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
//            refreshData();
            super.onChange(selfChange);
        }
    }
}
