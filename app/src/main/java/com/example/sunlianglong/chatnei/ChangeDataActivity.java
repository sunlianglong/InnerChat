package com.example.sunlianglong.chatnei;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sunlianglong.PHPServer.JSONParser;
import com.example.sunlianglong.PHPServer.WriteIntoSQL;
import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChangeDataActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText netname;
    private EditText home_data;
    private EditText signature;
    private EditText school_data;
    private EditText class_data;
    private EditText birthday_data;
    private EditText phone_data;
    private EditText email_data;
    private EditText sex_data;
    private Button backButton;
    private Button changeButton;
    private String ip_username;
    private int ImageId;
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String ip_UpPersonalData = "http://"+fangwen_ip+"/chatting/UpPersonalData.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changedata);
        //获取用户名：ip
        Intent intent = getIntent();
        ip_username = intent.getStringExtra("username");
        ImageId = intent.getIntExtra("imageId",0);
        System.out.println(ImageId);


        netname = (EditText)findViewById(R.id.netname_change);
        signature = (EditText)findViewById(R.id.sign_change);
        home_data = (EditText)findViewById(R.id.home_change);
        school_data = (EditText)findViewById(R.id.school_change);
        class_data = (EditText)findViewById(R.id.class_change);
        birthday_data = (EditText)findViewById(R.id.birthday_change);
        phone_data = (EditText)findViewById(R.id.phone_change);
        email_data = (EditText)findViewById(R.id.email_change);
        sex_data = (EditText)findViewById(R.id.sex_change);
        backButton = (Button)findViewById(R.id.backBtn);
        changeButton = (Button)findViewById(R.id.change_data);
        backButton.setOnClickListener(this);
        changeButton.setOnClickListener(this);

        //SharedPreferences文本存储
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
        netname.setText(Netname);
        signature.setText(Signature);
        home_data.setText(Home);
        school_data.setText(School);
        class_data.setText(Class);
        birthday_data.setText(Birthday);
        phone_data.setText(Phone);
        email_data.setText(Email);
        sex_data.setText(Sex);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_data:
                //得到昵称，个签，故乡，学校，班级等信息
                final String netname_string = netname.getText().toString();
                final String signature_string = signature.getText().toString();
                final String home_string = home_data.getText().toString();
                final String school_string = school_data.getText().toString();
                final String class_string = class_data.getText().toString();
                final String touxiang_string = String.valueOf(ImageId);
                final String phone_string = phone_data.getText().toString();
                final String birthday_string = birthday_data.getText().toString();
                final String email_string = email_data.getText().toString();
                final String sex_string = sex_data.getText().toString();
                if(netname_string.equals("")) {
                    Toast.makeText(this, "网名不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                //本地存储个人资料
                SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Netname", netname_string);
                editor.putString("Signature", signature_string);
                editor.putString("Home", home_string);
                editor.putString("School", school_string);
                editor.putString("Class", class_string);
                editor.putString("Touxiang", touxiang_string);
                editor.putString("Birthday", birthday_string);
                editor.putString("Phone", phone_string);
                editor.putString("Email", email_string);
                editor.putString("Sex", sex_string);
                editor.apply();

                //将个人信息写进数据库
                String[] strings = new String[10];
                strings[0] = ip_username;strings[1] = netname_string;strings[2] = signature_string;
                strings[3] = home_string;strings[4] = school_string;strings[5] = class_string;
                strings[6] = sex_string;strings[7] = phone_string;strings[8] = email_string;strings[9] = birthday_string;
                String[] php_names = new String[10];
                php_names[0] = "ip_address";php_names[1] = "netname";php_names[2] = "geqian_data";
                php_names[3] = "home_data";php_names[4] = "school_data";php_names[5] = "class_data";
                php_names[6] = "sex_data";php_names[7] = "phone_data";php_names[8] = "email_data";php_names[9] = "birthday_data";
                WriteIntoSQL w = new WriteIntoSQL();
                w.writetoSQL(php_names,strings,ip_UpPersonalData);


                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
                overridePendingTransition(R.anim.stay_in,R.anim.slide_out_down);
            case R.id.backBtn:
                finish();
                overridePendingTransition(R.anim.stay_in,R.anim.slide_out_down);
                break;
            default:
        }
    }
}
