package com.example.sunlianglong.PHPServer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun liang long on 2017/10/24.
 */

public class WriteIntoSQL {

    public void writetoSQL(final String[] php_name, final String[] string, final String ip_address){

        new Thread(new Runnable() {
            @Override
            public void run() {
                int string_length = php_name.length;
                //上传个人资料到数据库
                List<NameValuePair> paramss = new ArrayList<NameValuePair>();
                //替换键值对，这里的键必须和接口中post传递的键一致
                for (int a = 0;a<string_length;a++){
                    paramss.add(new BasicNameValuePair(php_name[a],string[a]));
                }
                JSONParser jsonParser = new JSONParser();
                try {
                    jsonParser.makeHttpRequest(ip_address,"POST", paramss);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
