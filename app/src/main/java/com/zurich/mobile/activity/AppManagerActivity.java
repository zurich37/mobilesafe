package com.zurich.mobile.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.AppManagerItemFactory;
import com.zurich.mobile.adapter.itemfactory.AppManagerTitleFactory;
import com.zurich.mobile.adapter.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.engine.AppInfoProvider;
import com.zurich.mobile.model.AppInfo;
import com.zurich.mobile.utils.DensityUtil;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.widget.HintView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 应用管理
 * Created by weixinfei on 16/5/3.
 */
public class AppManagerActivity extends BaseActivity {
    private static final String TAG = "AppManagerActivity";

    @Bind(R.id.recycler_app_manager)
    RecyclerView recyclerView;
    @Bind(R.id.tv_avail_rom)
    TextView tvAvailRom;
    @Bind(R.id.tv_avail_sd)
    TextView tvAvailSd;
    @Bind(R.id.toolbar_app_manager)
    Toolbar mToolbar;
    @Bind(R.id.hint_app_manager)
    HintView hintView;
    private List<AppInfo> appInfos;

    /**
     * 用户程序集合
     */
    private List<AppInfo> userAppInfos;

    /**
     * 系统程序集合
     */
    private List<AppInfo> systemAppInfos;

    private PopupWindow popupWindow;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            hintView.hidden();
        }
    };
    private AssemblyRecyclerAdapter mAdapter;
    private String listUserAppTitle;
    private String listSystemAppTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        ButterKnife.bind(this);

        initActionBar();

        initView();

        initData();

        initData();

    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.safe_soft_manager));
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
                SettingActivity.launch(AppManagerActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        tvAvailSd.setText("SD卡可用：" + getTotalSpace(Environment.getExternalStorageDirectory().getAbsolutePath()));
        tvAvailRom.setText("内存可用：" + getTotalSpace(Environment.getDataDirectory().getAbsolutePath()));
        //滑动时隐藏气泡
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 | dy > 0) {
                    dismissPopupWindow();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void initData() {
        hintView.loading().show();
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                appInfos = AppInfoProvider.getAppInfos(getBaseContext());
                userAppInfos = new ArrayList<AppInfo>();
                systemAppInfos = new ArrayList<AppInfo>();
                for (AppInfo appinfo : appInfos) {
                    if (appinfo.isUserApp()) {
                        userAppInfos.add(appinfo);
                    } else {
                        systemAppInfos.add(appinfo);
                    }
                }
                if (appInfos != null && appInfos.size() > 0) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    if (mAdapter == null) {
                        mAdapter = new AssemblyRecyclerAdapter(new ArrayList<Object>());
                        mAdapter.addItemFactory(new AppManagerTitleFactory());
                        mAdapter.addItemFactory(new AppManagerItemFactory(new EventListener()));
                        recyclerView.setAdapter(mAdapter);
                    }
                    initAdapterData();
                    handler.sendEmptyMessage(0);
                }
            }
        }.execute();
    }

    private void initAdapterData() {
        List<Object> datas = mAdapter.getDataList();
        datas.clear();
        if (userAppInfos != null && userAppInfos.size() > 0) {
            listUserAppTitle = "个人应用(" + userAppInfos.size() + ")";
            datas.add(0, listUserAppTitle);
            datas.addAll(userAppInfos);
        }

        if (systemAppInfos != null && systemAppInfos.size() > 0) {
            listSystemAppTitle = "系统应用(" + systemAppInfos.size() + ")";
            datas.add((userAppInfos != null && userAppInfos.size() > 0) ? userAppInfos.size() + 1 : 0, listSystemAppTitle);
            datas.addAll(systemAppInfos);
        }

        updateAdapter();
    }

    private void updateAdapter() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取某个路径可用的空间
     *
     * @param path
     * @return
     */
    public String getTotalSpace(String path) {
        StatFs stat = new StatFs(path);
        long count = stat.getAvailableBlocks();
        long size = stat.getBlockSize();
        return Formatter.formatFileSize(this, count * size);
    }

    /**
     * 气泡显示
     *
     * @param view
     * @param appInfo
     */
    private void showPopupWindow(View view, final AppInfo appInfo) {
        dismissPopupWindow();
        View contentView = View.inflate(getApplicationContext(), R.layout.popup_item, null);
        LinearLayout llUninstall = (LinearLayout) contentView.findViewById(R.id.ll_uninstall);
        LinearLayout llStart = (LinearLayout) contentView.findViewById(R.id.ll_start);
        LinearLayout llShare = (LinearLayout) contentView.findViewById(R.id.ll_share);
        LinearLayout llShare1 = llShare;
        llShare1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "分享：" + appInfo.getName());
                shareApplication(appInfo);
            }
        });
        llStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "启动：" + appInfo.getName());
                startApplication(appInfo);
            }
        });
        llUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "卸载：" + appInfo.getName());
                uninstall(appInfo);
            }
        });

        //播放动画有一个前提 就是窗体必须有背景
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //必须要设置背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        int[] location = new int[2];
        view.getLocationInWindow(location);
        //根据手机手机的分辨率 把60dip 转化成 不同的值 px
        int px = DensityUtil.dip2px(getApplicationContext(), 80);
        System.out.println(px);
        popupWindow.showAtLocation(recyclerView, Gravity.TOP + Gravity.LEFT, px, location[1]);
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(200);
        ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(200);
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(sa);
        set.addAnimation(aa);
        contentView.startAnimation(set);
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 开启应用
     */
    private void startApplication(AppInfo appInfo) {
        Intent intent = new Intent();
        String packname = appInfo.getPackname();
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packinfo = pm.getPackageInfo(packname, PackageManager.GET_ACTIVITIES);
            ActivityInfo[] activityInfos = packinfo.activities;
            if (activityInfos != null && activityInfos.length > 0) {
                ActivityInfo activityinfo = activityInfos[0];
                intent.setClassName(packname, activityinfo.name);
                startActivity(intent);
            } else {
                GlobalUtils.showToast(getBaseContext(), "哎呀，这个应用程序没界面");
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GlobalUtils.showToast(getBaseContext(), "无法打开此应用");
        }
    }

    /**
     * 分享应用程序
     */
    private void shareApplication(AppInfo appInfo) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(
                Intent.EXTRA_TEXT,
                "推荐你使用一个软件，软件的名称叫："
                        + appInfo.getName()
                        + "，下载地址：https://play.google.com/store/apps/details?id=" + appInfo.getPackname());
        startActivity(intent);
    }

    private void uninstall(AppInfo appInfo) {
        if (appInfo.isUserApp()) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DELETE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + appInfo.getPackname()));
            startActivityForResult(intent, 0);
        } else {
            GlobalUtils.showToast(getBaseContext(), "系统应用需要有root权限后才能卸载");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    class EventListener implements AppManagerItemFactory.AppManagerClicEvent {

        @Override
        public void OnItemClick(View view, AppInfo appInfo) {
            showPopupWindow(view, appInfo);
        }
    }
}
