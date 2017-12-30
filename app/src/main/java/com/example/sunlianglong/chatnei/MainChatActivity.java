package com.example.sunlianglong.chatnei;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sunlianglong.QunliaoSrever.ChatMsgEntity;
import com.example.sunlianglong.QunliaoSrever.ChatMsgViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainChatActivity extends AppCompatActivity {
    int flag;
    private String netname;
    private TextView chatTitleName;
    private ImageView friend_data_opo;
    private Button back_add;
    private String my_ip;
    public static MainChatActivity a = null;
    TextView show;
    EditText input;
    Button send;
    Handler handler;
    ClientThread clientThread;
    public static List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    public static ListView mListView;
    private ChatMsgViewAdapter mAdapter;

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

    //显示右边气泡内的文本
    public void you(String name, String text) {
        ChatMsgEntity entity = new ChatMsgEntity();
        entity.setDate(getDate());
        entity.setName(name);
        entity.setMsgType(false);
        entity.setText(text);
        mDataArrays.add(entity);
        mListView.setSelection(mListView.getCount() - 1);
    }
    //显示左边气泡内的文本
    public void zou(String name, String text) {
        ChatMsgEntity entity1 = new ChatMsgEntity();
        entity1.setDate(getDate());
        entity1.setName(name);
        entity1.setMsgType(true);
        entity1.setText(text);
        mDataArrays.add(entity1);
        mListView.setSelection(mListView.getCount() - 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);
        a=this;
        //获取对方的昵称
        final Intent intent = getIntent();
        netname = intent.getStringExtra("netname");
        my_ip = intent.getStringExtra("my_ip");
        chatTitleName = (TextView) findViewById(R.id.chatTitleName);
        chatTitleName.setText(netname);

        friend_data_opo = (ImageView) findViewById(R.id.friend_data_opo);
        friend_data_opo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainChatActivity.this, Friends_data_opoActivity.class);
                intent1.putExtra("netname", netname);
                intent1.putExtra("my_ip", my_ip);
                startActivity(intent1);
            }
        });


        //返回按钮
        back_add = (Button) findViewById(R.id.chatBack);
        back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //消息发送
        input = (EditText) findViewById(R.id.chatWithText);
        send = (Button) findViewById(R.id.chatSendBtn);
        show = (TextView) findViewById(R.id.show);
        //获取对方的昵称
        Intent intent2 = getIntent();
        my_ip = intent2.getStringExtra("my_ip");
        //加载视图
        mListView = (ListView) findViewById(R.id.chat_list);
        initData("系统提示","您已进入单聊模式");  //初始化数据

        handler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 0x123) {
                    if (flag == 1) {
                        flag = 0;
                    } else {
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
                    msg.obj = input.getText().toString();
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
