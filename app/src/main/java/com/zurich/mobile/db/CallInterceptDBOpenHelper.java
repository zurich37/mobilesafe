package com.zurich.mobile.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 拦截的电话数据库表
 * Created by weixinfei on 16/5/9.
 */
public class CallInterceptDBOpenHelper extends SQLiteOpenHelper {
    public CallInterceptDBOpenHelper(Context context) {
        super(context, "callintercept.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库的表结构  主键_id 自增长  number黑名单号码
        db.execSQL("create table callintercept (_id integer primary key autoincrement , number varchar(20) , count int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
