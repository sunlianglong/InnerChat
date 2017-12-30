package com.example.sunlianglong.messageServer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by sun liang long on 2017/10/22.
 * 创建本地SQLite
 */
public class MySQL extends SQLiteOpenHelper{
    public MySQL(Context context) {
        super(context, "message_list.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        //所有好友
//        db.execSQL("drop table if exists my_friends");
//        db.execSQL("drop table if exists personal_data");
//        db.execSQL("drop table if exists fenzu");
        db.execSQL("create table IF NOT EXISTS my_friends(id integer primary key autoincrement," +
                "ip varchar(20) UNIQUE,netname varchar(30),geqian varchar(50)," +
                "imageid integer,home varchar(30),school varchar(30),class varchar(30)," +
                "sex varchar(10),phone varchar(20),email varchar(20),birthday varchar(20)," +
                "fenzu varchar(20),beizhu varchar(30))");
        db.execSQL("create table IF NOT EXISTS personal_data(id integer primary key autoincrement," +
                "ip varchar(30) UNIQUE,netname varchar(50),geqian varchar(50),imageid integer,recetime varchar(50))");
        db.execSQL("create table IF NOT EXISTS fenzu(name varchar(30) UNIQUE,number integer)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists my_friends");
        db.execSQL("drop table if exists personal_data");
        db.execSQL("drop table if exists fenzu");
        onCreate(db);
    }
}
