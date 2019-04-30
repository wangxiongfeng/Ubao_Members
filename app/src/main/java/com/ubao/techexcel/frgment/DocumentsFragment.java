package com.ubao.techexcel.frgment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ub.service.activity.TeamActivity;
import com.ub.techexcel.adapter.CurrentTeamAdapter;
import com.ubao.techexcel.R;
import com.ubao.techexcel.info.Favorite;

import java.util.ArrayList;
import java.util.List;

public class DocumentsFragment extends MyFragment implements View.OnClickListener, CurrentTeamAdapter.OnItemLectureListener {


    private RecyclerView mCurrentTeamRrecyclerView;
    private CurrentTeamAdapter mCurrentTeamAdapter;
    private List<String> mCurrentTeamData = new ArrayList<>();
    private RelativeLayout teamrl;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.documentfragment, container,
                false);
        initView(view);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    @Override
    protected void lazyLoad() {
        // TODO Auto-generated method stub
        if (isPrepared && isVisible) {  // isPrepared 可见在onCreate之前执行
            if (!isLoadDataFinish) {
                isLoadDataFinish = true;
                initData();
            }
        }
    }

    private void initData() {


    }

    private void initView(View view) {

        mCurrentTeamRrecyclerView = (RecyclerView) view.findViewById(R.id.recycleview);
        teamrl = (RelativeLayout) view.findViewById(R.id.teamrl);
        teamrl.setOnClickListener(this);
        mCurrentTeamRrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mCurrentTeamData.add("");
        mCurrentTeamData.add("");
        mCurrentTeamData.add("");
        mCurrentTeamAdapter = new CurrentTeamAdapter(getActivity(), mCurrentTeamData);
        mCurrentTeamAdapter.setOnItemLectureListener(this);
        mCurrentTeamRrecyclerView.setAdapter(mCurrentTeamAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.teamrl:
                Intent intent = new Intent(getActivity(), TeamActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onItem(Favorite lesson) {

    }


    @Override
    public void onenterItem() {

    }
}
