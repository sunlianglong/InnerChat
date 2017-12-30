package com.example.sunlianglong.PHPServer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by sun liang long on 2017/10/31.
 */

public class GetMessage {
    private int i;
    private String message_list;
    private String[] message_friendip = new String[100];
    private String[] message_netname = new String[100];
    private String[] message_touxiangID = new String[100];
    private String[] message_state = new String[100];
    private String[] message_group = new String[100];
    private String[][] message = new String[6][100];
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String read_url="http://"+fangwen_ip+"/chatting/read_message_list.php";
    public String[][] getMessage_list(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //String ip_adds,  String up_name,  String up_name_key
                    RequireSQLinfo read_message_list = new RequireSQLinfo();
                    message_list = read_message_list.SendRequest(read_url,"my_ip","10.0.2.15");
                    //在线程中判断是否得到成功从服务器得到数据
                    Log.i("jsonString1",message_list);
                    JSONArray jsonArray = new JSONArray(message_list);
                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        message_friendip[i] = jsonObject.optString("friend_ip");
                        message_netname[i] = jsonObject.optString("netname");
                        message_touxiangID[i] = jsonObject.optString("touxiang_id");
                        message_state[i] = jsonObject.optString("state");
                        message_group[i] = jsonObject.optString("group");
                        message[0] = message_friendip;
                        message[1] = message_netname;
                        message[2] = message_touxiangID;
                        message[3] = message_state;
                        message[4] = message_group;
                        message[5][0] = String.valueOf(i+1);
                        if(i==0){
                            //
                        }
                    }
                    System.out.println(message[5][0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return message;
    }
}
