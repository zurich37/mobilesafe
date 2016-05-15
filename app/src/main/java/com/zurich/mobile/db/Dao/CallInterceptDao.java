package com.zurich.mobile.db.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zurich.mobile.db.CallInterceptDBOpenHelper;
import com.zurich.mobile.model.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 拦截电话数据库
 * Created by weixinfei on 16/5/9.
 */
public class CallInterceptDao {
    private CallInterceptDBOpenHelper helper;

    /**
     * 构造方法中完成数据库打开帮助类的初始化
     *
     * @param context
     */
    public CallInterceptDao(Context context) {
        helper = new CallInterceptDBOpenHelper(context);
    }

    /**
     * 添加一条拦截号码
     *
     * @param number 拦截号码
     */
    public void add(String number, int count) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("count", count);
        db.insert("callintercept", null, values);
        db.close();
    }

    /**
     * 删除一条拦截号码
     *
     * @param number 拦截号码
     */
    public void delete(String number) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("callintercept", "number=?", new String[]{number});
        db.close();
    }


    /**
     * 获取全部的拦截号码信息。
     *
     * @return
     */
    public List<BlackNumberInfo> findAll() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("callintercept", new String[]{"number", "count"}, null, null, null, null, null);
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.number = cursor.getString(0);
            info.count = cursor.getInt(1);
            infos.add(info);
        }
        cursor.close();
        db.close();
        return infos;
    }

    /**
     * 分页获取部分的拦截号码信息。
     *
     * @param startIndex 查询的开始位置
     * @return
     */
    public List<BlackNumberInfo> findPart(int startIndex) {
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select number from callintercept order by _id desc limit 20 offset ?", new String[]{String.valueOf(startIndex)});
        List<BlackNumberInfo> infos = new ArrayList<BlackNumberInfo>();
        while (cursor.moveToNext()) {
            BlackNumberInfo info = new BlackNumberInfo();
            info.number = cursor.getString(0);
            info.count = cursor.getInt(1);
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
        Cursor cursor = db.rawQuery("select count(*) from callintercept ", null);
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
        Cursor cursor = db.query("callintercept", null, "number=?", new String[]{number}, null, null, null);
        if (cursor.moveToNext()) {
            result = true;
        }
        cursor.close();
        db.close();
        return result;
    }

}
