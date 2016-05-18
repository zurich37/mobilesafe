package com.zurich.mobile.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Telephony.Sms;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.zurich.mobile.db.Dao.BlackNumberDao;
import com.zurich.mobile.db.Dao.CallInterceptDao;
import com.zurich.mobile.db.Dao.SmsInterceptDao;
import com.zurich.mobile.utils.GlobalUtils;

import java.lang.reflect.Method;

/**
 * 骚扰拦截
 * Created by weixinfei on 16/5/10.
 */
public class CallSmsSafeService extends Service {
    public static final String TAG = "CallSmsSafeService";
    private InnerSmsReceiver receiver;
    private BlackNumberDao blackNumberDao;

    //监听当前呼叫的状态
    private TelephonyManager tm;
    private MyPhoneListener listener;
    private ContentObserver mObserver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"我是服务内部的广播接受者，我接受到了 短信到来的广播事件");
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for(Object obj : objs){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                //得到短信发件人
                String sender = smsMessage.getOriginatingAddress();
                String mode = blackNumberDao.findMode(sender);
                if("2".equals(mode)||"3".equals(mode)){
                    Log.i(TAG,"拦截到黑名单短信");
                    abortBroadcast();
                }

                String body = smsMessage.getMessageBody();
                SmsInterceptDao interceptDao = new SmsInterceptDao(getBaseContext());
                interceptDao.add(sender, body);
                //遍历查询数据库
                if(body.contains("fapiao")){
                    Log.i(TAG,"拦截到卖发票的短信");
                    abortBroadcast();
                }
            }
        }
    }


    @Override
    public void onCreate() {
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        listener = new MyPhoneListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);//监听电话的状态

        blackNumberDao = new BlackNumberDao(this);
        receiver = new InnerSmsReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(Integer.MAX_VALUE);
        registerReceiver(receiver, filter);

        mObserver = new ContentObserver(new Handler()) {

            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(Uri.parse("content://sms/inbox"), new String[] { "_id", "address", "body" }, null, null, "_id desc");
                long id = -1;

                if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                    id = cursor.getLong(0);
                    String address = cursor.getString(1);
                    String body = cursor.getString(2);

                    GlobalUtils.showToast(getBaseContext(), String.format("address: %s\n body: %s", address, body));
                }
                cursor.close();

                if (id != -1) {
                    int count = 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        count = resolver.delete(Sms.CONTENT_URI, "_id=" + id, null);
                    }
                    GlobalUtils.showToast(getBaseContext(), count == 1 ? "删除成功" : "删除失败");
                }
            }

        };

        getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mObserver);

        super.onCreate();
    }

    private class MyPhoneListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                    String mode = blackNumberDao.findMode(incomingNumber);
                    if("1".equals(mode)||"3".equals(mode)){
                        CallInterceptDao dao = new CallInterceptDao(getBaseContext());
                        int count = dao.findCount(incomingNumber) + 1;
                        dao.add(incomingNumber, count);
                        Log.i(TAG,"这是黑名单号码，挂断电话。。。");
                        //监视呼叫记录的生成，如果呼叫记录产生了。删除呼叫记录。
                        Uri url = Uri.parse("content://call_log/calls");
                        getContentResolver().registerContentObserver(url, true, new CallLogObserver(new Handler(), incomingNumber));
                        endcall();// 立刻把电话挂断了。  但是呼叫记录的生成 并不是一个同步的代码。 是一个异步代码
                    }
                    break;
            }
        }
    }

    private class CallLogObserver extends ContentObserver {
        private String incomingNumber;
        public CallLogObserver(Handler handler, String incomingNumber) {
            super(handler);
            this.incomingNumber = incomingNumber;
        }
        //数据变化会调用onchage方法
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getContentResolver().unregisterContentObserver(this);
            deleteCalllog(incomingNumber);
        }
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
        listener = null;
        getContentResolver().unregisterContentObserver(mObserver);
        mObserver = null;
        super.onDestroy();
    }

    /**
     * 清除呼叫记录
     * @param incomingNumber
     */
    public void deleteCalllog(String incomingNumber) {
        ContentResolver resolver = getContentResolver();
        Uri url = Uri.parse("content://call_log/calls");
        resolver.delete(url, "number=?", new String[]{incomingNumber});
    }

    /**
     * 挂断电话
     */
    public void endcall() {
        //ServiceManager.getService(TELEPHONY_SERVICE);
        try {
            Class clazz = CallSmsSafeService.class.getClassLoader().loadClass("android.os.ServiceManager");
            Method method = clazz.getDeclaredMethod("getService", String.class);
            IBinder b  = (IBinder) method.invoke(null, TELEPHONY_SERVICE);
            //获取到了原生未经包装的系统电话的管理服务。
            ITelephony iTelephony = ITelephony.Stub.asInterface(b);
            iTelephony.endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
