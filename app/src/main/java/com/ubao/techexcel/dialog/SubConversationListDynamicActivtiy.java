package com.ubao.techexcel.dialog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ubao.techexcel.R;
import com.ubao.techexcel.config.AppConfig;

import io.rong.imkit.fragment.SubConversationListFragment;

public class SubConversationListDynamicActivtiy extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfig.Name = "咨询信息";
		
        setContentView(R.layout.rong_activity); 
        SubConversationListFragment fragment = new SubConversationListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();
    }
}