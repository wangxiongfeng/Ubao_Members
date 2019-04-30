package com.ubao.techexcel.school;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ubao.techexcel.R;
import com.ubao.techexcel.adapter.SchoolAdapter;
import com.ubao.techexcel.config.AppConfig;
import com.ubao.techexcel.help.DialogSelectSchool;
import com.ubao.techexcel.info.School;
import com.ubao.techexcel.start.LoginGet;
import com.ubao.techexcel.view.ClearEditText;

import java.util.ArrayList;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class SelectSchoolActivity extends SwipeBackActivity {

    private RecyclerView rv_ss;
    private LinearLayout lin_main;
    private ImageView img_back;
    private ClearEditText et_search;
    private TextView tv_OK;

    private ArrayList<School> mlist = new ArrayList<>();
    private SchoolAdapter sAdapter;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private School school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_school);
        
        findView();
        initView();
        getAllSchool();
    }

    private void getAllSchool() {
        LoginGet loginGet = new LoginGet();
        loginGet.setMySchoolGetListener(new LoginGet.MySchoolGetListener() {
            @Override
            public void getSchool(ArrayList<School> list) {
                mlist = new ArrayList<>();
                mlist.addAll(list);
                sAdapter.UpdateRV(mlist, GetSaveInfo());
                SetMySchool();

            }
        });
        loginGet.GetSchoolInfo(getApplicationContext());
    }

    private void SetMySchool() {
        int id = GetSaveInfo();
        for (int i = 0; i < mlist.size(); i++) {
            if(mlist.get(i).getSchoolID() == id){
                school = mlist.get(i);
                break;
            }
        }
    }

    private void initView() {
        sAdapter = new SchoolAdapter(mlist, GetSaveInfo());
        sAdapter.setOnItemClickListener(new SchoolAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                school = mlist.get(position);
                sAdapter.UpdateRV(mlist, school.getSchoolID());
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_ss.setLayoutManager(layoutManager);
        rv_ss.setAdapter(sAdapter);
        img_back.setOnClickListener(new MyOnClick());
        tv_OK.setOnClickListener(new MyOnClick());
    }

    private int GetSaveInfo() {
        sharedPreferences = getSharedPreferences(AppConfig.LOGININFO,
                MODE_PRIVATE);
        return sharedPreferences.getInt("SchoolID", -1);
    }

    private void findView() {
        img_back = (ImageView) findViewById(R.id.img_back);
        rv_ss = (RecyclerView) findViewById(R.id.rv_ss);
        et_search = (ClearEditText) findViewById(R.id.et_search);
        tv_OK = (TextView) findViewById(R.id.tv_OK);
        lin_main = (LinearLayout) findViewById(R.id.lin_main);
    }
    
    protected class MyOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img_back:
                    finish();
                    break;
                case R.id.tv_OK:
                    ShowPop(v);
                    break;
            }
        }
    }

    private void ShowPop(View v) {
        if(school == null || mlist.size() == 0){
            return;
        }

        DialogSelectSchool ds = new DialogSelectSchool();
        ds.setPoPDismissListener(new DialogSelectSchool.DialogDismissListener() {
            @Override
            public void PopSelect(boolean isSelect) {
                BackChange(1.0f);
                if(isSelect){
                    editor = sharedPreferences.edit();
                    editor.putInt("SchoolID", school.getSchoolID());
                    editor.putString("SchoolName", school.getSchoolName());
                    editor.commit();
                    finish();
                }
            }
        });
        ds.EditCancel(SelectSchoolActivity.this, school);
        BackChange(0.5f);
    }

    private void BackChange(float value) {
        lin_main.animate().alpha(value);
        lin_main.animate().setDuration(500);
    }

}
