package com.zurich.mobile.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zurich.mobile.R;
import com.zurich.mobile.adapter.AssemblyPinnedSectionAdapter;
import com.zurich.mobile.adapter.itemfactory.TaskManagerItemFactory;
import com.zurich.mobile.adapter.itemfactory.TaskManagerTitleFactory;
import com.zurich.mobile.engine.TaskInfoProvider;
import com.zurich.mobile.model.TaskInfo;
import com.zurich.mobile.utils.GlobalUtils;
import com.zurich.mobile.utils.SystemInfoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 进程管理
 * Created by weixinfei on 16/5/2.
 */
public class TaskManagerActivity extends FragmentActivity {

    @Bind(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @Bind(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @Bind(R.id.tv_process_count)
    TextView tvProcessCount;
    @Bind(R.id.tv_mem_info)
    TextView tvMemInfo;
    @Bind(R.id.lv_taskmanager)
    ListView lvTaskmanager;
    @Bind(R.id.ll_loading)
    LinearLayout llLoading;
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            llLoading.setVisibility(View.GONE);
        }
    };
    private String listUserTaskTitle;
    private String listSystemTaskTitle;
    private AssemblyPinnedSectionAdapter mAdapter;

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
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvToolbarTitle.setText("进程管理");
    }


    private void initView() {
        runningProcessCount = SystemInfoUtils.getRunningProcessCount(this);
        availRam = SystemInfoUtils.getAvailRam(this);
        totalRam = SystemInfoUtils.getTotalRam(this);
        tvProcessCount.setText("运行中进程:" + runningProcessCount + "个");
        tvMemInfo.setText("剩余/总内存："
                + Formatter.formatFileSize(this, availRam) + "/"
                + Formatter.formatFileSize(this, totalRam));
    }

    private void initData() {
        llLoading.setVisibility(View.VISIBLE);
        new AsyncTask<Void, Void, Boolean>(){

            @Override
            protected Boolean doInBackground(Void... params) {
                taskInfos = TaskInfoProvider.getTaskInfos(getApplicationContext());
                if (taskInfos != null && taskInfos.size() >0)
                    return true;
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
                if (aBoolean){
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
                        mAdapter = new AssemblyPinnedSectionAdapter(new ArrayList<Object>());
                        mAdapter.addItemFactory(new TaskManagerTitleFactory());
                        mAdapter.addItemFactory(new TaskManagerItemFactory());
                        lvTaskmanager.setAdapter(mAdapter);
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

        if (systemTaskInfos != null && systemTaskInfos.size() > 0) {
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
        for (int i = 1; i < dataList.size(); i++){
            if (dataList.get(i) instanceof TaskInfo){
                if (((TaskInfo)dataList.get(i)).isChecked()){
                    am.killBackgroundProcesses(((TaskInfo)dataList.get(i)).getPackname());
                    savedMem += ((TaskInfo)dataList.get(i)).getMemsize();
                    total++;
                }else {
                    if (((TaskInfo)dataList.get(i)).isUserTask()){
                        userTaskInfos.add((TaskInfo) dataList.get(i));
                    }else {
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
    public void enterSetting(View view) {
//        Intent intent = new Intent(this, TaskManagerSettingActivity.class);
//        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateAdapter();
    }
}
