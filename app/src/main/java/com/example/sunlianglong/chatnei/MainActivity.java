package com.example.sunlianglong.chatnei;
/*
* 项目需求：
1、类似QQ，但只限于内网人员，即实现一款局域网内的即时通讯软件。
2、实现常用的功能，如登录、注册、添加/删除联系人、修改联系人信息、联系人聊天、加入黑名单等。
3、实现分组功能，并实现搜索用户功能（可通过输入用户名、组名、IP等来查找我的好友.对于汉字可输入拼音或汉字的第一个拼音来查找）。
4、实现个性头像、个性签名等功能。
5、实现文件传输功能（文件上传下载）。
备注（可以实现以下功能作为加分项）：
1、实现联系人或联系组权限功能，即对某特定联系人或联系组进行隐身功能。
2、支持截屏，支持GIF动画。
3、实现群聊天功能。
4、语音聊天功能,可以语音对话。
功能越完善得分越高，有创新功能更好。
* */
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;;
import com.example.sunlianglong.messageServer.Message;
import com.example.sunlianglong.fragment.DongtaiFragment;
import com.example.sunlianglong.fragment.FriendFragment;
import com.example.sunlianglong.fragment.MessageFragment;
import com.example.sunlianglong.messageServer.MySQL;
import com.example.sunlianglong.messageServer.SqlMannger;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String username;
    //主界面菜单
    private ImageView listImage;
    private ImageView friendsImage;
    private ImageView funImage;
    private ImageView touxiang;
    private TextView textView_ip;
    private TextView textView_geqian;
    private Button test;
    private MySQL mySQL;
    private int count=0;
    //fragement
    private Fragment myFragment;
    private MessageFragment messageFragment = new MessageFragment();
    private FriendFragment friendFragment = new FriendFragment();
    private DongtaiFragment dongtaiFragment = new DongtaiFragment();

    //ListView
    private List<Message> fruitList = new ArrayList<Message>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //获取用户名ip
        final Intent intent = getIntent();
        username = intent.getStringExtra("username");
        //主界面菜单id
        listImage = (ImageView)findViewById(R.id.listImage);
        friendsImage = (ImageView)findViewById(R.id.friendsImage);
        funImage = (ImageView)findViewById(R.id.funImage);

        //默认的Fragment
        setDefaultFragment();

        //主菜单界面 菜单切换
        LinearLayout listMenu = (LinearLayout)findViewById(R.id.listMenu);
        LinearLayout funMenu = (LinearLayout)findViewById(R.id.funMenu);
        LinearLayout infoMenu = (LinearLayout)findViewById(R.id.infoMenu);
        listMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedMenu(listImage);
            }
        });
        funMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedMenu(friendsImage);
            }
        });
        infoMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelectedMenu(funImage);
            }
        });

        //将菜单栏的头像 ip 个签放在一个函数里
        update_headView();
        //头像点击 进入个人资料
        touxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,PersonalActivity.class);
                intent1.putExtra("username",username);
                startActivity(intent1);
            }
        });
        //本地存储flag

        SharedPreferences sp = getSharedPreferences("flag_insert", Context.MODE_PRIVATE);
        String  flag1 = sp.getString("flag1","");
        if (flag1==""){
            mySQL = new MySQL(MainActivity.this);
            SqlMannger sqlMannger = new SqlMannger(MainActivity.this);
            sqlMannger.InsertFirst();
            sqlMannger.InsertFirst_two();
            System.out.println("success useing insert!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
            SharedPreferences sp1 = getSharedPreferences("flag_insert", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp1.edit();
            editor.putString("flag1","0");
            editor.apply();
        }
        SqlMannger sqlMannger2 = new SqlMannger(MainActivity.this);
        count = sqlMannger2.selectSQL("我的好友");
        System.out.println(count);


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            Intent intent = new Intent(MainActivity.this,MainChatActivity.class);
//            intent.putExtra("username",username);
//            startActivity(intent);
        } else if (id == R.id.xixixi) {

        } else if (id == R.id.personal_data) {
            Intent intent1 = new Intent(MainActivity.this,PersonalActivity.class);
            intent1.putExtra("username",username);
            startActivity(intent1);
        } else if (id == R.id.set) {
//            Intent intent1 = new Intent(MainActivity.this,SearchActivity.class);
//            intent1.putExtra("username",username);
//            startActivity(intent1);
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.shit) {
            Intent intent = new Intent(MainActivity.this,HeimingdanActivity.class);
            intent.putExtra("my_ip",username);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //设置菜单选中状态
    private void setSelectedMenu(ImageView selected){
        if(selected == listImage)
        {
            listImage.setImageResource(R.drawable.skin_tab_icon_conversation_selected);
            setFragment(messageFragment);

        }
        else
            listImage.setImageResource(R.drawable.skin_tab_icon_conversation_normal);

        if(selected == friendsImage)
        {
            friendsImage.setImageResource(R.drawable.skin_tab_icon_contact_selected);
            setFragment(friendFragment);
        }
        else
            friendsImage.setImageResource(R.drawable.skin_tab_icon_contact_normal);

        if(selected == funImage)
        {
            funImage.setImageResource(R.drawable.skin_tab_icon_plugin_selected);
            setFragment(dongtaiFragment);
        }
        else
            funImage.setImageResource(R.drawable.skin_tab_icon_plugin_normal);
    }

    protected void onResume(){
        super.onResume();
        update_headView();

    }
    //更新headView
    public void update_headView(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //实时更改菜单栏headView信息
        SharedPreferences sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);//网名
        String Signature = sp.getString("Signature","");//个签
        //1.获取头布局文件
        View headerView = navigationView.getHeaderView(0);
        touxiang = (ImageView) headerView.findViewById(R.id.imageView_touxiang);
        textView_ip = (TextView)headerView.findViewById(R.id.textView_ip);
        textView_geqian = (TextView)headerView.findViewById(R.id.textView_geqian);
        //2.更改
        int imageId = sp.getInt("userImageId",0);
        touxiang.setImageResource(imageId);
        textView_ip.setText(username);
        textView_geqian.setText(Signature);
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_content,messageFragment);
        transaction.commit();
    }
    //设置Fragment
    private void setFragment(Fragment fragment){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.id_content,fragment);
        myFragment = fragment;//传递参数值，显示的fragment
        transaction.commit();
    }
    //传值给Fragment相关信息。username
    public String get_ip(){
        return username;
    }
    public int get_friends_count(){
        return count;
    }

}
