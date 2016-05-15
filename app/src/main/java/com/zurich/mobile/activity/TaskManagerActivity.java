package com.zurich.mobile.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.itemfactory.TaskManagerItemFactory;
import com.zurich.mobile.adapter.itemfactory.TaskManagerTitleFactory;
import com.zurich.mobile.assemblyadapter.AssemblyRecyclerAdapter;
import com.zurich.mobile.engine.TaskInfoProvider;
import com.zurich.mobile.model.TaskInfo;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.SharedPreferenceUtil;
import com.zurich.mobile.utils.SystemInfoUtils;
import com.zurich.mobile.widget.HintView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 进程管理
 * Created by weixinfei on 16/5/2.
 */
public class TaskManagerActivity extends BaseActivity {

    @Bind(R.id.tv_process_count)
    TextView tvProcessCount;
    @Bind(R.id.tv_mem_info)
    TextView tvMemInfo;
    @Bind(R.id.task_manager_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.btn_task_setting)
    Button btnTaskSetting;
    @Bind(R.id.hint_task_hint)
    HintView hintView;
    @Bind(R.id.lv_taskmanager)
    RecyclerView recyclerView;
    // 活动管理器 activitymanager
    private ActivityManager am;

    private List<TaskInfo> taskInfos;
    /**
     * 用户进程集合
     */
    private List<TaskInfo> userTaskInfos;

    /**
     * 系统进程集合
     */
    private List<TaskInfo> systemTaskInfos;

    // 正在运行进程数量
    private int runningProcessCount;
    // 可用用ram内存
    private long availRam;
    // 总内存
    private long totalRam;

    private String listUserTaskTitle;
    private String listSystemTaskTitle;
    private AssemblyRecyclerAdapter mAdapter;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            hintView.hidden();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        ButterKnife.bind(this);

        initActionBar();

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        initView();

        initData();

    }

    private void initActionBar() {
        mToolbar.setTitle(getResources().getString(R.string.safe_task_manager));
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
                enterSetting();
                return true;
            case R.id.men_action_about_me:
                return true;
            case R.id.menu_action_share:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        runningProcessCount = SystemInfoUtils.getRunningProcessCount(this);
        availRam = SystemInfoUtils.getAvailRam(this);
        totalRam = SystemInfoUtils.getTotalRam(this);
        tvProcessCount.setText("运行中进程:" + runningProcessCount + "个");
        tvMemInfo.setText("剩余/总内存："
                + Formatter.formatFileSize(this, availRam) + "/"
                + Formatter.formatFileSize(this, totalRam));

        btnTaskSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterSetting();
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
                taskInfos = TaskInfoProvider.getTaskInfos(getApplicationContext());
                if (taskInfos != null && taskInfos.size() > 0)
                    return true;
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean) {
                    userTaskInfos = new ArrayList<TaskInfo>();
                    systemTaskInfos = new ArrayList<TaskInfo>();
                    for (TaskInfo taskInfo : taskInfos) {
                        if (taskInfo.isUserTask()) {
                            userTaskInfos.add(taskInfo);
                        } else {
                            systemTaskInfos.add(taskInfo);
                        }
                    }
                    handler.sendEmptyMessage(0);
                    if (mAdapter == null) {
                        mAdapter = new AssemblyRecyclerAdapter(new ArrayList<Object>());
                        mAdapter.addItemFactory(new TaskManagerTitleFactory());
                        mAdapter.addItemFactory(new TaskManagerItemFactory());
                        recyclerView.setAdapter(mAdapter);
                    }
                    initAdapterData();
                }
            }
        }.execute();
    }

    private void initAdapterData() {
        List<Object> datas = mAdapter.getDataList();
        datas.clear();
        if (userTaskInfos != null && userTaskInfos.size() > 0) {
            listUserTaskTitle = "用户进程(" + userTaskInfos.size() + ")";
            datas.add(0, listUserTaskTitle);
            datas.addAll(userTaskInfos);
        }

        if (systemTaskInfos != null && systemTaskInfos.size() > 0 && SharedPreferenceUtil.getSysTaskVisiblePrefs()) {
            listSystemTaskTitle = "系统进程(" + systemTaskInfos.size() + ")";
            datas.add((userTaskInfos != null && userTaskInfos.size() > 0) ? userTaskInfos.size() + 1 : 0, listSystemTaskTitle);
            datas.addAll(systemTaskInfos);
        }

        updateAdapter();
    }

    /**
     * 全选
     */
    public void selectAll(View view) {
        for (TaskInfo info : userTaskInfos) {
            if (info.getPackname().equals(getPackageName())) {
                continue;
            }
            info.setChecked(true);
        }
        for (TaskInfo info : systemTaskInfos) {
            info.setChecked(true);
        }
        updateAdapter();
    }

    /**
     * 反选
     */
    public void unSelect(View view) {
        for (TaskInfo info : userTaskInfos) {
            if (info.getPackname().equals(getPackageName())) {
                continue;
            }
            info.setChecked(!info.isChecked());
        }
        for (TaskInfo info : systemTaskInfos) {
            info.setChecked(!info.isChecked());
        }
        updateAdapter();
    }

    /**
     * 杀死选中的进程
     */
    public void killAll(View view) {
        int total = 0;
        long savedMem = 0;

        List dataList = mAdapter.getDataList();
        userTaskInfos.clear();
        systemTaskInfos.clear();
        for (int i = 1; i < dataList.size(); i++) {
            if (dataList.get(i) instanceof TaskInfo) {
                if (((TaskInfo) dataList.get(i)).isChecked()) {
                    am.killBackgroundProcesses(((TaskInfo) dataList.get(i)).getPackname());
                    savedMem += ((TaskInfo) dataList.get(i)).getMemsize();
                    total++;
                } else {
                    if (((TaskInfo) dataList.get(i)).isUserTask()) {
                        userTaskInfos.add((TaskInfo) dataList.get(i));
                    } else {
                        systemTaskInfos.add((TaskInfo) dataList.get(i));
                    }
                }
            }
        }
        initAdapterData();

        // 给用户一个吐司提醒 告诉用户你干了什么事情。
        GlobalUtils.showToast(getBaseContext(), "成功清理" + total + "个进程,释放" + Formatter.formatFileSize(this, savedMem) + "的内存");
        runningProcessCount -= total;
        availRam += savedMem;
        tvProcessCount.setText("运行中进程:" + runningProcessCount + "个");
        tvMemInfo.setText("剩余/总内存："
                + Formatter.formatFileSize(this, availRam) + "/"
                + Formatter.formatFileSize(this, totalRam));
        updateAdapter();
    }

    private void updateAdapter() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    /**
     * 进入设置界面
     */
    public void enterSetting() {
        Intent intent = new Intent(this, TaskManagerSettingActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Boolean isShow = data.getBooleanExtra("is_show_sys", false);
            if (isShow) {
                initData();
            } else {
                systemTaskInfos.clear();
                initAdapterData();
            }
        }
    }
}
