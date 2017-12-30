package com.example.sunlianglong.fragment;

import android.accessibilityservice.AccessibilityService;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sunlianglong.chatnei.MainActivity;
import com.example.sunlianglong.chatnei.QunchatActivity;
import com.example.sunlianglong.chatnei.R;


public class DongtaiFragment extends Fragment {
    private String my_ip;
    Context mContext;
    //获取ip
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
        View v =  inflater.inflate(R.layout.fragment_dongtai, container, false);
        Button btn = (Button)v.findViewById(R.id.Btn_chat);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),QunchatActivity.class);
                intent.putExtra("my_ip",my_ip);
                startActivity(intent);
            }
        });
        return v;
    }
}

