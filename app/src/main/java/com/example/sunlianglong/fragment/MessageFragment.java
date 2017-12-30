package com.example.sunlianglong.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sunlianglong.PHPServer.WriteIntoSQL;
import com.example.sunlianglong.chatnei.MainActivity;
import com.example.sunlianglong.chatnei.MainChatActivity;
import com.example.sunlianglong.messageServer.Message;
import com.example.sunlianglong.messageServer.MessageAdapter;
import com.example.sunlianglong.PHPServer.RequireSQLinfo;
import com.example.sunlianglong.chatnei.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {
    private List<Message> messageList = new ArrayList<>();
    private int i;
    private String  message_list;
    private String[][]  message_list2;
    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String delete_message_list="http://"+fangwen_ip+"/chatting/delete_message_list.php";
    private String read_url="http://"+fangwen_ip+"/chatting/read_message_list.php";
    //定义一个列表集合
    List<Map<String,Object>> listItems;
    Map<String, Object> map;
    //接受MainActivity传过来的值：my_ip
    private String my_ip;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        my_ip = ((MainActivity) activity).get_ip();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_message,null);

        //不重复添加listView的Item
        messageList.clear();
        initView_message(v);
        //返回整个布局
        return  v;
    }

    public void initView_message(View v){
         final String[] message_friendip = new String[100];
         final String[] message_netname = new String[100];
         final String[] message_touxiangID = new String[100];
         final String[] message_state = new String[100];
         final String[] message_geqian = new String[100];
         final String[][] message = new String[6][100];
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //得到json解析出来的message_list_info表
                    //String ip_adds,  String up_name,  String up_name_key
                    RequireSQLinfo read_message_list = new RequireSQLinfo();
                    message_list = read_message_list.SendRequest(read_url,"my_ip",my_ip);
                    //在线程中判断是否得到成功从服务器得到数据
                    Log.i("jsonString1",message_list);
                    JSONArray jsonArray = new JSONArray(message_list);
                    for (i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        message_friendip[i] = jsonObject.optString("friend_ip");
                        message_netname[i] = jsonObject.optString("netname");
                        message_touxiangID[i] = jsonObject.optString("touxiang_id");
                        message_state[i] = jsonObject.optString("state");
                        message_geqian[i] = jsonObject.optString("geqian");
                        message[0] = message_friendip;
                        message[1] = message_netname;
                        message[2] = message_touxiangID;
                        message[3] = message_state;
                        message[4] = message_geqian;
                        message[5][0] = String.valueOf(i+1);

                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        //睡眠等待获取网络数据成功 0.3s
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //打印一下看看message_list数量的输出
        System.out.println("message_listttttttttt"+ message[5][0]);
        initMessage(message);

        final MessageAdapter adapter = new MessageAdapter(getActivity(), R.layout.message_list_item, messageList);
        final ListView listView = (ListView)v.findViewById(R.id.listView_message);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //ListView的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Message messa = messageList.get(position);
                Intent intent = new Intent(getActivity(), MainChatActivity.class);
                intent.putExtra("my_ip",my_ip);
                intent.putExtra("netname",messa.getName());
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messageList.remove(position);
                        adapter.notifyDataSetChanged();

                        //删掉message_list中的那一项
                        String[] strings = new String[2];
                        strings[0]=my_ip;strings[1]=message[0][position];
                        String[] php_names = new String[2];
                        php_names[0]="ip";php_names[1]="friend_ip";
                        System.out.println("ip"+my_ip+" "+"friend_ip"+message[0][position]);
                        WriteIntoSQL w = new WriteIntoSQL();
                        w.writetoSQL(php_names,strings,delete_message_list);
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

    //便利得到message_list的数据
    private void initMessage(String[][] message) {
        int count=0;
        for(int i=0;i<message[0].length;i++){

            if(message[0][i]==null){
                break;
            }else {
                count++;
            }
        }
        for(int j=0;j<count;j++){
            System.out.println("count"+count);
            Message a = new Message(message[1][j],Integer.parseInt(message[2][j]),message[4][j]);
            messageList.add(a);
        }
    }
}

