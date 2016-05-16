package com.zurich.mobile.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.zurich.mobile.activity.EnterPwdActivity;
import com.zurich.mobile.db.Dao.AppLockDao;

import java.util.List;

/**
 * 隐私保护
 * Created by weixinfei on 16/5/10.
 */
public class PrivacyService extends Service {
    public static final String TAG = "PrivacyService";
    private ActivityManager am;
    private boolean flag;
    private AppLockDao dao;
    /**
     * 临时停止保护的包名
     */
    private String tempStopProtectpackname;

    private InnerReceiver receiver;

    /**
     * 被保护的包名集合
     */
    private List<String> protectedPacknames;

    private MyObserver observer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private  class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            tempStopProtectpackname = intent.getStringExtra("packname");
            Log.i(TAG,"看门狗得到了消息，临时的停止对某个应用程序的保护:"+tempStopProtectpackname);

        }
    }

    @Override
    public void onCreate() {
        receiver = new InnerReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.zurich.mobile.stopprotect");
        registerReceiver(receiver, filter);
        dao = new AppLockDao(this);
        protectedPacknames = dao.findAll();
        //注册一个内容观察者
        Uri uri = Uri.parse("content://com.zurich.mobile/applockdb");
        observer = new MyObserver(new Handler());
        getContentResolver().registerContentObserver(uri, true, observer);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final Intent intent = new Intent(this,EnterPwdActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        new Thread(){
            public void run() {
                flag = true;
                while(flag){
                    String packname = "";
                    //巡逻 监视当前运行的应用程序  得到当前任务栈集合最前的任务栈信息  当前要开启的程序
                    if (Build.VERSION.SDK_INT > 20) {
                        // 5.0及其以后的版本
                        List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
                        if (null != tasks && tasks.size() > 0) {
                            packname = tasks.get(0).processName;
                        }
                    } else{
                        // 5.0之前
                        // 获取正在运行的任务栈(一个应用程序占用一个任务栈) 最近使用的任务栈会在最前面
                        // 1表示给集合设置的最大容量 List<RunningTaskInfo> infos = am.getRunningTasks(1);
                        // 获取最近运行的任务栈中的栈顶Activity(即用户当前操作的activity)的包名
                        ActivityManager.RunningTaskInfo taskInfo  = am.getRunningTasks(1).get(0);
                        packname = taskInfo.topActivity.getPackageName();
                    }

                    if(protectedPacknames.contains(packname)){//查询内存
                        //检查是否需要临时停止保护
                        if(packname.equals(tempStopProtectpackname)){
                            //什么事情都不做。
                        }else{
                            //不需要临时停止保护前应用程序需要保护， 关门放狗
                            intent.putExtra("packname", packname);
                            startActivity(intent);
                        }
                    }else{

                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
        super.onCreate();
    }


    private class MyObserver extends ContentObserver {

        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.i(TAG,"啊啊啊啊，我发现了数据库的内容变化了。");
            protectedPacknames = dao.findAll();
        }

    }

    @Override
    public void onDestroy() {
        flag = false;
        unregisterReceiver(receiver);
        receiver = null;
        getContentResolver().unregisterContentObserver(observer);
        observer = null;
        super.onDestroy();
    }
}
