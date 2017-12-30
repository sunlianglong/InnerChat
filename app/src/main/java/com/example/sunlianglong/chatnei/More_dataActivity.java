package com.example.sunlianglong.chatnei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sunlianglong.PHPServer.RequireSQLinfo;

import org.json.JSONArray;
import org.json.JSONObject;

public class More_dataActivity extends AppCompatActivity {
    private TextView back_add;
    private ImageView back;
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
    private String getFriends_info;
    private String his_ip;
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String Read_user_for_search = "http://"+fangwen_ip+"/chatting/read_user_for_search.php";


    private TextView netname;
    private TextView geqian;
    private TextView ip;
    private TextView xingbie;
    private TextView shengri;
    private TextView guxiang;
    private TextView school;
    private TextView dianhua;
    private TextView youxiang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_data);
        back_add = (TextView) findViewById(R.id.back_to2);
        back = (ImageView)findViewById(R.id.back_to1);

        netname = (TextView)findViewById(R.id.netname);
        geqian = (TextView)findViewById(R.id.geqian);
        ip = (TextView)findViewById(R.id.ip);
        xingbie = (TextView)findViewById(R.id.xingbie);
        shengri = (TextView)findViewById(R.id.shengri);
        guxiang = (TextView)findViewById(R.id.guxiang);
        school = (TextView)findViewById(R.id.school);
        dianhua = (TextView)findViewById(R.id.dianhua);
        youxiang = (TextView)findViewById(R.id.youxiang);


        //获取对方的昵称
        Intent intent = getIntent();
        his_ip = intent.getStringExtra("his_ip");
        //点击返回按钮
        back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //好友信息
        final int[] i = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //得到json解析出来的message_list_info表
                    //String ip_adds,  String up_name,  String up_name_key
                    RequireSQLinfo read_message_list = new RequireSQLinfo();
                    getFriends_info = read_message_list.SendRequest(Read_user_for_search,"search",his_ip);
                    //在线程中判断是否得到成功从服务器得到数据
                    if (getFriends_info.length()==6){
                         }else {
                        JSONArray jsonArray = new JSONArray(getFriends_info);
                        for (; i[0] < jsonArray.length(); i[0]++) {
                            JSONObject jsonObject = jsonArray.optJSONObject(i[0]);
                            search_list_ip[0] = jsonObject.optString("ip_address");
                            search_list_netname[0] = jsonObject.optString("netname");
                            search_list_geqian[0] = jsonObject.optString("geqian_data");
                            search_list_home[0] = jsonObject.optString("home_data");
                            search_list_school[0] = jsonObject.optString("school_data");
                            search_list_class[0] = jsonObject.optString("class_data");
                            search_list_touxiang[0] = jsonObject.optString("touxiang_num");
                            search_list_sex[0] = jsonObject.optString("sex");
                            search_list_phone[0] = jsonObject.optString("phone_num");
                            search_list_email[0] = jsonObject.optString("email");
                            search_list_birthday[0] = jsonObject.optString("birthday");
                        }
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
        netname.setText(search_list_netname[0]);
        geqian.setText(search_list_geqian[0]);
        ip.setText(search_list_ip[0]);
        xingbie.setText(search_list_sex[0]);
        shengri.setText(search_list_birthday[0]);
        guxiang.setText(search_list_home[0]);
        school.setText(search_list_school[0]);
        dianhua.setText(search_list_phone[0]);
        youxiang.setText(search_list_email[0]);

    }
}
