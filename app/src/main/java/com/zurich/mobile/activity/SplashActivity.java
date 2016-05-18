package com.zurich.mobile.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.zurich.mobile.Account;
import com.zurich.mobile.R;
import com.zurich.mobile.net.MySingleton;
import com.zurich.mobile.receiver.DownloadCompleteReceiver;
import com.zurich.mobile.service.AddressService;
import com.zurich.mobile.service.CallSmsSafeService;
import com.zurich.mobile.service.PrivacyService;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.PackageInfoUtil;
import com.zurich.mobile.utils.ServiceStatusUtils;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.view.kbv.KenBurnsView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 启动页
 * Created by weixinfei .
 */
public class SplashActivity extends FragmentActivity {

    protected static final String TAG = "SplashActivity";

    private Activity mActivity;
    private Context mContext;
    //params
    private String currentVersionName;
    private String versionFromServer;

    private Intent mainIntent;
    private long downloadId;
    private DownloadCompleteReceiver downLoadCompleteReceiver;

    //views
    private TextView mLogo;
    private KenBurnsView mKenBurns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mActivity = SplashActivity.this;
        mContext = getBaseContext();

        mainIntent = new Intent();
        mainIntent.setAction(Account.STTART_MAIN_PAGE);

        String localVersion = SharedPreferenceUtil.getLocalVersionPrefs();

        currentVersionName = PackageInfoUtil.getSelfVersionName(this);

        initView();

        initDbSource();

        checkCallSmsService();
        checkApplockService();

        if (localVersion.equals("") && SharedPreferenceUtil.getAutoUpdatePrefs()) {
            checkUpdate();
        } else {
            enterHomePage();
        }

        downLoadCompleteReceiver = new DownloadCompleteReceiver();
    }

    @Override
    protected void onResume() {
        registerReceiver(downLoadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        super.onResume();
    }

    private void checkApplockService() {
        Boolean isOpenPrivacy = SharedPreferenceUtil.getPrivacyPrefs();
        if (isOpenPrivacy) {
            Intent intent = new Intent(this, PrivacyService.class);
            if (ServiceStatusUtils.isServiceRunning(this, "com.zurich.service.PrivacyService")) {
                return;
            } else {
                startService(intent);
            }
        }
    }

    private void checkCallSmsService() {
        Boolean isOpenIntercept = SharedPreferenceUtil.getBlackInterceptPrefs();
        Boolean isAddressOpen = SharedPreferenceUtil.getSettingLocationPrefs();
        if (isOpenIntercept) {
            Intent callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
            if (ServiceStatusUtils.isServiceRunning(this, "com.zurich.service.CallSmsSafeService")) {
                return;
            } else {
                startService(callSmsSafeIntent);
            }
        }
        if (isAddressOpen) {
            Intent addressIntent = new Intent(this, AddressService.class);
            if (ServiceStatusUtils.isServiceRunning(this, "com.zurich.service.AddressService")) {
                return;
            } else {
                startService(addressIntent);
            }
        }
    }

    private void initDbSource() {
        // 把asset下的数据库 拷贝到系统的目录里面
        copyDB("address.db");
        copyDB("commonnum.db");
        copyDB("antivirus.db");
    }

    private void copyDB(String dbfilename) {
        /**
         * 拷贝资产目录下的数据库文件
         */
        File file = new File(getFilesDir(), dbfilename);
        if (file.exists() && file.length() > 0) {
            Log.i(TAG, "数据库文件已经拷贝过了，无需重复拷贝");
        } else {
            try {
                // 数据库文件只需要拷贝一次，如果已经拷贝成功了。以后就不需要重复的拷贝了
                AssetManager am = getAssets();
                InputStream is = am.open(dbfilename);
                // 创建一个文件 /data/data/包名/files/address.db
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        mKenBurns = (KenBurnsView) findViewById(R.id.kenBurnsView);
        TextView tv_version = (TextView) findViewById(R.id.tv_wec);
        tv_version.setText("精简极致体验  v" + currentVersionName);
        mLogo = (TextView) findViewById(R.id.splsh_logo);

        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        findViewById(R.id.rl_splash_root).startAnimation(aa);

        setAnimation();
    }

    private void setAnimation() {
        mKenBurns.setImageResource(R.drawable.splash_background);
        ObjectAnimator var1 = ObjectAnimator.ofFloat(this.mLogo, "scaleX", new float[]{5.0F, 1.0F});
        var1.setInterpolator(new AccelerateDecelerateInterpolator());
        var1.setDuration(1200L);
        ObjectAnimator var2 = ObjectAnimator.ofFloat(this.mLogo, "scaleY", new float[]{5.0F, 1.0F});
        var2.setInterpolator(new AccelerateDecelerateInterpolator());
        var2.setDuration(1200L);
        ObjectAnimator var3 = ObjectAnimator.ofFloat(this.mLogo, "alpha", new float[]{0.0F, 1.0F});
        var3.setInterpolator(new AccelerateDecelerateInterpolator());
        var3.setDuration(1200L);
        AnimatorSet var4 = new AnimatorSet();
        var4.play(var1).with(var2).with(var3);
        var4.setStartDelay(1000L);
        var4.start();
    }

    /**
     * 检查版本更新
     */
    private void checkUpdate() {

        String updUrl = getResources().getString(R.string.url_server);

        JsonObjectRequest updRequest = new JsonObjectRequest(Request.Method.GET, updUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                versionFromServer = response.optString("version");
                if (currentVersionName.equals(versionFromServer)) {
                    SharedPreferenceUtil.setLocalVersionPrefs(currentVersionName);
                    enterHomePage();
                } else {
                    Dialog.Builder builder = null;
                    String description = response.optString("description");
                    final String apkUrl = response.optString("apkurl");
                    builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                        @Override
                        public void onPositiveActionClicked(DialogFragment fragment) {
                            //开始升级
                            startUpdate(apkUrl);
                            super.onPositiveActionClicked(fragment);
                        }

                        @Override
                        public void onNegativeActionClicked(DialogFragment fragment) {
                            super.onNegativeActionClicked(fragment);
                            enterHomePage();
                        }
                    };

                    ((SimpleDialog.Builder) builder).message(description)
                            .title("新版本发布，点击立即更新")
                            .positiveAction("立即更新")
                            .negativeAction("下次再说");
                    DialogFragment fragment = DialogFragment.newInstance(builder);
                    fragment.show(getSupportFragmentManager(), null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalUtils.showToast(mContext, getResources().getString(R.string.update_fail));
                enterHomePage();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(updRequest);
    }

    private void enterHomePage() {
        startActivity(mainIntent);
        SplashActivity.this.finish();
    }

    /**
     * 开始更新，下载apk包
     *
     * @param apkUrl
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void startUpdate(final String apkUrl) {
        if (GlobalUtils.haveSDCard()) {
            if (GlobalUtils.getSDFreeSize() > 20) {

                String fileName = "mobile" + versionFromServer + ".apk";

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
                request.setDestinationInExternalPublicDir("mobile_download", fileName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setTitle("安全卫士");
                request.setDescription("正在下载...");
                request.setMimeType("application/com.mobile.download.file");
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE + DownloadManager.Request.NETWORK_WIFI);
                // 设置为可被媒体扫描器找到
                request.allowScanningByMediaScanner();
                // 设置为可见和可管理
                request.setVisibleInDownloadsUi(true);
                downloadId = downloadManager.enqueue(request);
            }
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(downLoadCompleteReceiver);
        downLoadCompleteReceiver = null;
        super.onStop();
    }

}
