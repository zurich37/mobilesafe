package com.zurich.mobile.db.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zurich.mobile.db.SmsInterceptDBOpenHelper;
import com.zurich.mobile.model.SmsDataInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信拦截数据库增删改查的工具类
 * Created by weixinfei on 16/5/9.
 */
public class SmsInterceptDao {
    private SmsInterceptDBOpenHelper helper;

    /**
     * 构造方法中完成数据库打开帮助类的初始化
     *
     * @param context
     */
    public SmsInterceptDao(Context context) {
        helper = new SmsInterceptDBOpenHelper(context);
    }

    /**
     * 拦截添加一条短信
     *
     * @param number 黑名单号码
     */
    public void add(String number, String content) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("content", content);
        db.insert("smsintercept", null, values);
        db.close();
    }

    /**
     * 删除一条黑名单号码
     *
     * @param number 黑名单号码
     */
    public void delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("smsintercept", "number=?", new String[]{number});
        db.close();
    }

    /**
     * 获取全部的黑名单号码信息。
     *
     * @return
     */
    public List<SmsDataInfo> findAll() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("smsintercept", new String[]{"number", "content"}, null, null, null, null, null);
        List<SmsDataInfo> infos = new ArrayList<SmsDataInfo>();
        while (cursor.moveToNext()) {
            SmsDataInfo info = new SmsDataInfo();
            info.senderNum = cursor.getString(0);
            info.smsInfo = cursor.getString(1);
            infos.add(info);
        }
        cursor.close();
        db.close();
        return infos;
    }

    /**
     * 分页获取部分的黑名单号码信息。
     *
     * @param startIndex 查询的开始位置
     * @return
     */
    public List<SmsDataInfo> findPart(int startIndex) {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number,content  from smsintercept order by _id desc limit 20 offset ?", new String[]{String.valueOf(startIndex)});
        List<SmsDataInfo> infos = new ArrayList<SmsDataInfo>();
        while (cursor.moveToNext()) {
            SmsDataInfo info = new SmsDataInfo();
            info.senderNum = cursor.getString(0);
            info.smsInfo = cursor.getString(1);
            infos.add(info);
        }
        cursor.close();
        db.close();
        return infos;
    }

    /**
     * 获取数据库一共有多少条记录
     *
     * @return int 总条目个数
     */
    public int getTotalCount() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) from smsintercept ", null);
        cursor.moveToNext();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }

    /**
     * 查询黑名单号码是否存在
     *
     * @param number
     * @return
     */
    public boolean find(String number) {
        boolean result = false;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("smsintercept", null, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

}
