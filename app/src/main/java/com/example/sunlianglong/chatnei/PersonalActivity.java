package com.example.sunlianglong.chatnei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.suitebuilder.TestMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import org.w3c.dom.Text;

public class PersonalActivity extends AppCompatActivity {
    private static final String TAG = PersonalActivity.class.getSimpleName();
    private Button button_change_touxiang;
    private Button button_change_data;
    private TextView userName;
    private TextView hostIp;
    private TextView signature;
    private TextView home;
    private TextView school;
    private TextView class_name;
    private String ip_username;
    private ImageView touxiang_image;
    private TextView sex;
    private TextView birthday;
    private TextView phone;
    private TextView email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_personal);
        //获取用户名：ip
        final Intent intent = getIntent();
        ip_username = intent.getStringExtra("username");

        userName = (TextView)findViewById(R.id.userName);
        hostIp = (TextView)findViewById(R.id.hostIp);
        signature = (TextView)findViewById(R.id.signature);
        home = (TextView)findViewById(R.id.home_data);
        school = (TextView)findViewById(R.id.school);
        class_name = (TextView)findViewById(R.id.class_name);
        touxiang_image = (ImageView)findViewById(R.id.userImage);
        sex = (TextView)findViewById(R.id.sex);
        birthday = (TextView)findViewById(R.id.birthday);
        phone = (TextView) findViewById(R.id.phone);
        email = (TextView)findViewById(R.id.email);


        //SharedPreferences存储个人资料
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String Netname = sp.getString("Netname","");
        String Signature = sp.getString("Signature","");
        String Home = sp.getString("Home","");
        String School = sp.getString("School","");
        String Class = sp.getString("Class","");
        String Birthday = sp.getString("Birthday","");
        String Phone = sp.getString("Phone","");
        String Email = sp.getString("Email","");
        String Sex = sp.getString("Sex","");
        userName.setText(Netname);
        signature.setText(Signature);
        hostIp.setText(ip_username);
        home.setText(Home);
        school.setText(School);
        class_name.setText(Class);
        birthday.setText(Birthday);
        phone.setText(Phone);
        email.setText(Email);
        sex.setText(Sex);

        //获取头像的id
        final int imageId = sp.getInt("userImageId",0);
        touxiang_image.setImageResource(imageId);

        button_change_data = (Button)findViewById(R.id.change_data_button);
        button_change_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到修改资料Activity
                Intent intent1 = new Intent(PersonalActivity.this,ChangeDataActivity.class);
                intent1.putExtra("username",ip_username);
                intent1.putExtra("imageId",imageId);
                startActivity(intent1);
            }
        });
        button_change_touxiang = (Button)findViewById(R.id.change_touxiang_button);
        button_change_touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到更换头像Activity
                Intent intent2 = new Intent(PersonalActivity.this,SetHeadImageActivity.class);
                intent2.putExtra("username",ip_username);
                intent2.putExtra("imageId",imageId);
                startActivity(intent2);
            }
        });
        touxiang_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到更换头像Activity
                Intent intent2 = new Intent(PersonalActivity.this,SetHeadImageActivity.class);
                intent2.putExtra("username",ip_username);
                intent2.putExtra("imageId",imageId);
                startActivity(intent2);
            }
        });
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //SharedPreferences存储个人资料
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String Netname = sp.getString("Netname","");
        String Signature = sp.getString("Signature","");
        String Home = sp.getString("Home","");
        String School = sp.getString("School","");
        String Class = sp.getString("Class","");
        String Birthday = sp.getString("Birthday","");
        String Phone = sp.getString("Phone","");
        String Email = sp.getString("Email","");
        String Sex = sp.getString("Sex","");
        userName.setText(Netname);
        signature.setText(Signature);
        hostIp.setText(ip_username);
        home.setText(Home);
        school.setText(School);
        class_name.setText(Class);
        int imageId = sp.getInt("userImageId",0);
        touxiang_image.setImageResource(imageId);
        birthday.setText(Birthday);
        phone.setText(Phone);
        email.setText(Email);
        sex.setText(Sex);
    }
}
