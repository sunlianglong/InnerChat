package com.example.sunlianglong.chatnei;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sunlianglong.PHPServer.RequireSQLinfo;
import com.example.sunlianglong.PHPServer.WriteIntoSQL;
import com.example.sunlianglong.messageServer.Friends;
import com.example.sunlianglong.messageServer.MySQL;
import com.example.sunlianglong.messageServer.SqlMannger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class AddFriendsActivity extends AppCompatActivity {
    private ImageView back_add;
    private AutoCompleteTextView friends_info_text;
    private String friends_info;
    private TextView add;
    private int flag=0;
    private String getFriends_info;
    private String[] search_list_ip = new String[1];
    private String[] search_list_netname = new String[1];
    private String[] search_list_geqian = new String[1];
    private String[] search_list_home = new String[1];
    private String[] search_list_school = new String[1];
    private String[] search_list_class = new String[1];
    private String[] search_list_touxiang = new String[1];
    private String[] search_list_sex = new String[1];
    private String[] search_list_phone = new String[1];
    private String[] search_list_email = new String[1];
    private String[] search_list_birthday= new String[1];
    private MySQL mySQL;
    private String my_ip;
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String URL_write_toAddFriends = "http://"+fangwen_ip+"/chatting/write_into_AddFriends.php";
    private String URL_write_toMessage_List = "http://"+fangwen_ip+"/chatting/write_into_messageListInfo.php";
    private String Read_heimingdan_list = "http://"+fangwen_ip+"/chatting/read_heimingdan_list.php";
    private String Read_user_for_search = "http://"+fangwen_ip+"/chatting/read_user_for_search.php";
    private int[] Current_time = new int[6];
    private String  heimingdan_list;
    private int heimingdan_flag=0;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        //接收Fragment传过来的值
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        my_ip=bundle.getString("my_ip");
        Log.i("---------->", my_ip);
        Current_time=get_time();

        back_add = (ImageView) findViewById(R.id.addfriends_back);
        back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        friends_info_text = (AutoCompleteTextView)findViewById(R.id.friends_info_search);
        add = (TextView)findViewById(R.id.add_friends_text);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] i = {0};
                friends_info = friends_info_text.getText().toString();
                System.out.println("Add_friends:"+friends_info);


                //判断是否在黑名单中
                final String[] heimingdan_ip = new String[100];
                final int[] count = {0};
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            //得到json解析出来的message_list_info表
                            //String ip_adds,  String up_name,  String up_name_key
                            RequireSQLinfo read_message_list = new RequireSQLinfo();
                            heimingdan_list = read_message_list.SendRequest(Read_heimingdan_list,"my_ip",friends_info);
                            //在线程中判断是否得到成功从服务器得到数据
                            Log.i("jsonString1",heimingdan_list);
                            JSONArray jsonArray = new JSONArray(heimingdan_list);
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.optJSONObject(j);
                                heimingdan_ip[j] = jsonObject.optString("his_ip");
                                count[0]++;
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                //睡眠等待获取网络数据成功 0.3s
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int k=0;k<count[0];k++){
                    if (my_ip.equals(heimingdan_ip[k])){
                        heimingdan_flag=1;
                    }
                }

                if(heimingdan_flag==0){


                    //加好友
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                //得到json解析出来的message_list_info表
                                //String ip_adds,  String up_name,  String up_name_key
                                RequireSQLinfo read_message_list = new RequireSQLinfo();
                                getFriends_info = read_message_list.SendRequest(Read_user_for_search,"search",friends_info);
                                Log.i("getFriends_info",flag+"+"+getFriends_info.length());
                                //在线程中判断是否得到成功从服务器得到数据
                                if (getFriends_info.length()==6){
                                    //System.out.println("AddFriends:NOT find this persion");
                                    // Toast.makeText(AddFriendsActivity.this,"内网中没有找到这位朋友~",Toast.LENGTH_SHORT).show();
                                }else {
                                    flag=1;
                                    Log.i("getFriends_info",flag+"+"+getFriends_info);
                                    JSONArray jsonArray = new JSONArray(getFriends_info);
                                    for (; i[0] < jsonArray.length(); i[0]++) {
                                        JSONObject jsonObject = jsonArray.optJSONObject(i[0]);
                                        search_list_ip[i[0]] = jsonObject.optString("ip_address");
                                        search_list_netname[i[0]] = jsonObject.optString("netname");
                                        search_list_geqian[i[0]] = jsonObject.optString("geqian_data");
                                        search_list_home[i[0]] = jsonObject.optString("home_data");
                                        search_list_school[i[0]] = jsonObject.optString("school_data");
                                        search_list_class[i[0]] = jsonObject.optString("class_data");
                                        search_list_touxiang[i[0]] = jsonObject.optString("touxiang_num");
                                        search_list_sex[i[0]] = jsonObject.optString("sex");
                                        search_list_phone[i[0]] = jsonObject.optString("phone_num");
                                        search_list_email[i[0]] = jsonObject.optString("email");
                                        search_list_birthday[i[0]] = jsonObject.optString("birthday");
                                    }
                                }
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (flag ==1){
                        //插入friends表我的好友
                        Friends friends = new Friends(search_list_ip[0],search_list_netname[0],search_list_geqian[0],
                                search_list_touxiang[0],search_list_sex[0],search_list_home[0],search_list_school[0],
                                search_list_class[0],search_list_phone[0],search_list_email[0],search_list_birthday[0]);
                        SqlMannger sqlMannger = new SqlMannger(AddFriendsActivity.this);
                        sqlMannger.addSQL(friends);
                        //之后在这里做一个判断 如果好友已经添加则不需要再次添加。

                        mySQL = new MySQL(AddFriendsActivity.this);
                        SQLiteDatabase db  =mySQL.getWritableDatabase();

                        //插入message_list表
                        ContentValues values2 = new ContentValues();
                        values2.put("ip",search_list_ip[0]);
                        values2.put("netname",search_list_netname[0]);
                        values2.put("geqian",search_list_geqian[0]);
                        values2.put("imageid",search_list_touxiang[0]);
                        values2.put("recetime",Current_time[4]+":"+Current_time[5]);
                        db.insert("personal_data",null,values2);

                        //将添加的好友写入my_all_friends_info
                        String[] php_name = new String[2];
                        php_name[0]="ip";php_name[1]="friend_ip";
                        String[] value_name = new String[2];
                        value_name[0]=my_ip;value_name[1]=search_list_ip[0];
                        WriteIntoSQL w = new WriteIntoSQL();
                        w.writetoSQL(php_name,value_name,URL_write_toAddFriends);

                        //将第一次添加的好友写入message_list_info
                        String[] php_name2 = new String[6];
                        php_name2[0]="ip";php_name2[1]="friend_ip";php_name2[2]="netname";
                        php_name2[3]="touxiang_id";php_name2[4]="state";php_name2[5]="geqian";
                        String[] value_name2 = new String[6];
                        value_name2[0]=my_ip;value_name2[1]=search_list_ip[0];value_name2[2]=search_list_netname[0];
                        value_name2[3]=search_list_touxiang[0];value_name2[4]="online";value_name2[5]=search_list_geqian[0];
                        System.out.println(""+value_name2[2]+" "+value_name2[3]+" "+ value_name2[4] + " "+value_name2[5]);
                        WriteIntoSQL w2 = new WriteIntoSQL();
                        w2.writetoSQL(php_name2,value_name2,URL_write_toMessage_List);

                        Toast.makeText(AddFriendsActivity.this,"添加 "+search_list_ip[0]+":"+search_list_netname[0]+" 成功！",Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(AddFriendsActivity.this,"内网中没找到这位朋友~",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(AddFriendsActivity.this,"内网中没找到这位朋友~",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    public int[] get_time(){
        int[] time = new int[6];
        Calendar c = Calendar.getInstance();//
        time[0] = c.get(Calendar.YEAR); // 获取当前年份
        time[1] = c.get(Calendar.MONTH) + 1;// 获取当前月份
        time[2] = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        time[3] = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        time[4] = c.get(Calendar.HOUR_OF_DAY);//时
        time[5] = c.get(Calendar.MINUTE);//分
        return  time;
    }
}
