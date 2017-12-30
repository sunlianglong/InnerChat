package com.example.sunlianglong.chatnei;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
//获取本地ip的所需包
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.net.SocketException;
//-----------------------------------------------------//

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private EditText editText_user,editText_passwd;
    private Button button_login;
    private SharedPreferences sharedPreferences;
    private int retCode;
    private String user,passwd,result;
    private TextView register;
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String Login_URL = "http://"+fangwen_ip+"/chatting/login.php";
    private int FLAG = 1 ;
    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Log.e(TAG, s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                retCode = jsonObject.getInt("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (retCode == 1) {
                //访问数据库

                //将username传入下一个Activity
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("username",user);
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this,"密码错误，请重新输入",Toast.LENGTH_SHORT).show();
            }
        }
    };
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Log.e(TAG, volleyError.getMessage(), volleyError);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        button_login = (Button) findViewById(R.id.signin_button);
        editText_user = (EditText)findViewById(R.id.username_edit);
        editText_passwd = (EditText)findViewById(R.id.password_edit);

        final String string_ip =getIPAddress(this);
        System.out.println(string_ip);
        editText_user.setText(string_ip);
        register = (TextView)findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FLAG = 0;
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                intent.putExtra("username",string_ip);
                startActivity(intent);
            }
        });
        if(FLAG == 1){
            //显示已保存的用户名和密码
            sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
            String getName = sharedPreferences.getString("username", null);
            if (getName == null) {
                //Toast.makeText(LoginActivity.this,"没有保存用户信息，请重新输入!",Toast.LENGTH_SHORT).show();
            } else {
                editText_user.setText(sharedPreferences.getString("userName",null));
                editText_passwd.setText(sharedPreferences.getString("passWord",null));
            }
            button_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = editText_user.getText().toString();
                    passwd = editText_passwd.getText().toString();
                    if(!user.equals(string_ip))
                    {
                        Toast.makeText(LoginActivity.this,"您输入的IP与本机IP不符合！",Toast.LENGTH_SHORT).show();
                        return;
                    } else if (user.equals("") || passwd.equals("")) {
                        Toast.makeText(LoginActivity.this,"IP或密码不能为空，请重新输入!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    //StringRequest stringRequest = new StringRequest(Request.Method.POST,"http://172.33.18.108:81/dollar/login.php",listener,errorListener) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,Login_URL,listener,errorListener) {
                        @Override
                        protected Map<String,String> getParams() throws AuthFailureError {
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("username",user);
                            map.put("password",passwd);
                            return map;
                        }
                    };
                    requestQueue.add(stringRequest);
                }
            });
        }

    }

    //****************************获取本地ip地址***************************************************
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }
    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
    //***************************************************************************//
}

