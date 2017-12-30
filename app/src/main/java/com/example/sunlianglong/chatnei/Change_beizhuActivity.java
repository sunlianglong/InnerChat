package com.example.sunlianglong.chatnei;

import android.content.Intent;
import android.opengl.EGLSurface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sunlianglong.messageServer.SqlMannger;

public class Change_beizhuActivity extends AppCompatActivity {
    private String netname;
    private EditText beizhu_data;
    private String beizhu;
    private Button change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_beizhu);
        //获取对方的昵称
        Intent intent = getIntent();
        netname = intent.getStringExtra("netname");
        beizhu = intent.getStringExtra("beizhu");
        beizhu_data = (EditText)findViewById(R.id.beizhu_data);
        change = (Button)findViewById(R.id.change);
        beizhu_data.setText(beizhu);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = beizhu_data.getText().toString();
                SqlMannger sqlMannger = new SqlMannger(Change_beizhuActivity.this);
                sqlMannger.Change_beizhu(netname,a);
                finish();
            }
        });





    }
}
