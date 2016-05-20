package com.zurich.mobile.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.receiver.DownloadCompleteReceiver;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.widget.GifMovieView;

import java.util.List;

public class GoogleInstallerActivity extends BaseActivity {
    private Activity mActivity;
    private TextView statusTv;
    private TextView operationTv;
    private TextView infoTv;
    private GifMovieView gifView;
    private ImageView imageView;
    private boolean isGoogleOK;
    private Toolbar mToolbar;

    private final int MSG_REPLAY_TIME = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REPLAY_TIME:
                    if (gifView != null) gifView.setVisibility(View.GONE);
                    if (operationTv != null) operationTv.setVisibility(View.VISIBLE);
                    if (statusTv != null) statusTv.setGravity(Gravity.LEFT);
                    if (imageView != null) imageView.setVisibility(View.VISIBLE);
                    if (isGoogleOK) {
                        imageView.setImageResource(R.drawable.google_right);
                        statusTv.setText("谷歌服务框架和市场已安装，快去下载体验精彩游戏吧~");
                        operationTv.setText("确定");
                        operationTv.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });


                    } else {
                        imageView.setImageResource(R.drawable.google_wrong);
                        statusTv.setText("尚未安装‘谷歌服务框架’，部分大型游戏可能出现黑屏闪退的现象，建议使用‘谷歌安装器’一键安装");
                        operationTv.setText("下载谷歌安装器" +
                                "");
                        infoTv.setVisibility(View.VISIBLE);
                        operationTv.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                if (GlobalUtils.haveSDCard()) {
                                    if (GlobalUtils.getSDFreeSize() > 20) {

                                        String fileName = "Google服务" + ".apk";

                                        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://mobile.d.appchina.com/McDonald/r/3690919/com.google.android.gms?channel=aplus.direct&uid=8088cb4d-0c09-473d-80af-b165f51eaae5&page=appplus.detail"));
                                        request.setDestinationInExternalPublicDir("mobile_download", fileName);
                                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                        request.setTitle("Google服务");
                                        request.setDescription("正在下载...");
                                        request.setMimeType("application/com.mobile.download.file");
                                        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE + DownloadManager.Request.NETWORK_WIFI);
                                        // 设置为可被媒体扫描器找到
                                        request.allowScanningByMediaScanner();
                                        // 设置为可见和可管理
                                        request.setVisibleInDownloadsUi(true);
                                        downloadManager.enqueue(request);
                                    }
                                }
                            }
                        });
                    }

                    break;
                default:
                    break;
            }
        }
    };

    private DownloadCompleteReceiver downLoadCompleteReceiver;
    private boolean flag;

    @Override
    protected void onResume() {
        if (!flag){
            registerReceiver(downLoadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            flag = true;
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (downLoadCompleteReceiver != null && flag)
            unregisterReceiver(downLoadCompleteReceiver);
        downLoadCompleteReceiver = null;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_google_installer);

        initActionBar();
        downLoadCompleteReceiver = new DownloadCompleteReceiver();

        setTitle("谷歌检测");
        mActivity = this;

        initView();
        initData();
    }

    private void initActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_google);
        mToolbar.setTitle(getResources().getString(R.string.manage_center_google));
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
                SettingActivity.launch(GoogleInstallerActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        imageView = (ImageView) findViewById(R.id.imageView);
        statusTv = (TextView) findViewById(R.id.statusTv);
        operationTv = (TextView) findViewById(R.id.operationTv);
        infoTv = (TextView) findViewById(R.id.infoTv);
        gifView = (GifMovieView) findViewById(R.id.gifView);

        infoTv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//				GoogleServerGuideActivity.launch(mActivity);
            }
        });
    }

    private void initData() {
        statusTv.setGravity(Gravity.CENTER);
        statusTv.setText("正在进行谷歌检测...");
        operationTv.setVisibility(View.INVISIBLE);
        infoTv.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        gifView.setMovieResource(R.raw.scaning);

        isGoogleOK = isInstalled();
//		isGoogleOK = false;
        mHandler.sendEmptyMessageDelayed(MSG_REPLAY_TIME, 2000);
    }

    public boolean isInstalled() {
        String[] appIds = new String[]{"com.android.vending", "com.google.android.gsf"};
        boolean[] installStatus = new boolean[]{false, false};
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            for (int w = 0; w < appIds.length; w++) {
                String appId = appIds[w];
                if (packageInfo.packageName.equals(appId)) {
                    installStatus[w] = true;
                }
            }
        }
        boolean installed = true;
        for (int w = 0; w < installStatus.length; w++) {
            if (!installStatus[w]) {
                installed = false;
                break;
            }
        }
        return installed;
    }

}
