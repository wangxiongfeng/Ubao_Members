package com.ubao.techexcel.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ubao.techexcel.R;
import com.ubao.techexcel.adapter.CustomerAdapter;
import com.ubao.techexcel.config.AppConfig;
import com.ubao.techexcel.help.SideBar;
import com.ubao.techexcel.help.SideBarSortHelp;
import com.ubao.techexcel.info.Customer;
import com.ubao.techexcel.start.LoginGet;
import com.ubao.techexcel.view.ClearEditText;

import java.util.ArrayList;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class MyFriendsActivity extends AppCompatActivity {


    private ListView lv_contact;
    private LinearLayout lin_none;
    private ClearEditText et_search;
    private SideBar sidebar;
    private TextView tv_back;
    private TextView tv_title;

    private CustomerAdapter cAdapter;

    private ArrayList<Customer> cuslist = new ArrayList<Customer>();
    ArrayList<Customer> eList = new ArrayList<Customer>();

    public static final String 俺の朋友 = "(๑•̀ㅂ•́)و✧";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myfriends);

        initView();
    }

    private void initView() {
        lin_none = (LinearLayout) findViewById(R.id.lin_none);
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        et_search = (ClearEditText) findViewById(R.id.et_search);
        sidebar = (SideBar) findViewById(R.id.sidebar);

        ViewCompat.setTransitionName(tv_title, 俺の朋友);

        editCustomers();
        getSide();

        cAdapter = new CustomerAdapter(this, cuslist);
        lv_contact.setAdapter(cAdapter);
        lv_contact.setOnItemClickListener(new MyOnitem());
        getData();

        tv_back.setOnClickListener(new MyOnClick());

    }

    protected class MyOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_back:
                    ActivityCompat.finishAfterTransition(MyFriendsActivity.this);
                    break;
                default:
                    break;
            }
        }
    }

    private void getSide() {
        sidebar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position;
                position = SideBarSortHelp.getPositionForSection(cuslist,
                        s.charAt(0));

                if (position != -1) {
                    lv_contact.setSelection(position);
                } else {
                    lv_contact
                            .setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                }

            }
        });

    }

    private void editCustomers() {
        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                eList = new ArrayList<Customer>();
                for (int i = 0; i < cuslist.size(); i++) {
                    Customer cus = cuslist.get(i);
                    String name = et_search.getText().toString();
                    String getName = cus.getName().toLowerCase();//转小写
                    String nameb = name.toLowerCase();//转小写
                    if (getName.contains(nameb.toString())
                            && name.length() > 0) {
                        Customer customer;
                        customer = cus;
                        eList.add(customer);
                    }
                }
                if (et_search.length() != 0) {
                    cAdapter.updateListView2(eList);
                } else {
                    cAdapter.updateListView2(cuslist);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }


    private void getData() {
        final LoginGet loginget = new LoginGet();
        loginget.setLoginGetListener(new LoginGet.LoginGetListener() {

            @Override
            public void getMember(ArrayList<Customer> list) {
            }

            @Override
            public void getCustomer(ArrayList<Customer> list) {
                cuslist = new ArrayList<Customer>();
                cuslist.addAll(list);
                cAdapter.updateListView(cuslist);
                VisibleGoneList(cuslist);
            }
        });
        loginget.CustomerRequest(MyFriendsActivity.this);

    }

    private void VisibleGoneList(ArrayList<Customer> list) {
        // TODO Auto-generated method stub
        if (0 == list.size()) {
            lin_none.setVisibility(View.VISIBLE);
            lv_contact.setVisibility(View.GONE);
            sidebar.setVisibility(View.GONE);
        } else {
            lin_none.setVisibility(View.GONE);
            lv_contact.setVisibility(View.VISIBLE);
            sidebar.setVisibility(View.VISIBLE);
        }
    }

    private class MyOnitem implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Customer cus = cuslist.get(position);
            if (et_search.length() != 0) {
                cus = eList.get(position);
            }
            if (0 == position && cus.getName().equals(AppConfig.RobotName)) {
                AppConfig.Name = AppConfig.RobotName;
                AppConfig.isUpdateDialogue = true;
                RongIM.getInstance().refreshUserInfoCache(new UserInfo(AppConfig.DEVICE_ID + AppConfig.RongUserID,
                        AppConfig.Name,
                        null));
                RongIM.getInstance().startPrivateChat(MyFriendsActivity.this,
                        AppConfig.DEVICE_ID + AppConfig.RongUserID,
                        AppConfig.Name);
            } else {
//                Intent intent = new Intent(getActivity(), isCustomer ? UserDetail.class : MemberDetail.class);
                Intent intent = new Intent(MyFriendsActivity.this, UserDetail.class);
                intent.putExtra("UserID", cus.getUserID());
                startActivity(intent);
            }

        }

    }

}
