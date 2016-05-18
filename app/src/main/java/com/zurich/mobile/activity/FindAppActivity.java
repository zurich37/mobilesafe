package com.zurich.mobile.activity;

import android.app.DownloadManager;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.AppItemFactory;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.model.Asset;
import com.zurich.mobile.receiver.DownloadCompleteReceiver;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.widget.HintView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 淘应用
 * Created by weixinfei on 16/5/15.
 */
public class FindAppActivity extends BaseActivity implements AppItemFactory.AppItemClickEvent{
    @Bind(R.id.toolbar_find_app)
    Toolbar mToolbar;
    @Bind(R.id.recycler_find_app)
    RecyclerView recyclerView;
    @Bind(R.id.hint_find_app)
    HintView hintView;
    private RequestQueue mQueue;
    private List<Asset> assets;
    private AssemblyRecyclerAdapter mAdapter;
    private long downloadId;
    private DownloadCompleteReceiver downLoadCompleteReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_app);
        ButterKnife.bind(this);

        mQueue = Volley.newRequestQueue(this);
        assets = new ArrayList<Asset>();

        downLoadCompleteReceiver = new DownloadCompleteReceiver();

        initActionBar();

        initView();

        loadData();

        showData();
    }

    private void registerDownloadReceiver() {
        registerReceiver(downLoadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 网络请求获取数据
     */
    private void loadData() {
        hintView.loading().show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("apiVer", 17);
            jsonObject.put("guid", "f68e8336-2b68-4a67-9bc1-1efcba719b2c");
            jsonObject.put("imei", "000000000000000");
            jsonObject.put("type", "showlist");
            jsonObject.put("forCache", false);
            jsonObject.put("imgType", 128);
            jsonObject.put("gpuType", 4);
            jsonObject.put("imglevel", 2);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("channel", "ac.publish.yyh");
            jsonObject1.put("distinctId", 11034);
            jsonObject1.put("showPlace", "rank");
            jsonObject1.put("size", 20);
            jsonObject1.put("indexStart", 0);
            jsonObject1.put("version", 1);

            jsonObject.put("params", jsonObject1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://10.19.3.184:8080/hot_app.json", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parseJson(response);
                        initAdapter();
                        Log.d("TAG", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mQueue.add(jsonObjectRequest);
    }

    private void initAdapter() {
        if (mAdapter == null){
            mAdapter = new AssemblyRecyclerAdapter(assets);
            mAdapter.addItemFactory(new AppItemFactory(this));
            recyclerView.setAdapter(mAdapter);
        }
    }

    private void refreshAdapter(){
        if (mAdapter != null){
            mAdapter.notifyDataSetChanged();
        }
    }

    private void showData(){
        hintView.hidden();
        refreshAdapter();
    }

    private void parseJson(JSONObject response) {
        if (response != null){
            JSONArray list = response.optJSONArray("list");
            for (int i = 0; i < list.length(); i++){
                try {
                    Asset asset = new Asset();
                    JSONObject item = (JSONObject) list.get(i);
                    asset.id = item.optInt("id");
                    JSONObject appinfo = item.optJSONObject("showProps").optJSONObject("appinfo");
                    asset.apkUrl = appinfo.optString("apkUrl");
                    asset.iconUrl = appinfo.optString("iconUrl");
                    asset.appName = appinfo.optString("name");
                    asset.appDescription = appinfo.optString("shorDesc");
                    assets.add(asset);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.manage_center_find_app));
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
                SettingActivity.launch(FindAppActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAppClick(Asset asset) {
        startDownload(asset);
    }

    private void startDownload(Asset asset) {
        if (GlobalUtils.haveSDCard()) {
            if (GlobalUtils.getSDFreeSize() > 20) {

                DownloadManager downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(asset.apkUrl));
                request.setDestinationInExternalPublicDir("mobile_download", asset.appName+".apk");
                request.setDescription("正在下载...");
                request.setTitle(asset.appName);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                request.setMimeType("application/com.mobile.download.file");
                downloadId = downloadManager.enqueue(request);

                // 设置为可被媒体扫描器找到
                request.allowScanningByMediaScanner();
                // 设置为可见和可管理
                request.setVisibleInDownloadsUi(true);
            }
        }
    }

    @Override
    protected void onResume() {
        registerDownloadReceiver();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterDownloadReceiver();
    }

    private void unRegisterDownloadReceiver() {
        if (downLoadCompleteReceiver != null){
            unregisterReceiver(downLoadCompleteReceiver);
        }
    }
}
