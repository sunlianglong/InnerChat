package com.example.sunlianglong.chatnei;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import com.example.sunlianglong.PHPServer.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class RegisterActivity extends BaseActivity {

    //声明接口地址
    //private String url = "http://172.33.18.108:81/chat/register.php";//2017.10.10 14：12 CQUPT的IP地址

    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String register_url = "http://"+fangwen_ip+"/chatting/register.php";
    private String a;
    private String b;
    private String c;
    private String username;
    private EditText et1;
    private EditText et2;
    private EditText et3;
    private Button btn;
    private String jsonString;
    private String jsonStr;

    ArrayList<String> flag_arraylist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et1 = (EditText) findViewById(R.id.username_edit);
        et2 = (EditText) findViewById(R.id.password_edit);
        btn = (Button) findViewById(R.id.signin_button);
        et3 = (EditText) findViewById(R.id.password_edit_sure);
        //获取用户名
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        System.out.println(username);
        et1.setText(username);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //获取输入框中的内容
                a = et1.getText().toString();
                int len = a.length();
                b = et2.getText().toString();
                c = et3.getText().toString();
                if(!a.equals(username)){
                    Toast.makeText(RegisterActivity.this,"输入的IP与本机IP不符合",Toast.LENGTH_SHORT).show();
                }else {
                    if(!b.equals(c)){
                        Toast.makeText(RegisterActivity.this,"您前后两次输入的密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                    }else {
                        //替换键值对，这里的键必须和接口中post传递的键一致
                        params.add(new BasicNameValuePair("name", a));
                        params.add(new BasicNameValuePair("password", b));

                        JSONParser jsonParser = new JSONParser();
                        try{
                            JSONObject json = jsonParser.makeHttpRequest(register_url,"POST", params);
                            jsonStr = json.toString();
                            Log.v("uploadsucceed", a);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        if (jsonStr.equals("{\"success\":1}")){
                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            intent.putExtra("username",username);
                            startActivity(intent);
                        }else{
                            try{
                                JSONObject json = jsonParser.makeHttpRequest(register_url,"POST", params);
                                jsonStr = json.toString();
                                Log.v("uploadsucceed", a);
                            }catch(Exception e){
                                e.printStackTrace();
                            }

                            if (jsonStr.equals("{\"success\":1}")){
                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.putExtra("username",username);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                }
            }
        });
        //下面的代码是必须加上的，具体的意义还需要去探索
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()   // or .detectAll() for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }
}