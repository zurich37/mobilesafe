package com.zurich.mobile.db.Dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
     * 查询病毒数据库
     * Created by weixinfei on 16/5/1.
     */
    public class AntivirusDao {
        public static final String path = "/data/data/com.zurich.mobile/files/antivirus.db";

        /**
         * 查询是否是病毒
         *
         * @param md5
         * @return 病毒的描述信息，如果为null不是病毒
         */
        public static String isVirus(String md5) {
            String result = null;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
            Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
            if(cursor.moveToNext()){
                result = cursor.getString(0);
            }
            cursor.close();
            db.close();
            return result;
        }
    }