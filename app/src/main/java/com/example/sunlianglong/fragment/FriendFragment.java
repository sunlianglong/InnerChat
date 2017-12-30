package com.example.sunlianglong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.sunlianglong.chatnei.AddFriendsActivity;
import com.example.sunlianglong.chatnei.MainActivity;
import com.example.sunlianglong.chatnei.MainChatActivity;
import com.example.sunlianglong.chatnei.R;
import com.example.sunlianglong.chatnei.SearchActivity;
import com.example.sunlianglong.messageServer.MySQL;
import com.example.sunlianglong.messageServer.SqlMannger;

public class FriendFragment extends Fragment {
    ExpandableListView Friend_List;
    private MySQL mySQL;
    private ImageView image_addFriends;
    private Button searchView;
    //接受MainActivity传过来的值：my_ip
    private String my_ip;
    private int friends_count;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        my_ip = ((MainActivity) activity).get_ip();
        friends_count = ((MainActivity) activity).get_friends_count();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends,null);
        Friend_List=(ExpandableListView)v.findViewById(R.id.friend_list);

        initview();
        image_addFriends = (ImageView)v.findViewById(R.id.friends_add);
        image_addFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddFriendsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("my_ip",my_ip);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        searchView = (Button)v.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("my_ip",my_ip);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return v;
    }

    public void initview(){
        //mySQL = new MySQL(getActivity());
        //得到分组情况:一个二维数组
        /*******************************************************************************************/
        SqlMannger sqlMannger = new SqlMannger(getActivity());
        String[][] fenzu_detail = sqlMannger.Look_Fenzu();
        //fenzu_num=0如果在开始类的定义会逐渐增加 ：报错
        int fenzu_num=0;
        for (int a = 0; a < fenzu_detail.length; a++) {//获取行的长度
            fenzu_num++;//分组数目
        }
        System.out.println("fenzu_detail.length:"+fenzu_num);
        int[] each_fenzu_num = new int[fenzu_num];
        for (int a = 0; a < fenzu_detail.length; a++) {//获取行的长度
            each_fenzu_num[a] = Integer.parseInt(fenzu_detail[a][1]);//每个分组的数目
        }
        System.out.println("each_fenzu_num[0]:"+each_fenzu_num[0]);

        /****************************************************************************************/




        //设置组视图的图片
        final int[] logos = new int[fenzu_num];
        for (int i =0;i<fenzu_num;i++){
            logos[i] = R.drawable.dmm;
        }
        //设置组视图的显示文字
        final String[] category = new String[fenzu_num];
        for (int i =0;i<fenzu_num;i++){
            category[i] = fenzu_detail[i][0];
        }
        //子视图显示图片
        final int[][] sublogos = new int[fenzu_num][];
        for (int i=0;i<fenzu_num;i++){
            String[][] friends_info1 = sqlMannger.get_friends_info(fenzu_detail[i][0],Integer.parseInt(fenzu_detail[i][1]));
            sublogos[i] = new int[each_fenzu_num[i]];
            for (int j=0;j<each_fenzu_num[i];j++){
                if(friends_info1[j][3]==null){
                    sublogos[i][j]=R.drawable.dmm;
                }else {
                    sublogos[i][j] = Integer.parseInt(friends_info1[j][3]);
                }

                System.out.println(sublogos[i][j]);
            }
        }
        //子视图显示文字
        final String[][] subcategory = new String[fenzu_num][];
        for (int i=0;i<fenzu_num;i++){
            String[][] friends_info2 = sqlMannger.get_friends_info(fenzu_detail[i][0],Integer.parseInt(fenzu_detail[i][1]));
            subcategory[i] = new String[each_fenzu_num[i]];
            for (int j=0;j<each_fenzu_num[i];j++){
                if(friends_info2[j][1]==null){
                    subcategory[i][j] = "贴心机器人";
                }else {
                    subcategory[i][j] = friends_info2[j][1];
                }

                System.out.println("subcategory[i][j]"+subcategory[i][j]);
            }
        }

        //设置ExpandableView视图
        final ExpandableListAdapter adapter=new BaseExpandableListAdapter() {
            //定义一个显示文字信息的方法
            TextView getTextView(){
                AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,120);
                TextView textView=new TextView(getActivity());
                //设置 textView控件的布局
                textView.setLayoutParams(lp);
                //设置该textView中的内容相对于textView的位置
                textView.setGravity(Gravity.CENTER_VERTICAL);
                //设置txtView的内边距
                textView.setPadding(36, 0, 0, 0);
                //设置文本颜色
                textView.setTextColor(Color.BLACK);
                return textView;
            }
            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public boolean hasStableIds() {
                // TODO Auto-generated method stub
                return true;
            }
            //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                                     View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                //定义一个LinearLayout用于存放ImageView、TextView
                LinearLayout ll=new LinearLayout(getActivity());
                //设置子控件的显示方式为水平
                //ll.setOrientation(0);
                TextView textView=getTextView();
                textView.setTextSize(20);
                textView.setPadding(80,0,0,0);
                textView.setText(category[groupPosition]);
                ll.addView(textView);
                return ll;
            }
            //取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）.
            @Override
            public long getGroupId(int groupPosition) {
                // TODO Auto-generated method stub
                return groupPosition;
            }
            //取得分组数
            @Override
            public int getGroupCount() {
                // TODO Auto-generated method stub
                return category.length;
            }
            //取得与给定分组关联的数据
            @Override
            public Object getGroup(int groupPosition) {
                // TODO Auto-generated method stub
                return category[groupPosition];
            }
            //取得指定分组的子元素数.
            @Override
            public int getChildrenCount(int groupPosition) {
                // TODO Auto-generated method stub
                return subcategory[groupPosition].length;
            }
            //取得显示给定分组给定子位置的数据用的视图
            @Override
            public View getChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                //定义一个LinearLayout用于存放ImageView、TextView
                LinearLayout ll=new LinearLayout(getActivity());
                //设置子控件的显示方式为水平
                //ll.setOrientation(0);
                //定义一个ImageView用于显示列表图片
                ImageView logo=new ImageView(getActivity());
                logo.setPadding(0, 0, 0, 0);
                //设置logo的大小
                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(140, 140);
                logo.setLayoutParams(lp);
                logo.setImageResource(sublogos[groupPosition][childPosition]);
                ll.addView(logo);
                TextView textView=getTextView();
                textView.setText(subcategory[groupPosition][childPosition]);
                textView.setTextSize(20);
                ll.addView(textView);
                return ll;
            }
            //取得给定分组中给定子视图的ID. 该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）.
            @Override
            public long getChildId(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return childPosition;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return subcategory[groupPosition][childPosition];
            }
        };
        Friend_List.setAdapter(adapter);
        //为ExpandableListView的子列表单击事件设置监听器
        Friend_List.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getActivity(), MainChatActivity.class);
                intent.putExtra("netname",subcategory[groupPosition][childPosition]);
                intent.putExtra("my_ip",my_ip);
                startActivity(intent);
                return true;
            }
        });
        Friend_List.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                return true;
            }
        });
    }
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initview();
    }
}