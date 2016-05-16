package com.zurich.mobile.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.db.Dao.AppLockDao;
import com.zurich.mobile.engine.AppInfoProvider;
import com.zurich.mobile.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 程序锁页面
 * Created by weixinfei on 16/5/11.
 */
public class AppLockActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.app_lock_toolbar)
    Toolbar mToolbar;
    private TextView tv_unlock;
    private TextView tv_locked;
    private LinearLayout ll_unlock;
    private LinearLayout ll_locked;

    private ListView lv_unlock;
    private ListView lv_locked;

    private List<AppInfo> appInfos;

    private TextView tv_unlock_count;
    private TextView tv_locked_count;

    private AppLockDao dao;

    private List<AppInfo> unlockAppInfos;
    private List<AppInfo> lockedAppInfos;

    private AppLockAdapter unlockadapter;
    private AppLockAdapter lockedadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);

        initActionBar();

        initView();
        dao = new AppLockDao(this);
        initData();
    }

    private void initData() {
        // 获取所有的应用程序信息的集合。//下面的逻辑最好放在子线程。
        appInfos = AppInfoProvider.getAppInfos(this);
        // 过滤所有应用程序的集合 把未加锁的和已加锁的appinfo给区分出来。
        unlockAppInfos = new ArrayList<AppInfo>();
        lockedAppInfos = new ArrayList<AppInfo>();
        for (AppInfo appinfo : appInfos) {
            if (dao.find(appinfo.getPackname())) {
                lockedAppInfos.add(appinfo);
            } else {
                unlockAppInfos.add(appinfo);
            }
        }
        unlockadapter = new AppLockAdapter(true);
        lv_unlock.setAdapter(unlockadapter);
        lockedadapter = new AppLockAdapter(false);
        lv_locked.setAdapter(lockedadapter);
    }

    private void initView() {
        tv_locked = (TextView) findViewById(R.id.tv_locked);
        tv_unlock = (TextView) findViewById(R.id.tv_unlock);
        ll_unlock = (LinearLayout) findViewById(R.id.ll_unlock);
        ll_locked = (LinearLayout) findViewById(R.id.ll_locked);
        lv_unlock = (ListView) findViewById(R.id.lv_unlock);
        lv_locked = (ListView) findViewById(R.id.lv_locked);
        tv_unlock_count = (TextView) findViewById(R.id.tv_unlock_count);
        tv_locked_count = (TextView) findViewById(R.id.tv_locked_count);
        tv_locked.setOnClickListener(this);
        tv_unlock.setOnClickListener(this);
    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.manage_center_privacy_protect));
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
                SettingActivity.launch(AppLockActivity.this);
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class AppLockAdapter extends BaseAdapter {
        /**
         * 标记是否是未加锁适配器
         */
        private boolean unlockflag = true;

        public AppLockAdapter(boolean unlockflag) {
            this.unlockflag = unlockflag;
        }

        @Override
        public int getCount() {// 返回当前界面有多少个条目
            if (unlockflag) {
                tv_unlock_count.setText("未加锁软件:" + unlockAppInfos.size() + "个");
                return unlockAppInfos.size();
            } else {
                tv_locked_count.setText("已加锁软件:" + lockedAppInfos.size() + "个");
                return lockedAppInfos.size();
            }
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            final View view;
            ViewHolder holder;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getApplicationContext(),
                        R.layout.list_applock_item, null);
                holder = new ViewHolder();
                holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
                holder.iv_status = (ImageView) view
                        .findViewById(R.id.iv_status);
                view.setTag(holder);
            }
            final AppInfo appinfo;
            if (unlockflag) {
                holder.iv_status.setBackgroundResource(R.drawable.ic_lock_open);
                appinfo = unlockAppInfos.get(position);
            } else {
                holder.iv_status.setBackgroundResource(R.drawable.ic_lock_outline);
                appinfo = lockedAppInfos.get(position);
            }
            holder.iv_icon.setImageDrawable(appinfo.getIcon());
            holder.tv_name.setText(appinfo.getName());
            holder.iv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 从当前界面移除item
                    // 把这个条目的包名给存到数据库
                    if (unlockflag) {// 未加锁列表
                        TranslateAnimation ta = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 1.0f,
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0);
                        ta.setDuration(500);
                        view.startAnimation(ta);
                        //让主线程在这等待500毫秒
                        //若干秒时间后 在主线程执行逻辑
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //会在主线程里面执行。
                                unlockAppInfos.remove(position);
                                dao.add(appinfo.getPackname());
                                lockedAppInfos.add(appinfo);
                                unlockadapter.notifyDataSetChanged();
                                lockedadapter.notifyDataSetChanged();
                            }
                        }, 500);


                    } else {
                        TranslateAnimation ta = new TranslateAnimation(
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, -1.0f,
                                Animation.RELATIVE_TO_SELF, 0,
                                Animation.RELATIVE_TO_SELF, 0);
                        ta.setDuration(500);
                        view.startAnimation(ta);
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                lockedAppInfos.remove(position);
                                dao.delete(appinfo.getPackname());
                                unlockAppInfos.add(appinfo);
                                unlockadapter.notifyDataSetChanged();
                                lockedadapter.notifyDataSetChanged();
                            }
                        }, 500);

                    }
                }
            });
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

    }

    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        ImageView iv_status;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_locked:
                tv_locked.setBackgroundResource(R.drawable.tab_right_pressed);
                tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
                ll_unlock.setVisibility(View.GONE);
                break;
            case R.id.tv_unlock:
                tv_locked.setBackgroundResource(R.drawable.tab_right_default);
                tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
                ll_unlock.setVisibility(View.VISIBLE);
                break;
        }
    }
}
