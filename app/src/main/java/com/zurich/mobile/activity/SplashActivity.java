package com.zurich.mobile.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.PackageInfoUtil;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.view.kbv.KenBurnsView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weixinfei .
 */
public class SplashActivity extends FragmentActivity {

    private String SPLASH_SCREEN_OPTION_1 = "Fade in + Ken Burns";

    private Activity mActivity;
    private Context mContext;
    //params
    private String currentVersionName;
    private String versionFromServer;

    private Intent mainIntent;
    private long downloadId;
    private DownLoadCompleteReceiver downLoadCompleteReceiver;

    //views
    private TextView mLogo;
    private KenBurnsView mKenBurns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mActivity = SplashActivity.this;
        mContext = getBaseContext();

        mainIntent = new Intent();
        mainIntent.setAction(Account.STTART_MAIN_PAGE);

        String localVersion = SharedPreferenceUtil.getSettingPrefs(this, SharedPreferenceUtil.SETTING_PREFS_NAME, "0");

        currentVersionName = PackageInfoUtil.getSelfVersionName(this);

        initView();

        if (localVersion.equals("0")) {
            checkUpdate();
        }

        downLoadCompleteReceiver = new DownLoadCompleteReceiver();
        registerReceiver(downLoadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void initView() {
        mKenBurns = (KenBurnsView) findViewById(R.id.kenBurnsView);
        TextView tv_version = (TextView) findViewById(R.id.tv_wec);
        tv_version.setText("精简极致体验  v" + currentVersionName);
        mLogo = (TextView) findViewById(R.id.splsh_logo);

        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        findViewById(R.id.rl_splash_root).startAnimation(aa);

        setAnimation(SPLASH_SCREEN_OPTION_1);
    }

    private void animation1() {
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

    public void setAnimation(String animation) {
        mKenBurns.setImageResource(R.drawable.splash_background);
        animation1();
    }

    /**
     * 检查版本更新
     */
    private void checkUpdate() {

        String updUrl = getResources().getString(R.string.url_server);

        JsonObjectRequest updRequest = new JsonObjectRequest(Request.Method.GET, updUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    versionFromServer = response.getString("version");
                    if (currentVersionName.equals(versionFromServer)) {
                        SharedPreferenceUtil.setSettingPrefs(getBaseContext(), SharedPreferenceUtil.PACKAGE_INFOS_VERSION_NAME, currentVersionName);
                        startActivity(mainIntent);
                        SplashActivity.this.finish();
                    } else {
                        Dialog.Builder builder = null;
                        String description = response.getString("description");
                        final String apkUrl = response.getString("apkurl");
                        builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
                            @Override
                            public void onPositiveActionClicked(DialogFragment fragment) {
                                //开始升级
                                startUpdate(apkUrl);
                                super.onPositiveActionClicked(fragment);
                            }

                            @Override
                            public void onNegativeActionClicked(DialogFragment fragment) {
                                startActivity(mainIntent);
                                super.onNegativeActionClicked(fragment);
                                SplashActivity.this.finish();
                            }
                        };

                        ((SimpleDialog.Builder) builder).message(description)
                                .title("新版本发布，点击立即更新")
                                .positiveAction("立即更新")
                                .negativeAction("下次再说");
                        DialogFragment fragment = DialogFragment.newInstance(builder);
                        fragment.show(getSupportFragmentManager(), null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                GlobalUtils.showToast(mContext, getResources().getString(R.string.net_wrong));
                startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(updRequest);
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
                request.setDestinationInExternalFilesDir(mContext, null, fileName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setTitle("安全卫士");
                request.setDescription("正在下载...");
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE + DownloadManager.Request.NETWORK_WIFI);
                // 设置为可被媒体扫描器找到
                request.allowScanningByMediaScanner();
                // 设置为可见和可管理
                request.setVisibleInDownloadsUi(true);
                downloadId = downloadManager.enqueue(request);
            }
        }
    }

    public class DownLoadCompleteReceiver extends BroadcastReceiver {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onReceive(Context context, Intent intent) {
            long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId == myDwonloadID) {

                String serviceString = Context.DOWNLOAD_SERVICE;

                DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);

                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);

                SplashActivity.this.finish();
            }
        }
    }

    @Override
    protected void onStop() {
        unregisterReceiver(downLoadCompleteReceiver);
        super.onStop();
    }
}
