package com.example.sunlianglong.chatnei;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sunlianglong.PHPServer.RequireSQLinfo;
import com.example.sunlianglong.PHPServer.WriteIntoSQL;
import com.example.sunlianglong.messageServer.Message;
import com.example.sunlianglong.messageServer.MessageAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeimingdanActivity extends AppCompatActivity {
    private String my_ip;
    private String  heimingdan_list;
    private ListView mListView;
    private int i;
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String delete_heimingdan_list="http://"+fangwen_ip+"/chatting/delete_heimingdan_list.php";
    private String Read_heimingdan_list = "http://"+fangwen_ip+"/chatting/read_heimingdan_list.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heimingdan);
        Intent intent = getIntent();
        my_ip = intent.getStringExtra("my_ip");
        initView_heimingdan();
    }
    public void initView_heimingdan(){
        final String[] heimingdan_ip = new String[100];
        final int[] count = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //得到json解析出来的message_list_info表
                    //String ip_adds,  String up_name,  String up_name_key
                    RequireSQLinfo read_message_list = new RequireSQLinfo();
                    heimingdan_list = read_message_list.SendRequest(Read_heimingdan_list,"my_ip",my_ip);
                    //在线程中判断是否得到成功从服务器得到数据
                    Log.i("jsonString1",heimingdan_list);
                    JSONArray jsonArray = new JSONArray(heimingdan_list);
                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        heimingdan_ip[i] = jsonObject.optString("his_netname");
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

        final String[] a = new String[count[0]];
        for (int k=0;k<count[0];k++){
            a[k] = heimingdan_ip[k];
        }

        mListView = (ListView) findViewById(R.id.heimingdan_list);
        mListView.setAdapter(new ArrayAdapter<>(HeimingdanActivity.this, android.R.layout.simple_list_item_1,a));
        mListView.setTextFilterEnabled(true);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(HeimingdanActivity.this);
                builder.setMessage("确定移除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //移除黑名单的中的那一项
                        String[] strings = new String[2];
                        strings[0]=my_ip;strings[1]=a[position];
                        String[] php_names = new String[2];
                        php_names[0]="my_ip";php_names[1]="his_ip";
                        WriteIntoSQL w = new WriteIntoSQL();
                        w.writetoSQL(php_names,strings,delete_heimingdan_list);
                        Toast.makeText(HeimingdanActivity.this,"移除黑名单成功！",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
                return false;
            }
        });
    }
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initView_heimingdan();
    }

}
