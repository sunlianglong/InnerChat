package com.example.sunlianglong.chatnei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.example.sunlianglong.PHPServer.WriteIntoSQL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SetHeadImageActivity extends AppCompatActivity {
    private int[] imageIds = new int[]{
            R.drawable.dml,
            R.drawable.dmk,
            R.drawable.dmn,
            R.drawable.dmm,
            R.drawable.head1,
            R.drawable.head2,
            R.drawable.head3,
            R.drawable.head4,
            R.drawable.head5,
            R.drawable.head6,
            R.drawable.head7,
            R.drawable.head8
    };
    private String ip_username;
    private String imageId;
    private GridView myImageGridView;
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String ip_ChangeImage = "http://"+fangwen_ip+"/chatting/ChangeImage.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_headimage);
        //获取用户名：ip
        Intent intent = getIntent();
        ip_username = intent.getStringExtra("username");

        myImageGridView = (GridView)findViewById(R.id.myImageGridView);
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<imageIds.length;i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("headImage",imageIds[i]);
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.item_gridview_image,new String[]{"headImage"},new int[]{R.id.headImage});
        myImageGridView.setAdapter(adapter);

        myImageGridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("userImageId",imageIds[position]);
                editor.commit();

                //将个人信息写进数据库
                String[] strings = new String[2];
                strings[0] = ip_username; strings[1] = String.valueOf(imageIds[position]);
                System.out.println("0000000000"+strings[0]+"image:"+strings[1]);
                String[] php_names = new String[2];
                php_names[0] = "ip"; php_names[1] = "image";
                WriteIntoSQL w = new WriteIntoSQL();
                w.writetoSQL(php_names,strings,ip_ChangeImage);

                finish();
                overridePendingTransition(R.anim.stay_in,R.anim.slide_out_down);
            }
        });
    }
}
