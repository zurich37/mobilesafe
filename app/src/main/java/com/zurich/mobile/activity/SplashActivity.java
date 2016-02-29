package com.zurich.mobile.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.app.ThemeManager;
import com.zurich.mobile.R;
import com.zurich.mobile.net.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by weixi_000 .
 */
public class SplashActivity extends FragmentActivity{

    private SplashActivity mActivity;

    private TextView tv_version;
    private String version;

    public static final String TAG_JSON_REQUEST = "update_check";
    public static final String UPD_URL = "http://192.168.106.2:8080/updateinfo.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mActivity = SplashActivity.this;

        version = getAppVersion();
        tv_version = (TextView) findViewById(R.id.tv_wec);
        tv_version.setText("精简极致体验  v" + version);

        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        checkUpdate();
    }

    /**
     * 检查版本更新
     */
    private void checkUpdate() {



        JsonObjectRequest updRequest = new JsonObjectRequest(Request.Method.GET, UPD_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (version.equals(response.getString("version"))){
                        Intent intent = new Intent();
                        intent.setAction("MainActivity");
                        startActivity(intent);
                    }else {
                        Dialog.Builder builder = null;
                        boolean isLightTheme = ThemeManager.getInstance().getCurrentTheme() == 0;
                        String description = response.getString("description");
                        final String apkUrl = response.getString("apkurl");
                        builder = new SimpleDialog.Builder(isLightTheme ? R.style.SimpleDialogLight : R.style.SimpleDialog){
                            @Override
                            public void onPositiveActionClicked(DialogFragment fragment) {

                                startUpdate(apkUrl);
                                Toast.makeText(mActivity, "Agreed", Toast.LENGTH_SHORT).show();
                                super.onPositiveActionClicked(fragment);
                            }

                            @Override
                            public void onNegativeActionClicked(DialogFragment fragment) {
                                Toast.makeText(mActivity, "Disagreed", Toast.LENGTH_SHORT).show();
                                super.onNegativeActionClicked(fragment);
                            }
                        };

                        ((SimpleDialog.Builder)builder).message(description)
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
                Toast.makeText(getBaseContext(), "升级失败，请检查网络！", Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(this).addToRequestQueue(updRequest);
    }

    /**
     * 开始更新，下载apk包
     * @param apkUrl
     */
    private void startUpdate(String apkUrl) {

    }

    /**
     * 得到应用的版本信息
     * @return version
     */
    public String getAppVersion() {

        PackageManager pm = getPackageManager();

        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
