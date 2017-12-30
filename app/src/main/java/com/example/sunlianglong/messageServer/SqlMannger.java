package com.example.sunlianglong.messageServer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
/**
 * Created by sun liang long on 2017/11/16.
 * 数据库相关操作
 *
 //更新删除数据
 values.put("listname",2222);
 db.update("message",values,"userip=?",new String[]{"2.2.2.2"});
 db.delete("my_friends","netname = ?",new String[]{"bigdollar"});
 */
public class SqlMannger {
    Context context;
    MySQL mysql;
    public SqlMannger(Context conetxt) {
        this.context = context;
        mysql = new MySQL(conetxt);
    }
    // 添加好友
    public void addSQL(Friends friends) {
        SQLiteDatabase db = null;
        try {
            db = mysql.getWritableDatabase();
            ContentValues values = new ContentValues();
            //插入myfriends表
            values.put("ip",friends.getip());
            values.put("netname",friends.getNetname());
            values.put("geqian",friends.getGeqian());
            values.put("imageid",friends.getImageId());
            values.put("home",friends.getHome());
            values.put("school",friends.getSchool());
            values.put("class",friends.getaClass());
            values.put("phone",friends.getPhone());
            values.put("sex",friends.getSex());
            values.put("email",friends.getEmail());
            values.put("birthday",friends.getBirthday());
            values.put("fenzu","我的好友");
            values.put("beizhu",friends.getNetname());
            db.insert("my_friends",null,values);
            //给分组表相应列加一
            String sql="UPDATE fenzu SET number=number+1 WHERE name = '我的好友'";
            db.execSQL(sql);
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }
    //删除好友
    public void delete_friend(String his_ip,String fenzu){
        SQLiteDatabase db = null;
        try {
            db = mysql.getWritableDatabase();
            ContentValues values = new ContentValues();
            //删除好友
            db.delete("my_friends","ip = ?",new String[]{his_ip});
            //给分组表相应列减一
            String sql="UPDATE fenzu SET number=number-1 WHERE name = '"+fenzu+"'";
            db.execSQL(sql);
            db.close();
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }
    //查询my_friends表
    public int selectSQL(String fenzu_para) {
        //List<Friends> list = new ArrayList<Friends>();
        SQLiteDatabase db = null;
        Cursor cursor=null;
        String ip = null;
        int count=0;
        try {
            db = mysql.getReadableDatabase();
            //cursor = db.query("my_friends",null,null,null,null,null,null);
            cursor=db.query("my_friends", new String[]{"id as _id","ip","fenzu"},"fenzu=?", new String[]{fenzu_para}, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    count++;
                    ip = cursor.getString(cursor.getColumnIndex("ip"));
                    String fenzu = cursor.getString(cursor.getColumnIndex("fenzu"));
                    Log.d("ADDFriendFragment","ip is"+ip);
                    Log.d("ADDFriendFragment","fenzu is"+fenzu);
                    Log.d("ADDFriendFragment","count is"+count);
                }while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            cursor.close();
            db.close();
        }
        return count;
    }
    //获取好友信息
    public String[][] get_friends_info(String fenzu_para,int count_para) {
        //List<Friends> list = new ArrayList<Friends>();
        SQLiteDatabase db = null;
        Cursor cursor=null;
        String[][] hhh = new String[1][1];
        String[][] s = new String[count_para][4];
        int count=0;
        try {
            db = mysql.getReadableDatabase();
            cursor=db.query("my_friends", new String[]{"id as _id","ip","netname","beizhu","imageid"},"fenzu=?", new String[]{fenzu_para}, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    String ip  = cursor.getString(cursor.getColumnIndex("ip"));
                    s[count][0] = ip;
                    String netname  = cursor.getString(cursor.getColumnIndex("netname"));
                    s[count][1] =netname;

                    String beizhu = cursor.getString(cursor.getColumnIndex("beizhu"));
                    s[count][2] = beizhu;
                    int imageid  = cursor.getInt(cursor.getColumnIndex("imageid"));
                    s[count][3] =String.valueOf(imageid);
                    Log.d("ADDFriendFragment","好友信息：");
                    Log.d("ADDFriendFragment","ip is"+ip);
                    Log.d("ADDFriendFragment","netname is"+netname);
                    Log.d("ADDFriendFragment","geqian is"+beizhu);
                    Log.d("ADDFriendFragment","imageid is"+imageid);
                    count++;
                }while (cursor.moveToNext());
            }
            cursor.close();
            return s;
        } catch (Exception e) {
        } finally {
            cursor.close();
            db.close();
        }
        return hhh;
    }
    // 添加好友
    public void InsertFirst_two() {
        SQLiteDatabase db = null;
        try {
            db = mysql.getWritableDatabase();
            ContentValues values = new ContentValues();
            //插入myfriends表
            values.put("ip","0.0.0.0");
            values.put("netname","贴心机器人");
            values.put("geqian","我是你的贴心机器人");
            values.put("imageid",2130837601);
            values.put("home","28-414");
            values.put("school","cqupt");
            values.put("class","04911501");
            values.put("phone","18875143259");
            values.put("sex","男");
            values.put("email","111@qq.com");
            values.put("birthday","11-11");
            values.put("fenzu","我的好友");
            values.put("beizhu","贴心机器人");
            db.insert("my_friends",null,values);
        } catch (Exception e) {
        } finally {
            db.close();
        }
    }
    //安装软件后：插入分组信息
    public void InsertFirst(){
        SQLiteDatabase db = mysql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name","我的好友");
        values.put("number",1);
        db.insert("fenzu",null,values);
        values.put("name","朋友");
        values.put("number",0);
        db.insert("fenzu",null,values);
        values.put("name","家人");
        values.put("number",0);
        db.insert("fenzu",null,values);
        System.out.println("success insert!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Cursor cursor=null;
        String name = null;
        cursor = db.query("fenzu",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do{
                name = cursor.getString(cursor.getColumnIndex("name"));
                int num = cursor.getInt(cursor.getColumnIndex("number"));
                Log.d("ADDFriendFragment","name is"+name);
                Log.d("ADDFriendFragment","number is"+num);
            }while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
    }
    //查看我的分组并返回每个分组的人数
    public String[][] Look_Fenzu(){
        SQLiteDatabase db = mysql.getReadableDatabase();
        Cursor cursor=null;
        String name = null;
        String[][] hhh = new String[1][1];
        int count=0;
        try {
            //cursor = db.query("my_friends",null,null,null,null,null,null);
            cursor = db.query("fenzu",null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                do{
                    count++;
                    name = cursor.getString(cursor.getColumnIndex("name"));
                    int num = cursor.getInt(cursor.getColumnIndex("number"));
                }while (cursor.moveToNext());
            }
            cursor.close();
            //定义数组
            String[][] s = new String[count][2];
            int i=0;
            cursor = db.query("fenzu",null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                do{
                    s[i][0] = cursor.getString(cursor.getColumnIndex("name"));
                    int num = cursor.getInt(cursor.getColumnIndex("number"));
                    s[i][1] = String.valueOf(num);
                    Log.d("ADDFriendFragment","name is"+s[i][0]);
                    Log.d("ADDFriendFragment","number is"+s[i][1]);
                    i++;
                }while (cursor.moveToNext());
            }
            Log.d("ADDFriendFragment","zong_number is"+count);
            Log.d("ADDFriendFragment","s[i] is"+s.length);
            cursor.close();
            return s;
        } catch (Exception e) {
        } finally {
            db.close();
        }
        return hhh;
    }

    //根据netname查好友的备注和分组
    public String[] look_fenzu_beizhu(String netname_para) {
        SQLiteDatabase db = null;
        Cursor cursor=null;
        String[] s = new String[3];
        try {
            db = mysql.getReadableDatabase();
            cursor=db.query("my_friends", new String[]{"id as _id","ip","fenzu","beizhu"},"netname=?", new String[]{netname_para}, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    String fenzu  = cursor.getString(cursor.getColumnIndex("fenzu"));
                    s[0] = fenzu;
                    String beizhu  = cursor.getString(cursor.getColumnIndex("beizhu"));
                    s[1] =beizhu;
                    String ip  = cursor.getString(cursor.getColumnIndex("ip"));
                    s[2] = ip;
                }while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return s;
        } catch (Exception e) {
        } finally {
            db.close();
        }
        return s;
    }

    // 更换分组：参数：分组+ip
    public void ChangeFenzu(String change_fenzu,String my_ip,String now_fenzu) {
        SQLiteDatabase db = null;
        try {
            //对my_friends表进行修改
            db = mysql.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("fenzu", change_fenzu);
            db.update("my_friends", values, "ip=?", new String[]{my_ip});

            //对fenzu表进行修改
            //给分组表相应列加一
            String sql1="UPDATE fenzu SET number=number-1 WHERE name = '"+now_fenzu+"'";
            db.execSQL(sql1);
            String sql2="UPDATE fenzu SET number=number+1 WHERE name = '"+change_fenzu+"'";
            db.execSQL(sql2);

        } catch (Exception e) {
        } finally {
            db.close();
        }
    }
    //更改备注：传参：netname
    public void Change_beizhu(String netname,String beizhu_content){
        SQLiteDatabase db = mysql.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("beizhu", beizhu_content);
        db.update("my_friends", values, "netname=?", new String[]{netname});
        db.close();
    }
    //得到所有好友的昵称
    public String[] get_netname() {
        //List<Friends> list = new ArrayList<Friends>();
        SQLiteDatabase db = null;
        Cursor cursor=null;
        int count=0;
        int i=0;
        String[] hhh = {"error"};
        try {
            db = mysql.getReadableDatabase();
            //cursor = db.query("my_friends",null,null,null,null,null,null);

            cursor=db.query("my_friends", new String[]{"id as _id","netname"},null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    count++;
                }while (cursor.moveToNext());
            }
            cursor.close();


            String[] a = new String [count];
            cursor=db.query("my_friends", new String[]{"id as _id","netname"},null, null, null, null, null);
            if (cursor.moveToFirst()){
                do{
                    a[i]= cursor.getString(cursor.getColumnIndex("netname"));
                    i++;
                }while (cursor.moveToNext());
            }
            cursor.close();
            return a;
        } catch (Exception e) {
        } finally {
            cursor.close();
            db.close();
        }
        return hhh;
    }




}
