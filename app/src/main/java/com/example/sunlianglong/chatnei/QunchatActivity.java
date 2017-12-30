package com.example.sunlianglong.chatnei;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunlianglong.QunliaoSrever.ChatMsgEntity;
import com.example.sunlianglong.QunliaoSrever.ChatMsgViewAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 雪松 on 2017/12/19.
 */

public class QunchatActivity extends Activity {
    int flag;
    TextView show;
    EditText input;
    Button send;
    Handler handler;
    ClientThread clientThread;
    String my_ip;
    public static List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    public static ListView mListView;
    private ChatMsgViewAdapter mAdapter;
    //显示右边气泡内的文本
    public void you(String name,String text){
        ChatMsgEntity entity = new ChatMsgEntity();
        entity.setDate(getDate());
        entity.setName(name);
        entity.setMsgType(false);
        entity.setText(text);
        mDataArrays.add(entity);
        mListView.setSelection(mListView.getCount() - 1);

    }
    //显示左边气泡内的文本
    public void zou(String name,String text){
        ChatMsgEntity entity1 = new ChatMsgEntity();
        entity1.setDate(getDate());
        entity1.setName(name);
        entity1.setMsgType(true);
        entity1.setText(text);
        mDataArrays.add(entity1);
        mListView.setSelection(mListView.getCount() - 1);

    }
    //获取日期
    private String getDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));
        StringBuffer sbBuffer = new StringBuffer();
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":" + mins);
        return sbBuffer.toString();
    }
    //初始化要显示的数据：
    private void initData(String name,String text) {
            ChatMsgEntity entity = new ChatMsgEntity();
            entity.setDate(this.getDate());      //这儿多研究
            entity.setName(name);
            entity.setMsgType(true);
            entity.setText(text);
            mDataArrays.add(entity);

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_main);
        input = (EditText) findViewById(R.id.input);
        send = (Button) findViewById(R.id.send);
        show = (TextView) findViewById(R.id.show);
        //获取对方的昵称
        Intent intent = getIntent();
        my_ip = intent.getStringExtra("my_ip");
        //加载视图
        mListView = (ListView) findViewById(R.id.chat_list);
        initData("系统提示","您已进入群聊");  //初始化数据

        handler = new Handler() {
            public void handleMessage(Message msg) {
                    if(msg.what == 0x123){
                        if(flag == 1)
                        {
                            flag = 0;
                        }else {
                            zou(my_ip, msg.obj.toString());
                        }
                    }
            }
        };
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message msg = new Message();
                    msg.what = 0x345;
                    msg.obj =  input.getText().toString();
                    you(my_ip, msg.obj.toString());
                    flag = 1;
                    clientThread.revHandler.sendMessage(msg);
                    input.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}



class ClientThread implements Runnable{
    private Socket s;
    private Handler handler;
    public Handler revHandler;
    BufferedReader br = null;
    OutputStream os = null;
    public ClientThread(Handler handler){
        this.handler = handler;
    }
    public void run(){
        try {
            s = new Socket("123.206.17.117", 30000);
            //s = new Socket("172.18.113.140", 30000);
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            os = s.getOutputStream();
            new Thread() {
                public void run() {
                    String content = null;
                    try {
                        while ((content = br.readLine()) != null) {
                            Message msg = new Message();
                            msg.what = 0x123;
                            msg.obj = content;
                            handler.sendMessage(msg);
                        }
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                }
            }.start();
            Looper.prepare();
            revHandler = new Handler() {

                public void handleMessage(Message msg) {
                    if (msg.what == 0x345) {
                        try {
                            os.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }
            };
            Looper.loop();
        }catch (SocketTimeoutException e1){
            System.out.println("网络连接超时");
        }catch (Exception e){
            e.getStackTrace();
        }
    }
}
