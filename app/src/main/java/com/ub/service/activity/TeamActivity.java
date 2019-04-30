package com.ub.service.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ub.techexcel.adapter.TeamAdapter;
import com.ubao.techexcel.R;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends Activity {

    private RecyclerView mTeamRecyclerView;
    private TeamAdapter mTeamAdapter;
    private List<String> mCurrentTeamData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documentteam);
        initView();

    }

    private void initView() {
        mTeamRecyclerView= (RecyclerView) findViewById(R.id.recycleview);
        mTeamRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        mCurrentTeamData.add("");
        mCurrentTeamData.add("");
        mCurrentTeamData.add("");

        mTeamAdapter=new TeamAdapter(this,mCurrentTeamData);
        mTeamRecyclerView.setAdapter(mTeamAdapter);
    }
}
