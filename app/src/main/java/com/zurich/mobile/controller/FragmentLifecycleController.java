package com.zurich.mobile.controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressLint("LongLogTag")
public class FragmentLifecycleController implements Runnable {
    private static final String TAG = "FragmentLifecycleController";
    private static final boolean DEBUG = false;
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final int DELAY_MILLIS = 300;
    private static final int TASK_TYPE_SHOW_DATA = 1101;
    private static final int TASK_TYPE_LOAD_DATA = 1102;

    @SuppressWarnings("FieldCanBeLocal")
    private String fragmentName;
    private LifecycleCallback lifecycleCallback;

    private boolean loading;
    private boolean needShowData;
    private boolean delayedProcess;
    @TaskType
    private int taskType = 0;
    private boolean disableDelay;

    // Fragment状态
    private boolean created;
    private boolean viewCreated;
    private boolean visibleToUser;

    public FragmentLifecycleController(Fragment fragment, LifecycleCallback lifecycleCallback) {
        //noinspection ConstantConditions
        this.fragmentName = DEBUG ? fragment.getClass().getSimpleName() : null;
        this.lifecycleCallback = lifecycleCallback;
    }

    public void create() {
        created = true;
    }

    public View createView(LayoutInflater inflater, ViewGroup container) {
        int contentViewLayoutId = lifecycleCallback.getContentViewLayoutId();
        if (contentViewLayoutId != 0) {
            return inflater.inflate(contentViewLayoutId, container, false);
        } else {
            return null;
        }
    }

    public void viewCreated(View view, Bundle savedInstanceState) {
        if (DEBUG) {
            Log.w(TAG, fragmentName + ": viewCreated");
        }
        viewCreated = true;
        needShowData = true;
        lifecycleCallback.onInitViews(view, savedInstanceState);

        if (disableDelay) {
            if (lifecycleCallback.isReadyData()) {
                if (DEBUG) {
                    Log.w(TAG, fragmentName + ": Execute showData() in viewCreated");
                }
                lifecycleCallback.onShowData();
                needShowData = false;
            }
        }
    }

    public void visibleToUserChanged(boolean isVisibleToUser) {
        this.visibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            if (disableDelay) {
                if(needShowData){
                    if(lifecycleCallback.isReadyData()){
                        lifecycleCallback.onShowData();
                        needShowData = false;
                    }else{
                        if (DEBUG) {
                            Log.w(TAG, fragmentName + ": Execute loadData() in visibleToUser");
                        }
                        loadData();
                    }
                }else{
                    if (!lifecycleCallback.isReadyData()) {
                        if (DEBUG) {
                            Log.w(TAG, fragmentName + ": Execute loadData() in visibleToUser");
                        }
                        loadData();
                    }
                }
            } else {
                if (needShowData) {
                    if (lifecycleCallback.isReadyData()) {
                        if (DEBUG) {
                            Log.w(TAG, fragmentName + ": Delay execute showData() in visibleToUser");
                        }
                        taskType = TASK_TYPE_SHOW_DATA;
                        handler.postDelayed(this, DELAY_MILLIS);
                        delayedProcess = true;
                    } else {
                        if (DEBUG) {
                            Log.w(TAG, fragmentName + ": Delay execute loadData() in visibleToUser");
                        }
                        taskType = TASK_TYPE_LOAD_DATA;
                        handler.postDelayed(this, DELAY_MILLIS);
                        delayedProcess = true;
                    }
                } else {
                    if (!lifecycleCallback.isReadyData()) {
                        if (DEBUG) {
                            Log.w(TAG, fragmentName + ": Delay execute loadData() in visibleToUser");
                        }
                        taskType = TASK_TYPE_LOAD_DATA;
                        handler.postDelayed(this, DELAY_MILLIS);
                        delayedProcess = true;
                    }
                }
            }
        } else {
            if (delayedProcess) {
                if (DEBUG) {
                    Log.w(TAG, fragmentName + ": No visible to user, " + (taskType == TASK_TYPE_SHOW_DATA ? "show task" : "load task") + "canceled");
                }
                handler.removeCallbacks(this);
                delayedProcess = false;
            }
        }
    }

    public void destroyView() {
        if (DEBUG) {
            Log.w(TAG, fragmentName + ": destroyView");
        }
        viewCreated = false;
    }

    public void destroy() {
        if (DEBUG) {
            Log.w(TAG, fragmentName + ": destroy");
        }
        created = false;
    }

    public void showData() {
        if (viewCreated) {
            if (visibleToUser) {
                if (DEBUG) {
                    Log.w(TAG, fragmentName + ": Execute onShowData()");
                }
                lifecycleCallback.onShowData();
                needShowData = false;
            } else {
                if (DEBUG) {
                    Log.w(TAG, fragmentName + ": No visible to user, does not perform onShowData()");
                }
            }
        } else {
            if (DEBUG) {
                Log.w(TAG, fragmentName + ": View No create, does not perform onShowData()");
            }
        }
    }

    public void loadData() {
        if (loading) {
            return;
        }
        if (DEBUG) {
            Log.w(TAG, fragmentName + ": Execute onLoadData()");
        }
        lifecycleCallback.onLoadData();
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    @SuppressWarnings("unused")
    public boolean isDisableDelay() {
        return disableDelay;
    }

    public void setDisableDelay(boolean disableDelay) {
        this.disableDelay = disableDelay;
    }

    public boolean isLoading() {
        return loading;
    }

    @SuppressWarnings("unused")
    public boolean isNeedShowData() {
        return needShowData;
    }

    @SuppressWarnings("unused")
    public boolean isDelayedProcess() {
        return delayedProcess;
    }

    @SuppressWarnings("unused")
    public boolean isViewCreated() {
        return viewCreated;
    }

    public boolean isCreated() {
        return created;
    }

    @Override
    public void run() {
        delayedProcess = false;
        if (taskType == TASK_TYPE_SHOW_DATA) {
            if (DEBUG) {
                Log.w(TAG, fragmentName + ": Execute showData() in run");
            }
            showData();
        } else if (taskType == TASK_TYPE_LOAD_DATA) {
            if (DEBUG) {
                Log.w(TAG, fragmentName + ": Execute loadData() in run");
            }
            loadData();
        }
    }

    public interface LifecycleCallback {
        int getContentViewLayoutId();
        void onInitViews(View view, Bundle savedInstanceState);
        boolean isReadyData();
        void onShowData();
        void onLoadData();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
    @IntDef({TASK_TYPE_SHOW_DATA, TASK_TYPE_LOAD_DATA})
    public @interface TaskType {

    }
}
