package com.zurich.mobile.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.zurich.mobile.Account;

public class AppStatusListenController {
    private static final String TAG = "AppStatusListener";
    private BroadcastReceiver broadcastReceiver;
    private AppStatusAndProgressListener appStatusAndProgressListener;
    private AppStatusListener appStatusListener;
    private boolean opened;

    public void setAppStatusAndProgressListener(AppStatusAndProgressListener appStatusAndProgressListener) {
        this.appStatusAndProgressListener = appStatusAndProgressListener;
    }

    public void setAppStatusListener(AppStatusListener appStatusListener) {
        this.appStatusListener = appStatusListener;
    }

    public boolean isOpened() {
        return opened;
    }

    public void open(Context context){
        if(!opened){
            opened = true;
            register(context);
        }
    }

    public void close(Context context){
        if(opened){
            opened = false;
            unregister(context);
        }
    }

    private void register(Context context){
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if(appStatusAndProgressListener != null){
                        appStatusAndProgressListener.onAppStatusAndProgressChanged();
                    }
                    if(appStatusListener != null){
                        if(!Account.ACTION_NOTIFY_DOWNLOADING_CONTENTS_CHANGED.equals(intent.getAction())){
                            appStatusListener.onAppStatusChanged();
                        }
                    }
                }
            };
        }

        IntentFilter packageStatusChangedIntentFilter = new IntentFilter();
        packageStatusChangedIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        packageStatusChangedIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        packageStatusChangedIntentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        packageStatusChangedIntentFilter.addDataScheme("package");
        try{
            context.registerReceiver(broadcastReceiver, packageStatusChangedIntentFilter);
        }catch (Exception e){
            Log.w(TAG, "packageStatusChanged registered");
        }

        IntentFilter appStatusChangedIntentFilter = new IntentFilter();
        appStatusChangedIntentFilter.addAction(Account.ACTION_PACKAGE_STATUS_CHANGED);
        appStatusChangedIntentFilter.addAction(Account.ACTION_NOTIFY_CHECK_INCREMENTAL_UPDATE);
        appStatusChangedIntentFilter.addAction(Account.ACTION_NOTIFY_DOWNLOADING_CONTENTS_CHANGED);
        try{
            context.registerReceiver(broadcastReceiver, appStatusChangedIntentFilter);
        }catch (Exception e){
            Log.w(TAG, "appStatusChanged registered");
        }
    }

    private void unregister(Context context){
        if (broadcastReceiver != null) {
            try{
                context.unregisterReceiver(broadcastReceiver);
            }catch (Exception e){
                Log.w(TAG, "unregistered");
            }
        }
    }

    public interface AppStatusAndProgressListener {
        void onAppStatusAndProgressChanged();
    }

    public interface AppStatusListener {
        void onAppStatusChanged();
    }
}
