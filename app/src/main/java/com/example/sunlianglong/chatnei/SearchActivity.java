package com.example.sunlianglong.chatnei;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import com.example.sunlianglong.messageServer.SqlMannger;


public class SearchActivity extends AppCompatActivity {
    private SearchView mSearchView;
    private ListView mListView;
    private String[] name;
    private String my_ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent = getIntent();
        my_ip = intent.getStringExtra("uesrname");
        SqlMannger sqlMannger = new SqlMannger(SearchActivity.this);
        name = sqlMannger.get_netname();
        mSearchView = (SearchView) findViewById(R.id.search_friends);
        mListView = (ListView) findViewById(R.id.search_list);
        mListView.setAdapter(new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1,name));
        mListView.setTextFilterEnabled(true);

        //更改搜索框效果
        if(mSearchView==null){
            return;
        }
        else{
            //获取到TextView的ID
            int id = mSearchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
            //获取到TextView的控件
            TextView textView = (TextView) mSearchView.findViewById(id);
            //设置字体大小为14sp
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//14sp
            //设置字体颜色
            textView.setTextColor(SearchActivity.this.getResources().getColor(R.color.myBlue));
            //设置提示文字颜色
            textView.setHintTextColor(SearchActivity.this.getResources().getColor(R.color.myBlue));
        }

        // 设置搜索文本监听
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 当点击搜索按钮时触发该方法
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            // 当搜索内容改变时触发该方法
            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText)){
                    mListView.setFilterText(newText);
                }else{
                    mListView.clearTextFilter();
                }
                return false;
            }
        });
        //添加点击事件
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                System.out.println("点击第"+arg2+"个项目");
                Intent intent = new Intent(SearchActivity.this,MainChatActivity.class);
                intent.putExtra("netname",name[arg2]);
                intent.putExtra("my_ip",my_ip);
                startActivity(intent);
            }
        });
    }
}
