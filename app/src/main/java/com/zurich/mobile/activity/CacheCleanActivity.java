package com.zurich.mobile.activity;

import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zurich.mobile.R;

import java.lang.reflect.Method;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 缓存清理
 * Created by weixinfei on 16/5/18.
 */
public class CacheCleanActivity extends BaseActivity {

    @Bind(R.id.toolbar_clean_cache)
    Toolbar mToolbar;
    @Bind(R.id.pb_cache_clean)
    ProgressBar pbCacheClean;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;

    protected static final int SCANING = 1;
    public static final int SHOW_CACHE_INFO = 2;
    protected static final int SCAN_FINISH = 3;
    @Bind(R.id.fab_clean_cache)
    FloatingActionButton fabCleanCache;

    private PackageManager pm;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);
        ButterKnife.bind(this);

        initActionBar();

        initHandler();

        initView();

        new Thread() {
            public void run() {
                pm = getPackageManager();
                List<PackageInfo> packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
                pbCacheClean.setMax(packageInfos.size());
                int total = 0;
                for (PackageInfo packinfo : packageInfos) {
                    try {
                        String packname = packinfo.packageName;
                        Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
                        method.invoke(pm, packname, new MyObserver());
                        Message msg = Message.obtain();
                        msg.what = SCANING;
                        msg.obj = packinfo.applicationInfo.loadLabel(pm).toString();
                        mHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    total++;
                    pbCacheClean.setProgress(total);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = Message.obtain();
                msg.what = SCAN_FINISH;
                mHandler.sendMessage(msg);
            }

            ;
        }.start();
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.manage_center_favorite));
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
                SettingActivity.launch(CacheCleanActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void initView() {
        fabCleanCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanAll();
            }
        });
    }

    private class MyObserver extends IPackageStatsObserver.Stub {
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException {
            long cache = pStats.cacheSize;
            long codeSize = pStats.codeSize;
            if (cache > 0) {
                //System.out.println("当前应用程序："+pStats.packageName+"有缓存："+Formatter.formatFileSize(getApplicationContext(), cache));
                try {
                    Message msg = Message.obtain();
                    msg.what = SHOW_CACHE_INFO;
                    CacheInfo cacheInfo = new CacheInfo();
                    cacheInfo.packname = pStats.packageName;
                    cacheInfo.icon = pm.getApplicationInfo(pStats.packageName, 0).loadIcon(pm);
                    cacheInfo.name = pm.getApplicationInfo(pStats.packageName, 0).loadLabel(pm).toString();
                    cacheInfo.size = cache;
                    msg.obj = cacheInfo;
                    mHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initHandler() {
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SCANING:
                        String text = (String) msg.obj;
                        tvStatus.setText("正在扫描：" + text);
                        break;
                    case SHOW_CACHE_INFO:
                        View view = View.inflate(getApplicationContext(), R.layout.list_appcache_item, null);
                        ImageView iv = (ImageView) view.findViewById(R.id.iv_icon);
                        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                        TextView tv_cache = (TextView) view.findViewById(R.id.tv_cache);
                        final CacheInfo info = (CacheInfo) msg.obj;
                        iv.setImageDrawable(info.icon);
                        tv_name.setText(info.name);
                        tv_cache.setText("缓存大小：" + Formatter.formatFileSize(getApplicationContext(), info.size));
                        ImageView iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
                        iv_delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Method[] methods = PackageManager.class.getMethods();
                                for (Method method : methods) {
                                    try {
                                        if ("deleteApplicationCacheFiles".equals(method.getName())) {
                                            method.invoke(pm, info.packname, new IPackageDataObserver.Stub() {
                                                @Override
                                                public void onRemoveCompleted(String packageName, boolean succeeded)
                                                        throws RemoteException {

                                                }
                                            });
                                        }
                                    } catch (Exception e) {
                                        Intent intent = new Intent();
                                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setData(Uri.parse("package:" + info.packname));
                                        startActivity(intent);
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        llContainer.addView(view, 0);
                        break;
                    case SCAN_FINISH:
                        tvStatus.setText("扫描完毕");
                        break;
                }
            }

            ;
        };
    }

    class CacheInfo {
        Drawable icon;
        String name;
        long size;
        String packname;
    }

    public void cleanAll(){
//		/freeStorageAndNotify
        Method[] methods = PackageManager.class.getMethods();
        for(Method method:methods){
            if("freeStorageAndNotify".equals(method.getName())){
                try {
                    method.invoke(pm, Integer.MAX_VALUE, new IPackageDataObserver.Stub() {
                        @Override
                        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                            System.out.println(succeeded);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}
