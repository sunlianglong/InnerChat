package com.example.sunlianglong.chatnei;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.view.View.OnClickListener;

import com.example.sunlianglong.PHPServer.WriteIntoSQL;
import com.example.sunlianglong.messageServer.SqlMannger;

import java.util.List;

public class Friends_data_opoActivity extends AppCompatActivity {
    private String netname;
    private String my_ip;
    private TextView back_add;
    private ImageView back;
    private TextView fenzu;
    private TextView beizhu;
    private TextView netname_textview;
    private TextView his_ip_textview;
    private ImageView beizhu_image;
    private  ImageView fenzu_image;
    private RadioOnClick radioOnClick = new RadioOnClick(1);
    private Button delete_friend_button;
    private Button add_shit_button;
    private ListView areaRadioListView;
    private TextView more;

    //private String fangwen_ip="172.33.25.182:81";
    private String fangwen_ip="123.206.17.117";
    private String URL_write_to_heimingdan = "http://"+fangwen_ip+"/chatting/write_into_heimingdan_list.php";
    private String delete_message_list="http://"+fangwen_ip+"/chatting/delete_message_list.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_data_opo);
        //获取对方的昵称
        Intent intent = getIntent();
        netname = intent.getStringExtra("netname");
        my_ip = intent.getStringExtra("my_ip");
        back_add = (TextView)findViewById(R.id.back_to_chatting);
        netname_textview = (TextView)findViewById(R.id.netname);
        his_ip_textview = (TextView)findViewById(R.id.ip);

        fenzu = (TextView)findViewById(R.id.fenzu);
        beizhu = (TextView)findViewById(R.id.beizhu);
        beizhu_image = (ImageView)findViewById(R.id.beizhu_image);
        fenzu_image = (ImageView)findViewById(R.id.fenzu_image);
        delete_friend_button = (Button)findViewById(R.id.delete_friend_button);
        add_shit_button = (Button)findViewById(R.id.add_shit_button);
        more = (TextView)findViewById(R.id.friends_more);
        SqlMannger sqlMannger = new SqlMannger(Friends_data_opoActivity.this);

        final String[] s = sqlMannger.look_fenzu_beizhu(netname);
        fenzu.setText(s[0]);
        beizhu.setText(s[1]);
        netname_textview.setText(netname);
        his_ip_textview.setText(s[2]);

        //点击返回按钮
        back_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back = (ImageView)findViewById(R.id.back_to_chat);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //点击更多，获取更多
        more.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Friends_data_opoActivity.this,More_dataActivity.class);
                intent1.putExtra("his_ip", his_ip_textview.getText().toString());
                startActivity(intent1);
            }
        });
        //点击备注跳转
        beizhu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Friends_data_opoActivity.this,Change_beizhuActivity.class);
                intent1.putExtra("netname",netname);
                intent1.putExtra("beizhu",s[1]);
                startActivity(intent1);
            }
        });
        beizhu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Friends_data_opoActivity.this,Change_beizhuActivity.class);
                intent2.putExtra("netname",netname);
                intent2.putExtra("beizhu",s[1]);
                startActivity(intent2);
            }
        });
        //点击分组
        fenzu.setOnClickListener(new RadioClickListener());
        fenzu_image.setOnClickListener(new RadioClickListener());

        //删除好友
        delete_friend_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SqlMannger s = new SqlMannger(Friends_data_opoActivity.this);
                s.delete_friend(his_ip_textview.getText().toString(),fenzu.getText().toString());
                Toast.makeText(Friends_data_opoActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
                //删掉message_list中的那一项
                String[] strings = new String[2];
                strings[0]=my_ip;strings[1]=his_ip_textview.getText().toString();
                String[] php_names = new String[2];
                php_names[0]="ip";php_names[1]="friend_ip";
                WriteIntoSQL w1 = new WriteIntoSQL();
                w1.writetoSQL(php_names,strings,delete_message_list);
                //干掉MainActivity页面
                MainChatActivity.a.finish();
                finish();
            }
        });

        //加入黑名单
        add_shit_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先删除好友
                SqlMannger s = new SqlMannger(Friends_data_opoActivity.this);
                s.delete_friend(his_ip_textview.getText().toString(),fenzu.getText().toString());
                //删掉message_list中的那一项
                String[] strings = new String[2];
                strings[0]=my_ip;strings[1]=his_ip_textview.getText().toString();
                String[] php_names = new String[2];
                php_names[0]="ip";php_names[1]="friend_ip";
                WriteIntoSQL w1 = new WriteIntoSQL();
                w1.writetoSQL(php_names,strings,delete_message_list);

                //再加入黑名单
                String[] php_name = new String[3];
                php_name[0]="my_ip";php_name[1]="his_ip";php_name[2]="his_netname";
                String[] value_name = new String[3];
                value_name[0]=my_ip;value_name[1]=his_ip_textview.getText().toString();
                value_name[2]=netname_textview.getText().toString();
                WriteIntoSQL w2 = new WriteIntoSQL();
                w2.writetoSQL(php_name,value_name,URL_write_to_heimingdan);

                Toast.makeText(Friends_data_opoActivity.this,"加入黑名单成功！",Toast.LENGTH_SHORT).show();
                //干掉MainActivity页面
                MainChatActivity.a.finish();
                finish();
            }
        });
    }
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }
    public void initview(){
        SqlMannger sqlMannger = new SqlMannger(Friends_data_opoActivity.this);
        final String[] s = sqlMannger.look_fenzu_beizhu(netname);
        fenzu.setText(s[0]);
        beizhu.setText(s[1]);
        netname_textview.setText(netname);
        his_ip_textview.setText(s[2]);
    }
    /**
     * 单选弹出菜单窗口
     * @author long
     *
     */
    class RadioClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            AlertDialog ad =new AlertDialog.Builder(Friends_data_opoActivity.this).setTitle("选择要更换的分组")
                    .setSingleChoiceItems(get_fenzu_info(),radioOnClick.getIndex(),radioOnClick).create();
            areaRadioListView=ad.getListView();
            ad.show();
        }
    }
    class RadioOnClick implements DialogInterface.OnClickListener{
        private int index;

        public RadioOnClick(int index){
            this.index = index;
        }
        public void setIndex(int index){
            this.index=index;
        }
        public int getIndex(){
            return index;
        }

        public void onClick(DialogInterface dialog, int whichButton){
            setIndex(whichButton);
            String[] fenzu_info = get_fenzu_info();
            SqlMannger s2 = new SqlMannger(Friends_data_opoActivity.this);
            s2.ChangeFenzu(fenzu_info[index],his_ip_textview.getText().toString(),fenzu.getText().toString());
            Toast.makeText(Friends_data_opoActivity.this, "已将其移至 "+ fenzu_info[index]+" 分组", Toast.LENGTH_LONG).show();
            initview();
            dialog.dismiss();
        }
    }

    public String[] get_fenzu_info(){
        SqlMannger sqlMannger = new SqlMannger(Friends_data_opoActivity.this);
        //获取分组的数目以及名称
        String[][] fenzu_detail = sqlMannger.Look_Fenzu();
        int fenzu_num=0;
        for (int a = 0; a < fenzu_detail.length; a++) {//获取行的长度
            fenzu_num++;//分组数目
        }
        //设置组视图的显示文字
        final String[] category = new String[fenzu_num];
        for (int i =0;i<fenzu_num;i++){
            category[i] = fenzu_detail[i][0];
        }
        return category;
    }
}
