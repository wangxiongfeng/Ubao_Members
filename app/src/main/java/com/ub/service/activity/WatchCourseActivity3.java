package com.ub.service.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.util.PreferencesCookieStore;
import com.mining.app.zxing.MipcaActivityCapture;
import com.ub.service.audiorecord.AudioRecorder;
import com.ub.service.audiorecord.RecordEndListener;
import com.ub.service.audiorecord.RecordStreamListener;
import com.ub.teacher.gesture.BrightnessHelper;
import com.ub.teacher.gesture.ShowChangeLayout;
import com.ub.teacher.gesture.VideoGestureRelativeLayout;
import com.ub.techexcel.adapter.BigAgoraAdapter;
import com.ub.techexcel.adapter.ChatAdapter;
import com.ub.techexcel.adapter.LeftAgoraAdapter;
import com.ub.techexcel.adapter.MyRecyclerAdapter;
import com.ub.techexcel.adapter.MyRecyclerAdapter2;
import com.ub.techexcel.adapter.SocketLogAdapter;
import com.ub.techexcel.adapter.TeacherRecyclerAdapter;
import com.ub.techexcel.bean.AgoraBean;
import com.ub.techexcel.bean.AudioActionBean;
import com.ub.techexcel.bean.LineItem;
import com.ub.techexcel.bean.NotifyBean;
import com.ub.techexcel.bean.ServiceBean;
import com.ub.techexcel.bean.SoundtrackBean;
import com.ub.techexcel.tools.AudioPlayDialog;
import com.ub.techexcel.tools.ConfirmDialog;
import com.ub.techexcel.tools.DetchedPopup;
import com.ub.techexcel.tools.FavoritePopup;
import com.ub.techexcel.tools.FavoriteVideoPopup;
import com.ub.techexcel.tools.FileUtils;
import com.ub.techexcel.tools.InviteUserPopup;
import com.ub.techexcel.tools.MoreactionPopup;
import com.ub.techexcel.tools.NotificationPopup;
import com.ub.techexcel.tools.ServiceInterfaceListener;
import com.ub.techexcel.tools.ServiceInterfaceTools;
import com.ub.techexcel.tools.SpliteSocket;
import com.ub.techexcel.tools.Tools;
import com.ub.techexcel.tools.VideoPopup;
import com.ub.techexcel.tools.WebCamPopup;
import com.ub.techexcel.tools.YinxiangCreatePopup;
import com.ub.techexcel.tools.YinxiangEditPopup;
import com.ub.techexcel.tools.YinxiangPopup;
import com.ub.techexcel.view.CustomVideoView;
import com.ubao.techexcel.R;
import com.ubao.techexcel.app.App;
import com.ubao.techexcel.config.AppConfig;
import com.ubao.techexcel.help.PopAlbums;
import com.ubao.techexcel.help.Popupdate;
import com.ubao.techexcel.help.Popupdate2;
import com.ubao.techexcel.httpgetimage.ImageLoader;
import com.ubao.techexcel.info.Customer;
import com.ubao.techexcel.info.Favorite;
import com.ubao.techexcel.service.ConnectService;
import com.ubao.techexcel.start.LoginGet;
import com.ubao.techexcel.tool.Md5Tool;

import org.feezu.liuli.timeselector.Utils.TextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import Decoder.BASE64Encoder;
import io.agora.openlive.model.AGEventHandler;
import io.agora.openlive.ui.BaseActivity;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * Created by wang on 2017/6/16.
 */
public class WatchCourseActivity3 extends BaseActivity implements View.OnClickListener, AGEventHandler, VideoGestureRelativeLayout.VideoGestureListener {

    private String targetUrl;
    public PopupWindow mPopupWindow1;
    public PopupWindow documentPopupWindow;
    public PopupWindow chatPopupWindow;

    public PopupWindow mPopupWindow2;
    public PopupWindow mPopupWindow4;
    private XWalkView wv_show;
    private CustomVideoView videoView;
    private VideoGestureRelativeLayout videoGestureRelativeLayout;
    private ShowChangeLayout showChangeLayout;
    private AudioManager mAudioManager;
    private int maxVolume = 0;
    private int oldVolume = 0;
    private int newProgress = 0, oldProgress = 0;
    private BrightnessHelper mBrightnessHelper;
    private float brightness = 1;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;

    private ImageView closeVideo;
    private ProgressBar mProgressBar;
    private String studentid, attachmentid;
    private String teacherid;
    private String meetingId;
    private String lessonId;
    private int isInstantMeeting;  // 1 新的课程   旧的课程
    private boolean isHtml = false;
    private boolean isStartCourse = false;
    private boolean isMeetingStatus = false;

    private int identity = 0;
    private WebSocketClient mWebSocketClient;

    public ImageLoader imageLoader;
    private Customer teacherCustomer = new Customer();
    private PowerManager pm;
    private PowerManager.WakeLock wl;

    /**
     * 当前为presenter的student
     */
    private Customer studentCustomer = new Customer();
    /**
     * isPresenter 为true  表示此时老师为presenter   不可点击老师  只能点击学生
     * isPresenter 为false  表示此时学生为presenter  不可点击学生  只能点击老师
     */
    private String currentPresenterId = "";
    private RecyclerView auditorrecycleview; //旁听者的集合
    private RecyclerView teacherrecycler; //老师学生的集合
    private MyRecyclerAdapter myRecyclerAdapter;//旁听者adapter
    private TeacherRecyclerAdapter teacherRecyclerAdapter;
    /**
     * meeting中旁听者的信息
     */
    private List<Customer> auditorList = new ArrayList<>();
    private List<Customer> teacorstudentList = new ArrayList<>(); //老师学生的集合

    private List<String> invateList = new ArrayList<>();  // 其余在meeting中的获得新加入invatelist的旁听者
    private List<String> invitedUserIds = new ArrayList<>(); //join meeting  返回的未加入meeting的invate

    private String leaveUserid;
    private RelativeLayout leavell;
    private RelativeLayout endll;
    private RelativeLayout refresh_notify_2;
    private RelativeLayout startll;
    private RelativeLayout joinvideo;
    private RelativeLayout callMeLater;
    private RelativeLayout scanll;
    private RelativeLayout testdebug;
    private RelativeLayout inviteUser;

    private RecyclerView documentrecycleview;
    private MyRecyclerAdapter2 myRecyclerAdapter2;
    private LineItem lineItem;  //当前展示的pdf
    private List<LineItem> documentList = new ArrayList<>();
    private String currentAttachmentId;
    private String currentAttachmentId2;
    private String currentAttachmentPage;
    private String currentBlankPageNumber;
    private File cache;
    private List<Favorite> myFavoriteList = new ArrayList<>();
    private List<Favorite> myFavoriteVideoList = new ArrayList<>();
    private ImageView menu;
    private ImageView command_active;
    private LinearLayout activte_linearlayout;
    private LinearLayout menu_linearlayout;
    private RelativeLayout displayAudience, displayFile, displaychat, displaywebcam, displayVideo, displayautocamera, setting, yinxiang;
    private RelativeLayout prepareStart, prepareclose, prepareScanTV;
    private LinearLayout noprepare;
    private boolean isPrepare = false;  //是否备课
    public static boolean watch3instance = false;
    private TextView endtextview;
    private TextView prompt;
    private String currentMode = "0";
    private int videoStatus = 0;
    private String videoFileId = "0";

    private String currentMaxVideoUserId = "";
    private String changeNumber = "0";

    private boolean isInClassroom = false;
    private MyHandler handler;
    private int screenWidth;

    private RelativeLayout selectwebcam, selectconnection, select240, select360, select480, select720, peertimebase, kloudcall, external;
    private TextView right3bnt;
    private LinearLayout right1;
    private LinearLayout settingllback;
    private LinearLayout right2;
    private LinearLayout right3;
    private LinearLayout leftview;
    private LinearLayout llpre;
    private TextView leavepre;

    private final int LINE_PEERTIME = 0;
    private final int LINE_KLOUDPHONE = 2;
    private final int LINE_EXTERNOAUDIO = 4;
    private int currentLine = LINE_PEERTIME;
    private String zoomInfo;
    private List<LineItem> videoList = new ArrayList<>();
    private boolean isLoadPdfAgain = true;
    private int yinxiangmode = 2;
    private boolean ishavedefaultpage;

    private static class MyHandler extends Handler {

        private WeakReference<WatchCourseActivity3> watchCourseActivity3WeakReference;

        public MyHandler(WatchCourseActivity3 watchCourseActivity3) {
            watchCourseActivity3WeakReference = new WeakReference<>(watchCourseActivity3);
        }

        @Override
        public void handleMessage(Message msg) {
            final WatchCourseActivity3 activity3 = watchCourseActivity3WeakReference.get();
            if (activity3 != null) {
                switch (msg.what) {
                    case 0x1109:
                        if (activity3.isWebViewLoadFinish) {
                            final String m = (String) msg.obj;
                            activity3.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(m);
                                        String actionType;
                                        try {
                                            actionType = jsonObject.getString("actionType");
                                        } catch (JSONException e) {
                                            actionType = "";
                                            e.printStackTrace();
                                        }
                                        if (actionType.equals("8")) {
                                            String attachmentid = jsonObject.getString("itemId");
                                            String pagenumber = jsonObject.getString("pageNumber");
                                            activity3.currentAttachmentPage = pagenumber;
                                            if (activity3.currentAttachmentId.equals(attachmentid)) {
                                                if (pagenumber.equals(activity3.currentAttachmentPage)) {

                                                } else {
                                                    activity3.currentAttachmentPage = pagenumber;
                                                    String changpage = "{\"type\":2,\"page\":" + activity3.currentAttachmentPage + "}";
                                                    activity3.wv_show.load("javascript:PlayActionByTxt('" + changpage + "','" + activity3.currentAttachmentId + "')", null);
                                                }
                                            } else {
                                                for (int i = 0; i < activity3.documentList.size(); i++) {
                                                    LineItem lineItem2 = activity3.documentList.get(i);
                                                    if ((attachmentid).equals(lineItem2.getAttachmentID())) {
                                                        activity3.lineItem = lineItem2;
                                                        activity3.currentAttachmentPage = pagenumber;
                                                        break;
                                                    }
                                                }
                                                activity3.changedocumentlabel(activity3.lineItem);
                                            }
                                        } else {
                                            activity3.wv_show.load("javascript:PlayActionByTxt('" + m + "','" + activity3.currentAttachmentId + "')", null);
                                            activity3.zoomInfo = null;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        break;
                    case 0x1120:
                        final String m = (String) msg.obj;
                        if (!m.equals(activity3.currentMode)) {
                            activity3.currentMode = m;
                            activity3.switchMode();
                        }
                        break;
                    case AppConfig.SUCCESS:
                        activity3.mProgressBar.setVisibility(View.GONE);
                        activity3.getAllData((List<Customer>) msg.obj);
                        for (int i = 0; i < activity3.documentList.size(); i++) {
                            LineItem lineItem2 = activity3.documentList.get(i);
                            if ((activity3.currentAttachmentId).equals(lineItem2.getAttachmentID())) {
                                String url = lineItem2.getUrl();
                                if ((url.equals(activity3.targetUrl))) {
                                    return;
                                }
                            }
                        }
                        activity3.getServiceDetail();
                        activity3.getVideoList();
                        break;
                    case 0x3102:
                        activity3.mProgressBar.setVisibility(View.GONE);
                        activity3.getAllData((List<Customer>) msg.obj);
                        break;
                    case AppConfig.SUCCESS2:
                        activity3.mProgressBar.setVisibility(View.GONE);
                        activity3.getAllData((List<Customer>) msg.obj);
                        break;
                    case 0x1110:  // 收到改变presenter的socket
                        for (int i = 0; i < activity3.teacorstudentList.size(); i++) {
                            Customer customer = activity3.teacorstudentList.get(i);
                            if (customer.getUserID().equals(activity3.currentPresenterId)) {
                                customer.setPresenter(true);
                                activity3.studentCustomer = customer;
                            } else {
                                customer.setPresenter(false);
                            }
                        }
                        activity3.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (activity3.currentPresenterId.equals(AppConfig.UserID)) {
                                    activity3.wv_show.load("javascript:ShowToolbar(" + true + ")", null);
                                    activity3.wv_show.load("javascript:Record()", null);
                                } else {
                                    activity3.wv_show.load("javascript:ShowToolbar(" + false + ")", null);
                                    activity3.wv_show.load("javascript:StopRecord()", null);
                                }
                            }
                        });
                        activity3.teacherRecyclerAdapter.Update(activity3.teacorstudentList);
                        activity3.videoPopuP.setPresenter(activity3.identity,
                                activity3.currentPresenterId,
                                activity3.studentCustomer, activity3.teacherCustomer);
                        if (activity3.isHavePresenter()) {
                            activity3.setting.setVisibility(View.VISIBLE);
                            activity3.findViewById(R.id.videoline).setVisibility(View.VISIBLE);
                        } else {
                            activity3.setting.setVisibility(View.GONE);
                            activity3.findViewById(R.id.videoline).setVisibility(View.GONE);
                        }
                        if (activity3.isAgoraRecord) {
                            activity3.stopAgoraRecording(false);
                        }
                        break;
                    case 0x1111: //离开
                        activity3.changeAllVisible(activity3.leaveUserid);
                        break;
                    case 0x1121:  // invate  to  meeting
                        List<String> ll = (List<String>) msg.obj;
                        activity3.getDetailInfo(ll, 1);
                        break;
                    case 0x1203: // 学生或其他旁听者  收到的  旁听者信息 invate  to  meeting
                        activity3.setDefaultAuditor((List<Customer>) msg.obj);
                        break;
                    case 0x1204: //join  meeting  返回的未进入meeting的人
                        activity3.setDefaultAuditor2((List<Customer>) msg.obj);
                        break;
                    case 0x1301: // 提升旁听者为学生
//                        activity3.promoteAuditor((String) (msg.obj));
                        break;
                    case 0x1123: // 初始化文档列表
                        activity3.documentList = (List<LineItem>) msg.obj;
                        if (activity3.documentList.size() > 0) {
                            for (int i = 0; i < activity3.documentList.size(); i++) {
                                String id = activity3.documentList.get(i).getAttachmentID();
                                if (!TextUtils.isEmpty(activity3.currentAttachmentId)) {
                                    if (activity3.currentAttachmentId.equals(id)) {
                                        activity3.documentList.get(i).setSelect(true);
                                    }
                                }
                                for (int j = 0; j < activity3.uploadList.size(); j++) {
                                    String id2 = activity3.uploadList.get(j).getAttachmentID();
                                    if (id.equals(id2)) {  // uploadList来不及删除
                                        activity3.uploadList.remove(j);
                                        j = j - 1;
                                    }
                                }
                            }
                            Log.e("iiiiiiiiiiiii", activity3.uploadList.size() + "");
                            activity3.documentList.addAll(activity3.uploadList);
                            activity3.myRecyclerAdapter2 = new MyRecyclerAdapter2(activity3, activity3.documentList);
                            activity3.documentrecycleview.setAdapter(activity3.myRecyclerAdapter2);
                            activity3.myRecyclerAdapter2.setMyItemClickListener(new MyRecyclerAdapter2.MyItemClickListener() {
                                @SuppressLint("JavascriptInterface")
                                @Override
                                public void onItemClick(int position) {
                                    if (activity3.isHavePresenter()) {
                                        activity3.currentAttachmentPage = "0";
                                        AppConfig.currentPageNumber = "0";
                                        for (int i = 0; i < activity3.documentList.size(); i++) {
                                            activity3.documentList.get(i).setSelect(false);
                                        }
                                        activity3.lineItem = activity3.documentList.get(position);
                                        activity3.lineItem.setSelect(true);
                                        activity3.myRecyclerAdapter2.notifyDataSetChanged();
                                        activity3.currentAttachmentId = activity3.lineItem.getAttachmentID();
                                        activity3.currentAttachmentId2 = activity3.lineItem.getAttachmentID2();
                                        activity3.currentBlankPageNumber = activity3.lineItem.getBlankPageNumber();
                                        activity3.targetUrl = activity3.lineItem.getUrl();
                                        activity3.isHtml = activity3.lineItem.isHtml5();
                                        activity3.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                activity3.wv_show.load("file:///android_asset/index.html", null);
                                            }
                                        });
                                        activity3.notifySwitchDocumentSocket(activity3.lineItem, "1");

                                    }
                                }
                            });
                        }
                        for (int i = 0; i < activity3.documentList.size(); i++) {
                            LineItem lineItem2 = activity3.documentList.get(i);
                            if (activity3.currentAttachmentId.equals("0")) {
                            } else {
                                if ((activity3.currentAttachmentId).equals(lineItem2.getAttachmentID())) {
                                    activity3.lineItem = lineItem2;
                                    break;
                                }
                            }
                        }
                        if (activity3.isLoadPdfAgain) {
                            activity3.isLoadPdfAgain = false;
                            activity3.changedocumentlabel(activity3.lineItem);
                        }
                        if (activity3.isHavePresenter()) {
                            activity3.updatecurrentdocumentid();
                        }
                        break;
                    case 0x1205:
                        activity3.findViewById(R.id.defaultpagehaha).setVisibility(View.GONE);
                        activity3.lineItem = (LineItem) msg.obj;
                        activity3.changedocumentlabel(activity3.lineItem);
                        break;
                    case 0x4010:
                        final String ddd = (String) msg.obj;
                        activity3.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (activity3.wv_show != null) {
                                    if (!TextUtil.isEmpty(ddd)) {
                                        activity3.wv_show.load("javascript:PlayActionByArray(" + ddd + "," + activity3.currentAttachmentId + ")", null);
                                    }
                                }
                            }
                        });
                        break;
                    case 0x2101:    // addBlankPageFunction
                        String pageid = (String) msg.obj;
                        for (int i = 0; i < activity3.documentList.size(); i++) {
                            LineItem lineItem = activity3.documentList.get(i);
                            if ((lineItem.getAttachmentID()).equals(activity3.currentAttachmentId)) {
                                String blankPageNumber = lineItem.getBlankPageNumber();
                                if (TextUtils.isEmpty(blankPageNumber)) {
                                    activity3.currentBlankPageNumber = pageid;
                                } else {
                                    activity3.currentBlankPageNumber = blankPageNumber + "," + pageid;
                                }
                                lineItem.setBlankPageNumber(activity3.currentBlankPageNumber);
                                break;
                            }
                        }
                        break;
                    case 0x3121:
                        int lineId = (int) msg.obj;
                        switch (lineId) {
                            case 0:    //打开声网
                                activity3.showAgora();
                                break;
                            case 2:    //打开kc
                                activity3.showKloudCall();
                                break;
                            case 4:
                                activity3.showExterNoAudio();
                                break;
                        }
                        break;
                    case 0x4001:
                        String prepareMode = (String) msg.obj;
                        int pre = Integer.parseInt(prepareMode);
                        if (pre == 1) {
                            activity3.llpre.setVisibility(View.VISIBLE);
                        } else if (pre == 0) {
                            activity3.llpre.setVisibility(View.GONE);
                        }
                        break;
                    case 0x4113:
                        String retcode = (String) msg.obj;
                        if (retcode.equals("-2002")) {
                            Toast.makeText(activity3, "用户没有kloud call账号", Toast.LENGTH_LONG).show();
                        } else if (retcode.equals("-2301")) {
                            Toast.makeText(activity3, "用户kloud call账号余额不足", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 0x1190:
                        activity3.detectPopwindow((int) msg.obj);
                        break;
                    case 0x2105:  //join meeting 走进来的
                        activity3.videoPopuP.setVideoList(activity3.videoList);
                        if (activity3.currentMode.equals(4 + "")) {
                            activity3.startOrPauseVideo(activity3.videoStatus, 0.0f, activity3.videoFileId, "", 0);
                        }
                        break;
                    case 0x6002:
//                        int type = (int) msg.obj;
//                        if (type == 1) {  //pdf
//                            activity3.getServiceDetail();
//                        } else if (type == 2) {  // video
//                            activity3.getVideoList();
//                        }
                        break;
                    case 0x6115: //copy file finish
                        String s = (String) msg.obj;
                        activity3.wv_show.load(s, null);
                        activity3.wv_show.load("javascript:Record()", null);
                        break;
                    case AppConfig.FAILED:
                        String result = (String) msg.obj;
                        /*Toast.makeText(activity3,
                                result,
                                Toast.LENGTH_LONG).show();*/
                        break;
                    case 0x7001:
                        List<AudioActionBean> audioActionBeanList = (List<AudioActionBean>) msg.obj;
                        if (audioActionBeanList.size() > 0) {
                            activity3.newAudioActionTime(audioActionBeanList);
                        }
                        break;
                    case 0x2345:
                        ServiceBean bean = (ServiceBean) msg.obj;
                        Intent intent = new Intent(activity3, WatchCourseActivity2.class);
                        intent.putExtra("userid", bean.getUserId());
                        intent.putExtra("meetingId", bean.getId() + "");
                        intent.putExtra("teacherid", bean.getTeacherId());
                        intent.putExtra("identity", bean.getRoleinlesson());
                        intent.putExtra("isStartCourse", true);
                        intent.putExtra("isInstantMeeting", 0);
                        activity3.startActivity(intent);
                        activity3.finish();
                        break;
                }
                super.handleMessage(msg);
            }
        }
    }


    private Timer actionTimer;

    private void newAudioActionTime(final List<AudioActionBean> audioActionBeanList) {
        Log.e("newAudioActionTime", audioActionBeanList.size() + "");
        if (actionTimer != null) {
            actionTimer.cancel();
            actionTimer = null;
        }
        actionTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (audioActionBeanList.size() == 0) {
                    actionTimer.cancel();
                }
                for (int i1 = 0; i1 < audioActionBeanList.size(); i1++) {
                    AudioActionBean audioActionBean = audioActionBeanList.get(i1);
                    Log.e("newAudioActionTime", tttime + "   " + audioActionBean.getTime());
                    if (tttime >= audioActionBean.getTime()) {
                        String data = audioActionBean.getData();
                        Message msg3 = Message.obtain();
                        msg3.obj = data;
                        msg3.what = 0x1109;
                        handler.sendMessage(msg3);
                        audioActionBeanList.remove(i1);
                        i1--;
                    } else {
                        break;
                    }
                }
            }
        };
        actionTimer.schedule(timerTask, 0, 500);
    }


    /**
     * 打开声网
     */
    private void showAgora() {
        if (startLessonPopup != null) {  //kcloud call  选择弹框
            startLessonPopup.dismiss();
        }
        currentLine = LINE_PEERTIME;
        callMeLater.setVisibility(View.GONE);
        initListen(true);
        icon_command_mic_enabel.setEnabled(true);
        openshengwang(2);
    }

    /**
     * 打开kloudcall
     */
    private void showKloudCall() {
        if (webCamPopuP != null) {
            webCamPopuP.dismiss();
        }
        callMeOrLater(identity, AppConfig.Mobile);
        callMeLater.setVisibility(View.VISIBLE);
        currentLine = LINE_KLOUDPHONE;
        initListen(false);
        icon_command_mic_enabel.setEnabled(false);
    }

    /**
     * 打开kloudcall
     */
    private void showKloudCall2() {
        callMeLater.setVisibility(View.VISIBLE);
        currentLine = LINE_KLOUDPHONE;
        initListen(false);
        icon_command_mic_enabel.setEnabled(false);
    }

    /**
     * 打开no dudio 模式
     */
    private void showExterNoAudio() {
        currentLine = LINE_EXTERNOAUDIO;
        callMeLater.setVisibility(View.GONE);
        initListen(false);
        icon_command_mic_enabel.setEnabled(false);
    }


    private void notifySwitchDocumentSocket(LineItem lineItem, String pagenumber) {
        JSONObject json = new JSONObject();
        try {
            json.put("actionType", 8);
            json.put("eventID", lineItem.getEventID());
            json.put("attachmentUrl", lineItem.getUrl());
            json.put("eventName", lineItem.getEventName());
            json.put("meetingID", meetingId);
            json.put("itemId", lineItem.getAttachmentID());
            json.put("incidentID", targetUrl);
            json.put("pageNumber", pagenumber);
            json.put("isH5", lineItem.isHtml5());
        } catch (Exception e) {
            e.printStackTrace();
        }
        send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
    }

    /**
     * 老师第一次join时，调一下这个方法，将当前文档ID发给鹏飞
     */
    private void updatecurrentdocumentid() {
        try {
            JSONObject loginjson = new JSONObject();
            loginjson.put("action", "UPDATE_CURRENT_DOCUMENT_ID");
            loginjson.put("sessionId", AppConfig.UserToken);
            loginjson.put("documentId", currentAttachmentId);
            String ss = loginjson.toString();
            SpliteSocket.sendMesageBySocket(ss);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changedocumentlabel(LineItem lineItem) {
        if (documentList.size() > 0) {
            if (lineItem == null) {
                documentList.get(0).setSelect(true);
                myRecyclerAdapter2.notifyDataSetChanged();
                currentAttachmentId = documentList.get(0).getAttachmentID();
                currentAttachmentId2 = documentList.get(0).getAttachmentID2();
                targetUrl = documentList.get(0).getUrl();
                currentBlankPageNumber = documentList.get(0).getBlankPageNumber();
                isHtml = documentList.get(0).isHtml5();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wv_show.load("file:///android_asset/index.html", null);
                    }
                });
            } else {
                for (int i = 0; i < documentList.size(); i++) {
                    LineItem lineItem1 = documentList.get(i);
                    Log.e("dddddd", lineItem1.getAttachmentID() + "   " + lineItem.getAttachmentID());
                    if (lineItem.getAttachmentID().equals(lineItem1.getAttachmentID())) {
                        lineItem1.setSelect(true);
                    } else {
                        lineItem1.setSelect(false);
                    }
                    Log.e("isselect", lineItem1.getFlag() + "   " + lineItem1.isSelect() + "   " + lineItem1.getAttachmentID() + "  " + lineItem.getAttachmentID());
                }
                myRecyclerAdapter2.notifyDataSetChanged();
                currentAttachmentId = lineItem.getAttachmentID();
                currentAttachmentId2 = lineItem.getAttachmentID2();
                targetUrl = lineItem.getUrl();
                currentBlankPageNumber = lineItem.getBlankPageNumber();
                isHtml = lineItem.isHtml5();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wv_show.load("file:///android_asset/index.html", null);
                    }
                });
            }
        }
    }

    /**
     * invate meeting  设置收到旁听者的信息
     *
     * @param list
     */
    private void setDefaultAuditor(List<Customer> list) {
        for (Customer c : list) {
            c.setEnterMeeting(false);
            c.setRole(1);
        }
        for (int i = 0; i < list.size(); i++) {
            Customer c1 = list.get(i);
            boolean isexist = false;
            for (int j = 0; j < teacorstudentList.size(); j++) {
                Customer c2 = teacorstudentList.get(j);
                if (c1.getUserID().equals(c2.getUserID())) {
                    c2.setEnterMeeting(false);
                    isexist = true;
                }
            }
            if (!isexist) {
                teacorstudentList.add(c1);
            }
        }
        teacherRecyclerAdapter.Update(teacorstudentList);
    }

    /**
     * join_meeting 设置为加入meeting 的旁听者
     *
     * @param list
     */
    private void setDefaultAuditor2(List<Customer> list) {
        for (Customer c : list) {
            c.setEnterMeeting(false);
            c.setRole(1);
            teacorstudentList.add(c);
        }
        teacherRecyclerAdapter.Update(teacorstudentList);
    }


    private void changeAllVisible(String leaveUserid) {
        //更新老师学生列表
        for (int j = 0; j < teacorstudentList.size(); j++) {
            Customer c = teacorstudentList.get(j);
            if (c.getUserID().equals(leaveUserid)) {
                teacorstudentList.remove(j);
                j--;
            }
        }
        teacherRecyclerAdapter.Update(teacorstudentList);
        //更新auditorList
        for (int j = 0; j < auditorList.size(); j++) {
            Customer c = auditorList.get(j);
            if (c.getUserID().equals(leaveUserid)) {
                auditorList.remove(j);
                j--;
            }
        }
        myRecyclerAdapter.notifyDataSetChanged();
    }


    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
//        if (Build.VERSION.SDK_INT < 19) { // lower api
//            View v = getWindow().getDecorView();
//            v.setSystemUiVisibility(View.GONE);
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//            View decorView = getWindow().getDecorView();
//            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
//            decorView.setSystemUiVisibility(uiOptions);
//        }

        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.watchcourse3);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }
        WindowManager wm = (WindowManager)
                getSystemService(WINDOW_SERVICE);
        EventBus.getDefault().register(this);
        screenWidth = wm.getDefaultDisplay().getWidth();
        handler = new MyHandler(this);
        watch3instance = true;
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);

        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "TEST");
        wl.acquire();

        teacherid = getIntent().getStringExtra("teacherid");
        studentid = getIntent().getStringExtra("userid");
        identity = getIntent().getIntExtra("identity", 0);
        meetingId = getIntent().getStringExtra("meetingId");
        isStartCourse = getIntent().getBooleanExtra("isStartCourse", false);
        isPrepare = getIntent().getBooleanExtra("isPrepare", false);
        isInstantMeeting = getIntent().getIntExtra("isInstantMeeting", 1);
        lessonId = getIntent().getStringExtra("lessionId");
        yinxiangmode = getIntent().getIntExtra("yinxiangmode", 2);
        isInClassroom = getIntent().getBooleanExtra("isInClassroom", false);
        ishavedefaultpage = getIntent().getBooleanExtra("ishavedefaultpage", false);
        Log.e("-------", targetUrl + "  " + attachmentid + "  meetingId: " + meetingId + "  identity : " + identity + "    isInstantMeeting :" + isInstantMeeting + "   lession: " + lessonId);
        imageLoader = new ImageLoader(this);
        cache = new File(Environment.getExternalStorageDirectory(), "Image");
        if (!cache.exists()) {
            cache.mkdirs();
        }

        initView();
        //初始化老师学生列表
        initDisplayAudienceList();
        //初始化文档列表
        initDocumentListAudienceList();
        //初始化会话弹窗
        initChatAudienceList();
        initSocketLogAudienceList();
        initVideoPopup();
        openSaveVideoPopup();

        ((App) getApplication()).initWorkerThread();
        initUIandEvent();
        icon_back.setTag(false);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("com.cn.getGroupbroadcastReceiver");
        registerReceiver(getGroupbroadcastReceiver, intentFilter2);

        //注册广播 接受socket信息
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cn.socket");
        registerReceiver(broadcastReceiver, intentFilter);
        initdefault();

    }


    private void initdefault() {
        mWebSocketClient = AppConfig.webSocketClient;
        if (mWebSocketClient == null) {
            return;
        }
        if (mWebSocketClient.getReadyState() == WebSocket.READYSTATE.OPEN) {
            sendStringBySocket2("JOIN_MEETING", AppConfig.UserToken, "", meetingId, attachmentid, true, "v20140605.0", false, identity, isInstantMeeting);
        }
    }

    private boolean isJoinChannel = false;

    /**
     * 获取选中的旁听者
     */
    @Override
    protected void onResume() {
        if (isJoinChannel) {

        } else {
            isJoinChannel = true;
            worker().joinChannel(meetingId.toUpperCase(), config().mUid);
            togglelinearlayout.setVisibility(View.VISIBLE);
            issetting = true;
        }

        if (AppConfig.isUpdateAuditor) {
            for (Customer c : AppConfig.auditorList) {
                c.setEnterMeeting(false);
            }
            for (Customer d : AppConfig.auditorList) {
                boolean isex = false;
                for (Customer c : teacorstudentList) {
                    if (d.getUserID().equals(c.getUserID())) {
                        isex = true;
                    }
                }
                if (!isex) {
                    teacorstudentList.add(d);
                }
            }
            teacherRecyclerAdapter.Update(teacorstudentList);
            AppConfig.isUpdateAuditor = false;
            invite_meeting(AppConfig.auditorList);
            AppConfig.auditorList.clear();
        }
        if (AppConfig.ISSUCCESS) {
            AppConfig.ISSUCCESS = false;
            LineItem attachmentBean = new LineItem();
            attachmentBean.setUrl(AppConfig.CURRENT_VALUES); //文件的路径
            attachmentBean.setFileName(AppConfig.CURRENT_VALUES
                    .substring(AppConfig.CURRENT_VALUES.lastIndexOf("/") + 1)); // 文件名
            AppConfig.CURRENT_VALUES = null;
            uploadFile(attachmentBean, false);
        }
        if (wv_show != null) {
            wv_show.resumeTimers();
            wv_show.onShow();
        }
        openAlbum();

        if (!TextUtils.isEmpty(AppConfig.OUTSIDE_PATH)) { // 打开我的收藏弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("")
                    .setMessage(getString(R.string.ask_update_out))
                    .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            LoginGet loginGet = new LoginGet();
                            loginGet.setMyFavoritesGetListener(new LoginGet.MyFavoritesGetListener() {
                                @Override
                                public void getFavorite(ArrayList<Favorite> list) {
                                    myFavoriteList.clear();
                                    myFavoriteList.addAll(list);
                                    openMySavePopup(myFavoriteList, 1); //拿到已存在的收藏
                                    UploadFavoriteWithHash(AppConfig.OUTSIDE_PATH, AppConfig.OUTSIDE_PATH.substring(AppConfig.OUTSIDE_PATH.lastIndexOf("/") + 1));
                                }
                            });
                            loginGet.MyFavoriteRequest(WatchCourseActivity3.this, 1);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.No),
                            new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                    AppConfig.OUTSIDE_PATH = "";
                                }
                            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        super.onResume();
    }


    private void UploadFavoriteWithHash(final String path, final String title) {
        Log.e("video", path + " : " + title);
        final JSONObject jsonobject = null;
        String url = null;
        File file = new File(path);
        if (file.exists()) {
            try {
                url = AppConfig.URL_PUBLIC + "FavoriteAttachment/UploadFileWithHash?Title="
                        + URLEncoder.encode(LoginGet.getBase64Password(title), "UTF-8") +
                        /*"&schoolID=" +
                        SchoolID +*/
                        "&Hash=" +
                        Md5Tool.getMd5ByFile(file);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String finalUrl = url;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responsedata = ConnectService
                                .submitDataByJson(finalUrl, jsonobject);
                        String retcode = responsedata.getString("RetCode");
                        Log.e("UploadFileWithHash", responsedata.toString() + "");
                        Message msg = new Message();
                        if (retcode.equals(AppConfig.RIGHT_RETCODE)) {  //刷新
                            msg.what = AppConfig.DELETESUCCESS;
                        } else if (retcode.equals(AppConfig.Upload_NoExist + "")) { // 添加
                            UpdateOutData();
                        } else if (retcode.equals(AppConfig.Upload_Exist + "")) { //不要重复上传
                            msg.what = AppConfig.FAILED;
                            String ErrorMessage = responsedata
                                    .getString("ErrorMessage");
                            msg.obj = ErrorMessage;
                        }
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.nofile),
                    Toast.LENGTH_LONG).show();
        }
    }


    private List<Favorite> uploadFavorList = new ArrayList<>();

    private void UpdateOutData() {
        RequestParams params = new RequestParams();
        params.setHeader("UserToken", AppConfig.UserToken);
        params.addBodyParameter("Content-Type", "multipart/form-data");// 设定传送的内容类型
        // params.setContentType("application/octet-stream");
        File file = new File(AppConfig.OUTSIDE_PATH);
        if (file.exists()) {
            int lastSlash = 0;
            lastSlash = AppConfig.OUTSIDE_PATH.lastIndexOf("/");
            String name = AppConfig.OUTSIDE_PATH.substring(lastSlash + 1, AppConfig.OUTSIDE_PATH.length());

            final Favorite favorite = new Favorite();
            favorite.setFlag(1);
            favorite.setTitle(name);
            favorite.setFileName(name);

            params.addBodyParameter(name, file);
            String url = null;
            try {
                url = AppConfig.URL_PUBLIC + "FavoriteAttachment/AddNewFavorite?Title="
                        + URLEncoder.encode(LoginGet.getBase64Password(name), "UTF-8") +
                        /*"&schoolID=" +
                        SchoolID
                        +*/ "&Hash=" + Md5Tool.getMd5ByFile(file);
                Log.e("hahaha", url + ":" + name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("url", url);
            HttpUtils http = new HttpUtils();
            http.configResponseTextCharset("UTF-8");
            http.send(HttpRequest.HttpMethod.POST, url, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            myFavoriteList.add(favorite);
                            uploadFavorList.add(favorite);
                            favoriteFilePopup.notifyDataSetChanged();
                        }

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {
                            favoriteFilePopup.setProgress(total, current, favorite);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            Log.e("hahaha999999", responseInfo.result + "");
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(responseInfo.result);
                                JSONObject js = jsonObject.getJSONObject("RetData");
                                if (js.getInt("Status") == 10) { // 上传成功  开始转换
                                    int attachmentid = js.getInt("AttachmentID");
                                    favorite.setAttachmentID(attachmentid);
                                    favorite.setFlag(2);
                                    favoriteFilePopup.setProgress(1, 0, favorite);
                                    final Timer timer = new Timer();
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            convertingPercentage2(favorite, timer);
                                        }
                                    };
                                    timer.schedule(timerTask, 0, 1000);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {

                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.nofile),
                    Toast.LENGTH_LONG).show();
        }
        AppConfig.OUTSIDE_PATH = "";
    }

    private void convertingPercentage2(final Favorite favorite, final Timer timer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = ConnectService.getIncidentbyHttpGet(AppConfig.URL_PUBLIC
                        + "Attachment/AttachmentConvertingPercentage?attachmentIDs=" + favorite.getAttachmentID());
                Log.e("iiiiiiiiii4444", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("RetData");
                    if (jsonArray == null) {
                        timer.cancel();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        int status = jsonObject1.getInt("Status");
                        int Percentage = jsonObject1.getInt("Percentage");
                        if (status == 0) {
                            Log.e("iiiiiiiiii4444", "cancel");
                            favorite.setProgressbar(100);
                            favorite.setFlag(0);
                            timer.cancel();
                            uploadFavorList.remove(favorite);
                        } else if (status == 10) {
                            favorite.setProgressbar(Percentage);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            favoriteFilePopup.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 邀请旁听者
     *
     * @param list
     */
    private void invite_meeting(List<Customer> list) {
        String meetings = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                meetings += list.get(i).getUserID();
            } else {
                meetings += "," + list.get(i).getUserID();
            }
        }
        try {
            JSONObject loginjson = new JSONObject();
            loginjson.put("action", "INVITE_TO_MEETING");
            loginjson.put("sessionId", AppConfig.UserToken);
            loginjson.put("meetingId", meetingId);
            loginjson.put("userIds", meetings);
            String ss = loginjson.toString();
            SpliteSocket.sendMesageBySocket(ss);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        try {
            json.put("meetingID", meetingId);
            json.put("sourceID", teacherid);
            json.put("targetID", studentid);
            json.put("incidentID", "");
            json.put("roleType", "1");
            json.put("attachmentUrl", targetUrl);
            json.put("actionType", 10);
            json.put("isH5", isHtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("send_mesage", meetings + "   " + json.toString());
        send_message("SEND_MESSAGE", AppConfig.UserToken, 1, meetings, Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
    }


    /**
     * 通知 放大缩小视频
     */
    private void changevideo(int i, String currentSessionID) {
        if (isHavePresenter()) {
            changeVideo1(i, currentSessionID);
        }
    }

    private void changeVideo1(int i, String currentSessionID) {
        JSONObject json = new JSONObject();
        try {
            json.put("videoMode", i);
            json.put("actionType", 9);
            if (i == 2) {
                json.put("currentSessionID", currentSessionID);
            } else {
                json.put("currentSessionID", "null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
        send_message("SEND_MESSAGE", AppConfig.UserToken, 1, AppConfig.UserID, Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
    }


    private String firstStatus = "0";

    /**
     * @param msg
     */
    private void isopenvideo(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            String status = getRetCodeByReturnData2("status", msg);
            firstStatus = status;
            Log.e("isopenvideo", status + "    " + currentLine);
            if (status.equals("1")) {  //已经加入课程
                if (currentLine == LINE_PEERTIME) {  // 刚进来是  声网  模式
                    if (!(Boolean) icon_back.getTag()) {
                        openshengwang(0);  // 打开声网
                        icon_back.setTag(true);
                    }
                } else if (currentLine == LINE_KLOUDPHONE) {  // 刚进来是  kloudcall  模式
                    showKloudCall2();
                    if (!(Boolean) icon_back.getTag()) {
                        openshengwang(1);  // 打开声网
                        icon_back.setTag(true);
                    }
                } else if (currentLine == LINE_EXTERNOAUDIO) {  // 刚进来是  no audio  模式
                    showExterNoAudio();
                    if (!(Boolean) icon_back.getTag()) {
                        openshengwang(1);  // 打开声网
                        icon_back.setTag(true);
                    }
                }
                startll.setVisibility(View.GONE);
                leavell.setVisibility(View.VISIBLE);
                prompt.setVisibility(View.GONE);
            } else if (status.equals("0")) {
                endtextview.setText("Close");
                if (identity == 2) { //老师
                    if (isStartCourse) {
                        startCourse();
                    } else {
                        startll.setVisibility(View.VISIBLE);
                        leavell.setVisibility(View.GONE);
                    }
                } else if (identity == 1) {
                    //学生 自己加入
                    joinvideo.setVisibility(View.VISIBLE);
                    startll.setVisibility(View.GONE);
                    leavell.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private String prepareMsg;
    String recordingId;

    private void doJOIN_MEETING(String msg) {
        try {
            JSONObject jsonObject = new JSONObject(msg);
            changeNumber = jsonObject.getString("changeNumber");
            JSONObject retdata = jsonObject.getJSONObject("retData");

            JSONArray jsonArray = retdata.getJSONArray("usersList");
            List<Customer> joinlist = Tools.getUserListByJoinMeeting(jsonArray);

            msg = retdata.toString();
            String currentLine2 = getRetCodeByReturnData2("currentLine", msg);
            currentLine = Integer.parseInt(currentLine2);
            currentMode = getRetCodeByReturnData2("currentMode", msg);
            currentMaxVideoUserId = getRetCodeByReturnData2("currentMaxVideoUserId", msg);
            if (isPrepare) {
                prepareMsg = msg;
                command_active.setVisibility(View.GONE);
            } else {
                isopenvideo(msg);
            }
            invitedUserIds.clear();
            String ids = getRetCodeByReturnData2("invitedUserIds", msg);
            if (!TextUtils.isEmpty(ids)) {
                String idss[] = ids.split(",");
                for (String s : idss) {
                    invitedUserIds.add(s);
                }
            }
            currentMode = getRetCodeByReturnData2("currentMode", msg);
            try {
                String videoStatus2 = getRetCodeByReturnData2("videoStatus", msg);
                videoStatus = TextUtils.isEmpty(videoStatus2) ? 0 : Integer.parseInt(videoStatus2);
                videoFileId = getRetCodeByReturnData2("videoFileId", msg);
                currentMaxVideoUserId = getRetCodeByReturnData2("currentMaxVideoUserId", msg);
                zoomInfo = getRetCodeByReturnData2("zoomInfo", msg);
                zoomInfo = zoomInfo.replaceAll("'", "\"");
                Log.e("miao1", zoomInfo);
                zoomInfo = zoomInfo.replaceAll("'", "\"");
                Log.e("miao2", zoomInfo);
                changeNumber2();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            String currentpage = getRetCodeByReturnData2("CurrentDocumentPage", msg);
            String[] page = currentpage.split("-");
            currentAttachmentId = page[0];
            currentAttachmentPage = page[1];

            Message message1 = Message.obtain();
            message1.obj = joinlist;
            message1.what = AppConfig.SUCCESS;
            handler.sendMessage(message1);

            String prepareMode = getRetCodeByReturnData2("prepareMode", msg);
            Message msg2 = Message.obtain();
            msg2.obj = prepareMode;
            msg2.what = 0x4001;
            handler.sendMessage(msg2);
            //播放音频
            String playAudioData = retdata.getString("playAudioData");
            String msg3 = Tools.getFromBase64(playAudioData);
            if (!TextUtils.isEmpty(msg3)) {
                JSONObject ms3 = new JSONObject(msg3);
                int actionType = ms3.getInt("actionType");
                if (actionType == 23) {
                    int soundtrackId = ms3.getInt("soundtrackId");
                    int stat = ms3.getInt("stat");
                    if (stat == 1 || stat == 4) {
                        getAudioAction(soundtrackId);
                        getyinxiangdetail(soundtrackId);
                    }
                }
            }
            int hideCamera = retdata.getInt("hideCamera");
            if (hideCamera == 0) {
                isShowDefaultVideo = true;
            } else {
                isShowDefaultVideo = false;
            }
            recordingId = retdata.getString("recordingId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void doLEAVE_MEETING(String msg) {
        notifyleave();
        String retdate = getRetCodeByReturnData2("retData", msg);
        try {
            leaveUserid = new JSONObject(retdate).getString("userId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg2 = Message.obtain();
        msg2.what = 0x1111;
        handler.sendMessage(msg2);

        if (identity == 2) { // 老师将权限拿过来
            if (!leaveUserid.equals(teacherCustomer.getUserID())) {
                if (currentPresenterId.equals(leaveUserid)) {  // 离开的学生是presenter
                    //设置老师为presenter
                    sendStringBySocket3("MAKE_PRESENTER", teacherCustomer.getUsertoken(), meetingId, teacherCustomer.getUsertoken());
                }
            }
        }

    }

    private int audioTime = 0;
    private boolean isendmeeting;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String message = intent.getStringExtra("message");
            String msg = Tools.getFromBase64(message);
            String msg_action = getRetCodeByReturnData2("action", msg);

            if (msg_action.equals("JOIN_MEETING")) {
                if (getRetCodeByReturnData2("retCode", msg).equals("0")) {
                    doJOIN_MEETING(msg);
                } else if (getRetCodeByReturnData2("retCode", msg).equals("-1")) {
                    initdefault();  // 重新 JOIN_MEETING
                }
            }

            if (msg_action.equals("USER_JOIN_MEETING_ON_OTHER_DEVICE")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.deviceprompt), Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            if (msg_action.equals("OFFLINE_MODE") || msg_action.equals("ONLINE_MODE")) {
                try {
                    List<Customer> joinlist = new ArrayList<>();
                    JSONObject jsonObject = new JSONObject(msg);
                    JSONObject retdata = jsonObject.getJSONObject("retData");
                    JSONArray jsonArray = retdata.getJSONArray("usersList");
                    joinlist = Tools.getUserListByJoinMeeting(jsonArray);
                    Message message1 = Message.obtain();
                    message1.obj = joinlist;
                    message1.what = 0x3102;
                    handler.sendMessage(message1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            /**
             * 邀请学生加入声网 成功回调
             */
            if (msg_action.equals("MEETING_STATUS")) {
                if (getRetCodeByReturnData2("status", msg).equals("1")) { //打开直播
                    prompt.setVisibility(View.GONE);
                    if (currentLine == LINE_PEERTIME) {  // 刚进来是  声网  模式
                        openshengwang(0);
                    } else if (currentLine == LINE_KLOUDPHONE) {   // 刚进来是  kloudcall  模式
                        showKloudCall2();
                        openshengwang(1);
                    } else if (currentLine == LINE_EXTERNOAUDIO) {  // 刚进来是  no audio  模式
                        showExterNoAudio();
                        openshengwang(1);
                    }
                    icon_back.setTag(true);
                    startll.setVisibility(View.GONE);
                    leavell.setVisibility(View.VISIBLE);
                    prompt.setVisibility(View.GONE);
                }
            }
            /**
             * 收到升级旁听者的通知
             */
            if (msg_action.equals("PROMOTE_TO_STUDENT")) {
                String userid = getRetCodeByReturnData2("userId", msg);
                Message ms = Message.obtain();
                ms.obj = userid;
                ms.what = 0x1301;
                handler.sendMessage(ms);
            }

            /**
             * 邀请旁听者 其余在线收到的信息
             */
            if (msg_action.equals("INVITE_TO_MEETING")) {
                invateList.clear();
                String userIDS = getRetCodeByReturnData2("userIds", msg);
                if (!TextUtils.isEmpty(userIDS)) {
                    String userid[] = userIDS.split(",");
                    for (int i = 0; i < userid.length; i++) {
                        invateList.add(userid[i]);
                    }
                    Message invatemsg = Message.obtain();
                    invatemsg.obj = invateList;
                    invatemsg.what = 0x1121;
                    handler.sendMessage(invatemsg);
                }
            }
            /**
             *
             * 切换文档  直播视频的切换
             */
            if (msg_action.equals("SEND_MESSAGE")) {
                String d = getRetCodeByReturnData2("data", msg);
                try {
                    JSONObject jsonObject = new JSONObject(Tools.getFromBase64(d));
                    if (jsonObject.getInt("actionType") == 2) {  // 横竖屏切换

                    } else if (jsonObject.getInt("actionType") == 8) { // 切换文档
                        LineItem lineitem = new LineItem();
                        lineitem.setUrl(jsonObject.getString("attachmentUrl"));
                        lineitem.setHtml5(jsonObject.getBoolean("isH5"));
                        lineitem.setAttachmentID(jsonObject.getString("itemId"));
                        currentAttachmentPage = jsonObject.getString("pageNumber");
                        AppConfig.currentPageNumber = jsonObject.getString("pageNumber");
                        Message documentMsg = Message.obtain();
                        documentMsg.obj = lineitem;
                        documentMsg.what = 0x1205;
                        handler.sendMessage(documentMsg);

                    } else if (jsonObject.getInt("actionType") == 9) { // 直播视频大小切换
                        Log.e("dddddddddddd", currentMode);
                        if (currentMode.equals("4")) {
                            currentMode = jsonObject.getInt("videoMode") + "";
                            if (currentMode.equals(0 + "")) {  // 关闭video
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startOrPauseVideo(2, 0, "", "", 0);
                                    }
                                });
                            }
                        } else {
                            currentMode = jsonObject.getInt("videoMode") + "";
                            if (!currentMode.equals("4")) {  // 4 为播放视频   0 1 2
                                try {
                                    currentMaxVideoUserId = jsonObject.getString("currentSessionID");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                switchMode();
                            }
                        }
                    } else if (jsonObject.getInt("actionType") == 16) {  //kloudcall or peertime
                        int lineId = jsonObject.getInt("lineId");
                        Message invatemsg = Message.obtain();
                        invatemsg.obj = lineId;
                        invatemsg.what = 0x3121;
                        handler.sendMessage(invatemsg);
                    } else if (jsonObject.getInt("actionType") == 19) {
                        final int stat = jsonObject.getInt("stat");
                        final float time = jsonObject.getInt("time");
                        final String attachmentId = jsonObject.getString("vid");
                        final String url = jsonObject.getString("url");
                        final int videotype = jsonObject.getInt("type");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                startOrPauseVideo(stat, time, attachmentId, url, videotype);
                            }
                        });
                    } else if (jsonObject.getInt("actionType") == 23) {  //学生接收是否播放音频
                        Log.e("mediaplayer-----", jsonObject.toString());
                        final int stat = jsonObject.getInt("stat");
                        final String soundtrackID = jsonObject.getString("soundtrackId");
                        if (stat == 4) {
                            audioTime = jsonObject.getInt("time");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("mediaplayer-----", stat + ":   " + (mediaPlayer == null));
                                if (stat == 1) {
                                    int vid2 = 0;
                                    if (!TextUtils.isEmpty(soundtrackID)) {
                                        vid2 = Integer.parseInt(soundtrackID);
                                    }
                                    getAudioAction(vid2);
                                    getyinxiangdetail(vid2);
                                } else if (stat == 0) {
                                    StopMedia();
                                    StopMedia2();
                                    closeAudioSync();
                                } else if (stat == 2) {
                                    pauseMedia();
                                    pauseMedia2();
                                } else if (stat == 3) {
                                    resumeMedia();
                                    resumeMedia2();
                                } else if (stat == 4) {
                                    Log.e("mediaplayer-----", audioTime + "  " + tttime);
                                    if (audioTime - tttime > 3000) {
                                        tttime = audioTime;
                                        if (mediaPlayer != null) {
                                            mediaPlayer.seekTo(tttime);
                                        }
                                        if (mediaPlayer2 != null) {
                                            mediaPlayer2.seekTo(tttime);
                                        }
                                    }
                                }
                            }
                        });
                    } else if (jsonObject.getInt("actionType") == 14) { // 收到关闭自己的Audio
                        if (jsonObject.getInt("stat") == 0) {
                            initListen(false);
                            if (mViewType == VIEW_TYPE_NORMAL || mViewType == VIEW_TYPE_SING_NORMAL) {
                                openVideoByViewType();
                            }
                        } else if (jsonObject.getInt("stat") == 1) {
                            initListen(true);
                            if (mViewType == VIEW_TYPE_NORMAL || mViewType == VIEW_TYPE_SING_NORMAL) {
                                openVideoByViewType();
                            }
                        }
                    } else if (jsonObject.getInt("actionType") == 15) { //收到关闭自己的Video
                        if (jsonObject.getInt("stat") == 0) {
                            initMute(false);
                            openVideoByViewType();
                        }
                    } else if (jsonObject.getInt("actionType") == 21) {
                        if (jsonObject.getInt("isHide") == 0) {
//                            initMute(true);  // SHOW
                            if (isShowDefaultVideo == false) {
                                isShowDefaultVideo = true;
                                toggleicon.setImageResource(R.drawable.eyeopen);
                                mLeftAgoraAdapter.setData(mUidsList, teacherid);
                                mLeftRecycler.setVisibility(View.VISIBLE);
                                toggle.setTag(true);
                            } else {
                                openVideoByViewType();
                            }
                        } else if (jsonObject.getInt("isHide") == 1) {
//                            initMute(false);  // HIDDEN
                            if (isShowDefaultVideo == true) {
                                isShowDefaultVideo = false;
                                toggleicon.setImageResource(R.drawable.eyeclose);
                                mLeftAgoraAdapter.setData(null, teacherid);
                                mLeftRecycler.setVisibility(View.GONE);
                                toggle.setTag(false);
                            } else {
//                                openVideoByViewType();
                            }
                        }
                    } else if (jsonObject.getInt("actionType") == 24) {
                        String newMeetingid = jsonObject.getString("meetingID");
                        enterTeacherOnGoingMeeting(newMeetingid);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg_action.equals("SEND_MESSAGE")) {
                String d = getRetCodeByReturnData2("data", msg);
                try {
                    JSONObject jsonObject = new JSONObject(Tools.getFromBase64(d));
                    if (jsonObject.getInt("actionType") == 11) {
                        lessonId = jsonObject.getString("incidentID");
                        getServiceDetail();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (msg_action.equals("KLOUD_CALL_STARTED")) {
                if (getRetCodeByReturnData2("retCode", msg).equals("1")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Message invatemsg = Message.obtain();
                            invatemsg.obj = 2;
                            invatemsg.what = 0x3121;
                            handler.sendMessage(invatemsg);
                        }
                    });
                }
            }
            if (msg_action.equals("CALL_ME")) {
                if (getRetCodeByReturnData2("retCode", msg).equals("-2403")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(WatchCourseActivity3.this, "user exist in conference", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            if (msg_action.equals("CREATE_KLOUD_CALL_CONFERENCE")) {
                Message invatemsg = Message.obtain();
                invatemsg.obj = getRetCodeByReturnData2("retCode", msg);
                invatemsg.what = 0x4113;
                handler.sendMessage(invatemsg);
            }
            if (msg_action.equals("END_KLOUD_CALL_CONFERENCE")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callMeLater.setVisibility(View.GONE);
                    }
                });
            }
            //通知上传文档了
            if (msg_action.equals("ATTACHMENT_UPLOADED")) {
                getServiceDetail();
                getVideoList();
            }
            if (msg_action.equals("UPDATE_CURRENT_DOCUMENT_ID")) {
//                currentAttachmentId=getRetCodeByReturnData2("documentId", msg);
            }
            //获得课程Action
            if (msg_action.equals("BROADCAST_FRAME")) {
                String msg2 = Tools.getFromBase64(getRetCodeByReturnData2("data", msg));
                Message msg3 = Message.obtain();
                msg3.obj = msg2;
                msg3.what = 0x1109;
                handler.sendMessage(msg3);
            }
            // 1@MAKE_PRESENTER
            if (msg_action.equals("MAKE_PRESENTER")) {
                if (getRetCodeByReturnData2("retCode", msg).equals("1")) {  // 此时设置student or teacher presenter success
                    currentPresenterId = getRetCodeByReturnData2("presenterId", msg);
                    Message msg4 = Message.obtain();
                    msg4.what = 0x1110;
                    handler.sendMessage(msg4);
                    if (currentPresenterId.equals(AppConfig.UserID)) {
                        changeVideo1(0, "");


                    }
                }
            }
            //离开课程  返回的socket
            if (msg_action.equals("LEAVE_MEETING")) {  // 别人收到xx离开
                doLEAVE_MEETING(msg);
            }
            //老师 结束课程  所有人离开
            if (msg_action.equals("END_MEETING")) { //所有人都收到
                if (getRetCodeByReturnData2("retCode", msg).equals("1")) {
                    if (isHavePresenter()) {
                        if (isAgoraRecord) {
                            isendmeeting = true;
                            stopAgoraRecording(true);
                        } else {
                            CheckAndMerge(true);
                        }
                    } else {
                        finish();
                    }
                }
            }
            if (msg_action.equals("HELLO")) {
                AppConfig.isPresenter = isHavePresenter();
                AppConfig.status = 1 + "";
                AppConfig.currentLine = currentLine;
                AppConfig.currentMode = currentMode;
                AppConfig.currentPageNumber = currentAttachmentPage;
                AppConfig.currentDocId = currentAttachmentId;
                try {
                    String data = getRetCodeByReturnData2("data", msg);
                    if (!TextUtils.isEmpty(data)) {
                        JSONObject jsonObject = new JSONObject(Tools.getFromBase64(data));

                        String currentAttachmentIds = jsonObject.getString("currentItemId");
                        currentPresenterId = jsonObject.getString("currentPresenter");
                        if (!currentPresenterId.equals(AppConfig.UserID)) {
                            String currentAttachmentPage = jsonObject.getString("currentPageNumber");
                            if (currentAttachmentIds.equals(currentAttachmentId)) {  //当前是同一个文档
                                String changpage = "{\"type\":2,\"page\":" + currentAttachmentPage + "}";
                                Message msg3 = Message.obtain();
                                msg3.obj = changpage;
                                msg3.what = 0x1109;
                                if (!isPlaying2) {
                                    handler.sendMessage(msg3);
                                }
                            } else {   //切换到当前文档
                                currentAttachmentId = currentAttachmentIds;
                                for (int i = 0; i < documentList.size(); i++) {
                                    if (documentList.get(i).getAttachmentID().equals(currentAttachmentId)) {
                                        Message documentMsg = Message.obtain();
                                        documentMsg.obj = documentList.get(i);
                                        documentMsg.what = 0x1205;
                                        handler.sendMessage(documentMsg);
                                    }
                                }
                            }
                            Message msg4 = Message.obtain();
                            msg4.obj = currentMode;
                            msg4.what = 0x1120;
                            handler.sendMessage(msg4);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (msg_action.equals("NO_ACTIVITY_DETECTED")) {
                if (teacherCustomer.getUserID().equals(AppConfig.UserID)) {
                    Message msg3 = Message.obtain();
                    String countDown = getRetCodeByReturnData2("countDown", msg);
                    int countDown2 = TextUtils.isEmpty(countDown) ? 0 : Integer.parseInt(countDown);
                    msg3.obj = countDown2;
                    msg3.what = 0x1190;
                    handler.sendMessage(msg3);
                }
            }
        }
    };

    private void enterTeacherOnGoingMeeting(final String newMeetingid) {
        doLeaveChannel();
        event().removeEventHandler(this);
        mUidsList.clear();

        new Thread(new Runnable() {
            @Override
            public void run() {


                JSONObject returnjson = com.ub.techexcel.service.ConnectService
                        .getIncidentbyHttpGet(AppConfig.URL_PUBLIC
                                + "Lesson/Item?lessonID=" + newMeetingid);
                formatMeetingData(returnjson);
            }
        }).start();
    }


    private void formatMeetingData(JSONObject returnJson) {
        Log.e("formatMeetingData", returnJson.toString());
        try {
            int retCode = returnJson.getInt("RetCode");
            switch (retCode) {
                case AppConfig.RETCODE_SUCCESS:
                    JSONObject service = returnJson.getJSONObject("RetData");
                    ServiceBean bean = new ServiceBean();
                    bean.setId(service.getInt("LessonID"));
                    String des = service.getString("Description");
                    bean.setDescription(des);
                    int statusID = service.getInt("StatusID");
                    bean.setStatusID(statusID);
                    bean.setRoleinlesson(service.getInt("RoleInLesson"));
                    if (bean.getRoleinlesson() == 3) {
                        bean.setRoleinlesson(1);
                    }
                    JSONArray memberlist = service.getJSONArray("MemberInfoList");
                    for (int i = 0; i < memberlist.length(); i++) {
                        JSONObject jsonObject = memberlist.getJSONObject(i);
                        int role = jsonObject.getInt("Role");
                        if (role == 2) { //teacher
                            bean.setTeacherName(jsonObject.getString("MemberName"));
                            bean.setTeacherId(jsonObject.getString("MemberID"));
                        } else if (role == 1) {
                            bean.setUserName(jsonObject.getString("MemberName"));
                            bean.setUserId(jsonObject.getString("MemberID"));
                        }
                    }

                    Message message = Message.obtain();
                    message.obj = bean;
                    message.what = 0x2345;
                    handler.sendMessage(message);

                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 改变HELLO changenumber的值
     */
    private void changeNumber2() {
        Intent intent1 = new Intent();
        intent1.setAction("com.cn.changenumber");
        intent1.putExtra("changeNumber", changeNumber);
        sendBroadcast(intent1);
    }

    private void sendvoicenet(String action, String sessionId, String meetingId, int status, String lessionid) {
        try {
            JSONObject loginjson = new JSONObject();
            loginjson.put("action", action);
            loginjson.put("sessionId", sessionId);
            loginjson.put("meetingId", meetingId);
            loginjson.put("status", status);
            loginjson.put("lessonid", lessionid);
            String ss = loginjson.toString();
            SpliteSocket.sendMesageBySocket(ss);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 老师给学生发送上课消息
     */
    private void send_message(String action, String sessionId, int type, String userlist, String data) {
        try {
            JSONObject loginjson = new JSONObject();
            loginjson.put("action", action);
            loginjson.put("sessionId", sessionId);
            loginjson.put("type", type);
            loginjson.put("userList", userlist);
            loginjson.put("data", data);
            String ss = loginjson.toString();
            SpliteSocket.sendMesageBySocket(ss);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendStringBySocket2(String action, String sessionId, String username, String meetingId2,
                                     String itemId, boolean isPresenter, String clientVersion, boolean isLogin, int role, int isInstantMeeting) {
        try {
            JSONObject loginjson = new JSONObject();
            loginjson.put("action", action);
            loginjson.put("sessionId", sessionId);
            loginjson.put("meetingId", meetingId2);
            loginjson.put("meetingPassword", "");
            loginjson.put("clientVersion", clientVersion);
            loginjson.put("role", role);
            loginjson.put("isInstantMeeting", isInstantMeeting);
            loginjson.put("lessonId", lessonId);
            if (isInClassroom) {
                loginjson.put("isInClassroom", 1);
                isInClassroom = false;
            }
            String ss = loginjson.toString();
            SpliteSocket.sendMesageBySocket(ss);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * MAKE_PRESENTER
     */
    private void sendStringBySocket3(String action, String sessionId, String meetingId, String newPresenterSessionId) {
        try {
            JSONObject loginjson = new JSONObject();
            loginjson.put("action", action);
            loginjson.put("sessionId", sessionId);
            loginjson.put("meetingId", meetingId);
            loginjson.put("newPresenterSessionId", newPresenterSessionId);
            String ss = loginjson.toString();
            SpliteSocket.sendMesageBySocket(ss);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获得socket返回的action
     */
    private String getRetCodeByReturnData2(String str, String returnData) {
        if (!TextUtils.isEmpty(returnData)) {
            try {
                JSONObject jsonObject = new JSONObject(returnData);
                if (jsonObject.has(str)) {
                    return jsonObject.getString(str) + "";
                } else {
                    return "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }


    private LinearLayout createblabkpage, inviteattendee, sharedocument;

    private void initView() {
        puo2 = new Popupdate2();
        puo2.getPopwindow(WatchCourseActivity3.this);
        prompt = (TextView) findViewById(R.id.prompt);
        endtextview = (TextView) findViewById(R.id.endtextview);
        llpre = (LinearLayout) findViewById(R.id.llpre);
        leavepre = (TextView) findViewById(R.id.leavepre);
        leavepre.setOnClickListener(this);
        llpre.setOnClickListener(this);

        wv_show = (XWalkView) findViewById(R.id.wv_show);
        wv_show.setZOrderOnTop(false);
        wv_show.addJavascriptInterface(WatchCourseActivity3.this, "AnalyticsWebInterface");
        XWalkPreferences.setValue("enable-javascript", true);
        XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, true);
        XWalkPreferences.setValue(XWalkPreferences.SUPPORT_MULTIPLE_WINDOWS, true);

        createblabkpage = (LinearLayout) findViewById(R.id.createblabkpage);
        inviteattendee = (LinearLayout) findViewById(R.id.inviteattendee);
        sharedocument = (LinearLayout) findViewById(R.id.sharedocument);
        findViewById(R.id.defaultpagehaha).setVisibility(View.VISIBLE);
        TextView tv = (TextView) findViewById(R.id.ashoast);
        if (ishavedefaultpage && identity == 2) {
            findViewById(R.id.ppromat).setVisibility(View.GONE);
            tv.setText("As host ,you can");
            createblabkpage.setOnClickListener(this);
            inviteattendee.setOnClickListener(this);
            sharedocument.setOnClickListener(this);
        } else if (ishavedefaultpage) {
            findViewById(R.id.ppromat).setVisibility(View.VISIBLE);
            tv.setText("As attendee ,you can");
            createblabkpage.setVisibility(View.GONE);
        } else {
            findViewById(R.id.defaultpagehaha).setVisibility(View.GONE);
        }
        findViewById(R.id.hiddenwalkview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("haha", "onclick");
                activte_linearlayout.setVisibility(View.GONE);
                menu_linearlayout.setVisibility(View.GONE);
                findViewById(R.id.hiddenwalkview).setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                command_active.setImageResource(R.drawable.icon_command);
            }
        });
        //设置视频控制器
        videoView = (CustomVideoView) findViewById(R.id.videoView);
        videoView.setMediaController(new MediaController(this));
        videoView.setZOrderOnTop(true);
        videoView.setZOrderMediaOverlay(true);
        closeVideo = (ImageView) findViewById(R.id.closeVideo);
        closeVideo.setOnClickListener(this);
        videoGestureRelativeLayout = (VideoGestureRelativeLayout) findViewById(R.id.videoll);
        videoGestureRelativeLayout.setVideoGestureListener(this);
        showChangeLayout = (ShowChangeLayout) findViewById(R.id.scl);
        //初始化获取音量属性
        mAudioManager = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //初始化亮度调节
        mBrightnessHelper = new BrightnessHelper(this);
        //下面这是设置当前APP亮度的方法配置
        mWindow = getWindow();
        mLayoutParams = mWindow.getAttributes();
        brightness = mLayoutParams.screenBrightness;

        audiosyncll = (LinearLayout) findViewById(R.id.audiosyncll);
        timeShow = (TextView) findViewById(R.id.timeshow);
        timeShow.setOnClickListener(this);
        timeHidden = (ImageView) findViewById(R.id.timehidden);
        timeHidden.setOnClickListener(this);

        menu = (ImageView) findViewById(R.id.menu);
        menu.setOnClickListener(this);
        command_active = (ImageView) findViewById(R.id.command_active);
        command_active.setOnClickListener(this);

        activte_linearlayout = (LinearLayout) findViewById(R.id.activte_linearlayout);
        menu_linearlayout = (LinearLayout) findViewById(R.id.menu_linearlayout);

        displayAudience = (RelativeLayout) findViewById(R.id.displayAudience);
        displayAudience.setOnClickListener(this);
        displayFile = (RelativeLayout) findViewById(R.id.displayFile);
        displayFile.setOnClickListener(this);
        yinxiang = (RelativeLayout) findViewById(R.id.yinxiang);
        yinxiang.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.GONE);
        leavell = (RelativeLayout) findViewById(R.id.leavell);
        endll = (RelativeLayout) findViewById(R.id.endll);
        startll = (RelativeLayout) findViewById(R.id.startll);
        scanll = (RelativeLayout) findViewById(R.id.scanll);
        inviteUser = (RelativeLayout) findViewById(R.id.inviteuser);
        joinvideo = (RelativeLayout) findViewById(R.id.joinvideo);
        callMeLater = (RelativeLayout) findViewById(R.id.callmelater);
        refresh_notify_2 = (RelativeLayout) findViewById(R.id.refresh_notify_2);
        testdebug = (RelativeLayout) findViewById(R.id.testdebug);
        refresh_notify_2.setOnClickListener(this);
        leavell.setOnClickListener(this);
        endll.setOnClickListener(this);
        startll.setOnClickListener(this);
        joinvideo.setOnClickListener(this);
        callMeLater.setOnClickListener(this);
        scanll.setOnClickListener(this);
        inviteUser.setOnClickListener(this);
        testdebug.setOnClickListener(this);

        startll.setVisibility(View.GONE);
        joinvideo.setVisibility(View.GONE);
        leavell.setVisibility(View.VISIBLE);
        if (identity == 2) { //老师
            endll.setVisibility(View.VISIBLE);
        } else {
            endll.setVisibility(View.GONE);
        }

        startll.setVisibility(View.VISIBLE);
        leavell.setVisibility(View.GONE);
        endtextview.setText("Close");

        displaychat = (RelativeLayout) findViewById(R.id.displaychat);
        displaychat.setOnClickListener(this);
        prepareStart = (RelativeLayout) findViewById(R.id.prepareStart);
        prepareStart.setOnClickListener(this);
        prepareclose = (RelativeLayout) findViewById(R.id.prepareclose);
        prepareclose.setOnClickListener(this);
        prepareScanTV = (RelativeLayout) findViewById(R.id.prepareScanTV);
        prepareScanTV.setOnClickListener(this);
        noprepare = (LinearLayout) findViewById(R.id.noprepare);

        displaywebcam = (RelativeLayout) findViewById(R.id.displaywebcam);
        displaywebcam.setOnClickListener(this);
        displayVideo = (RelativeLayout) findViewById(R.id.displayvideo);
        displayVideo.setOnClickListener(this);
        displayautocamera = (RelativeLayout) findViewById(R.id.displayautocamera);
        displayautocamera.setOnClickListener(this);

        setting = (RelativeLayout) findViewById(R.id.setting);
        if (identity != 2) {
            setting.setVisibility(View.GONE);
            findViewById(R.id.videoline).setVisibility(View.GONE);
        }
        setting.setOnClickListener(this);
        settingllback = (LinearLayout) findViewById(R.id.settingllback);
        settingllback.setOnClickListener(this);

        selectwebcam = (RelativeLayout) findViewById(R.id.selectwebcam);
        selectconnection = (RelativeLayout) findViewById(R.id.selectconnect);
        select240 = (RelativeLayout) findViewById(R.id.select240);
        select360 = (RelativeLayout) findViewById(R.id.select360);
        select480 = (RelativeLayout) findViewById(R.id.select480);
        select720 = (RelativeLayout) findViewById(R.id.select720);
        peertimebase = (RelativeLayout) findViewById(R.id.peertimebase);
        kloudcall = (RelativeLayout) findViewById(R.id.kloudcall);
        external = (RelativeLayout) findViewById(R.id.external);
        right3bnt = (TextView) findViewById(R.id.right3bnt);
        right3bnt.setOnClickListener(this);
        leftview = (LinearLayout) findViewById(R.id.leftview);
        leftview.setOnClickListener(this);


        selectwebcam.setOnClickListener(this);
        selectconnection.setOnClickListener(this);
        select240.setOnClickListener(this);
        select360.setOnClickListener(this);
        select480.setOnClickListener(this);
        select720.setOnClickListener(this);
        peertimebase.setOnClickListener(this);
        kloudcall.setOnClickListener(this);
        external.setOnClickListener(this);

        right1 = (LinearLayout) findViewById(R.id.right1);
        right2 = (LinearLayout) findViewById(R.id.right2);
        right3 = (LinearLayout) findViewById(R.id.right3);

    }

    private Favorite favoriteAudio = new Favorite();
    private MediaPlayer mediaPlayer;
    private Timer audiotimer;
    private Timer audioplaytimer;

    private void GetMediaPlay(String url, final boolean isrecording) {
        Log.e("GetMediaPlay", url + "  " + isrecording);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (TextUtils.isEmpty(url)) {
            if (isrecording) {  // 同步录音
                doLeaveChannel();
                mUidsList.clear();
                mLeftAgoraAdapter.setData(null, "");
                togglelinearlayout.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAudioRecord();  //开始录音
                    }
                }, 1000);
            }
        } else {
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setLooping(true);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    Log.e("GetMediaPlay", "ffffffffff");
                    mp.start();

                    doLeaveChannel();
                    mUidsList.clear();
                    mLeftAgoraAdapter.setData(null, "");
                    togglelinearlayout.setVisibility(View.GONE);
                    if (isrecording) {  // 同步录音
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startAudioRecord();  //开始录音
                            }
                        }, 1000);
                    }

                }
            });
        }
    }

    private boolean issetting = false;

    private void StopMedia() {
        if (togglelinearlayout.getVisibility() == View.GONE) {
            worker().joinChannel(meetingId.toUpperCase(), config().mUid);
            togglelinearlayout.setVisibility(View.VISIBLE);
            issetting = true;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (audiotimer != null) {
            audiotimer.cancel();
            audiotimer = null;
        }
        if (audioplaytimer != null) {
            audioplaytimer.cancel();
            audioplaytimer = null;
        }
        if (actionTimer != null) {
            actionTimer.cancel();
            actionTimer = null;
        }

    }

    private void pauseMedia() {
        isPause = true;
        playstop.setImageResource(R.drawable.video_play);
        if (togglelinearlayout.getVisibility() == View.GONE) {
            worker().joinChannel(meetingId.toUpperCase(), config().mUid);
            togglelinearlayout.setVisibility(View.VISIBLE);
            issetting = true;
        }
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void resumeMedia() {
        isPause = false;
        playstop.setImageResource(R.drawable.video_stop);
        if (togglelinearlayout.getVisibility() == View.VISIBLE) {
            doLeaveChannel();
            mUidsList.clear();
            mLeftAgoraAdapter.setData(null, "");
            togglelinearlayout.setVisibility(View.GONE);
        }
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }


    private LinearLayout audiosyncll;
    private ImageView playstop;
    private ImageView close;
    private TextView isStatus;
    private TextView audiotime;
    private ImageView syncicon;

    private boolean isPause = false;
    private boolean isPlaying2;

    private void openAudioSync(final boolean isrecording, final boolean isPlaying, long duration) {
        isPlaying2 = isPlaying;
        //clean the action
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wv_show.load("javascript:ClearPageAndAction()", null);
            }
        });
        isPause = false;

        playstop = (ImageView) findViewById(R.id.playstop);
        playstop.setImageResource(R.drawable.video_stop);
        syncicon = (ImageView) findViewById(R.id.syncicon);
        close = (ImageView) findViewById(R.id.close);
        isStatus = (TextView) findViewById(R.id.isStatus);
        audiotime = (TextView) findViewById(R.id.audiotime);

        if (isPlaying) {
            isStatus.setText("");
            syncicon.setVisibility(View.GONE);
        } else {
            if (isrecording) {
                isStatus.setText("Recording");
                syncicon.setVisibility(View.VISIBLE);
            } else {
                isStatus.setText("Syncing");
                syncicon.setVisibility(View.GONE);
            }
        }
        if (!isHavePresenter()) {
            playstop.setEnabled(false);
            close.setEnabled(false);
        } else {
            playstop.setEnabled(true);
            close.setEnabled(true);
        }
        playstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPause) {
                    resumeMedia();
                    resumeMedia2();
                    if (!isrecording && isHavePresenter()) {
                        if (isPlaying) {
                            sendAudioSocket(3, soundtrackID);
                        }
                    }
                } else {
                    pauseMedia();
                    pauseMedia2();
                    if (!isrecording && isHavePresenter()) {
                        if (isPlaying) {
                            sendAudioSocket(2, soundtrackID);
                        }
                    }
                }
                if (isrecording) {
                    pauseOrStartAudioRecord();
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAudioSync();
                StopMedia();
                StopMedia2();
                if (isSync) {
                    sendSync("AUDIO_SYNC", AppConfig.UserToken, 0, soundtrackID);
                }
                if (isrecording) {    // 完成录音
                    stopAudioRecord(soundtrackID);
                } else if (isHavePresenter()) { //通知結束播放
                    if (isPlaying) {
                        sendAudioSocket(0, soundtrackID);
                    }
                }
            }
        });
        if (!isrecording && isHavePresenter()) {
            if (!isSync) {
                if (audiotimer != null) {
                    audiotimer.cancel();
                    audiotimer = null;
                }
                audiotimer = new Timer();
                audiotimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (isPlaying) {
                            sendAudioSocket(4, soundtrackID);
                        }
                    }
                }, 0, 3000);
            }
        }
        refreshTime(duration, isrecording);
        audiosyncll.setVisibility(View.VISIBLE);


    }

    int tttime = 0;
    private TextView timeShow;
    private ImageView timeHidden;

    private void refreshTime(final long duration, final boolean isrecording) {
        tttime = 0;
        audiotime.setText("00:00");
        if (audioplaytimer != null) {
            audioplaytimer.cancel();
            audioplaytimer = null;
        }
        audioplaytimer = new Timer();
        audioplaytimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!isPause) {
                            tttime = tttime + 1000;
                            if (audiotime != null) {
                                String time = new SimpleDateFormat("mm:ss").format(tttime);
                                audiotime.setText(time);
                                timeShow.setText(time);
                            }
                            if (!isrecording && isHavePresenter()) {
                                if (tttime >= duration) {
                                    if (isSync && !isrecording) {

                                    } else {
                                        closeAudioSync();
                                        StopMedia();
                                        StopMedia2();
                                        sendAudioSocket(0, soundtrackID);
                                    }
                                }
                            }
                        }
                    }
                });
            }
        }, 0, 1000);
    }


    private void closeAudioSync() {
        isPlaying2 = false;
        audiosyncll.setVisibility(View.GONE);
        timeShow.setVisibility(View.GONE);
    }


    private void getAllData(List<Customer> mAttendesList) {
        auditorList.clear();
        teacorstudentList.clear();
        for (int i = 0; i < mAttendesList.size(); i++) {
            Customer a = mAttendesList.get(i);
            a.setEnterMeeting(true);
            Log.e("在meeting中的人", a.getRole() + "  " + a.getName());
            if (a.getRole() == 2) {   // 设置老师
                teacherCustomer = a;
                teacherid = teacherCustomer.getUserID();
                if (a.isPresenter()) {
                    currentPresenterId = a.getUserID();
                }
                teacorstudentList.add(a);
            } else if (a.getRole() == 1) { //设置学生信息
                if (a.isPresenter()) {
                    studentCustomer = a;
                    studentid = a.getUserID();
                    currentPresenterId = a.getUserID();
                }
                teacorstudentList.add(a);
            } else if (a.getRole() == 3) { //设置旁听者信息
                a.setEnterMeeting(true);
                auditorList.add(a);
            }
        }
        //刷新旁听者列表
        if (auditorList.size() > 0) {
            myRecyclerAdapter.notifyDataSetChanged();
        }
        //刷新老师与学生列表
        if (teacorstudentList.size() > 0) {
            teacherRecyclerAdapter.Update(teacorstudentList);
        }
    }


    public int i = 0;
    /**
     * 老师端与学生端
     * 得到学生与老师的详细信息
     * isinvite
     * 0  解析老师  学生  信息
     * 1  invate to  meeting  返回的旁听者信息
     * 2  invitedUserIds|  invitedUserIds的信息
     */
    private String userid2 = "";

    private void getDetailInfo(List<String> useridList, final int isinvite) {
        userid2 = "";
        for (int i = 0; i < useridList.size(); i++) {
            if (i == 0) {
                userid2 += useridList.get(i);
            } else {
                userid2 += "," + useridList.get(i);
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = ConnectService.getIncidentbyHttpGet(AppConfig.URL_PUBLIC + "User/UserListBasicInfo?userIds=" + userid2);
                formatjson(jsonObject, isinvite);
            }
        }).start();
    }


    private void formatjson(JSONObject jsonObject, int isinvate) {
        try {
            int retCode = jsonObject.getInt("RetCode");
            switch (retCode) {
                case 0:
                    JSONArray jsarray = jsonObject.getJSONArray("RetData");
                    List<Customer> mylist = new ArrayList<>();
                    for (int j = 0; j < jsarray.length(); j++) {
                        JSONObject js = jsarray.getJSONObject(j);
                        Customer s = new Customer();
                        s.setUserID(js.getString("UserID"));
                        s.setName(js.getString("UserName"));
                        s.setUBAOUserID(js.getString("RongCloudID"));
                        s.setUrl(js.getString("AvatarUrl"));
                        mylist.add(s);
                    }
                    Message msg = Message.obtain();
                    msg.obj = mylist;
                    if (isinvate == 1) { // invate-meeting 返回的
                        msg.what = 0x1203;
                    } else if (isinvate == 2) { // join meeting  invate
                        msg.what = 0x1204;
                    }
                    handler.sendMessage(msg);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Popupdate2 puo2;

//    /**
//     * 加载PDF或PPT
//     */
//    @org.xwalk.core.JavascriptInterface
//    public void afterLoadPageFunction() {
//        Log.e("webview-afterLoadPage", "afterLoadPageFunction");
//        if (TextUtils.isEmpty(targetUrl)) {
//            return;
//        }
//        if (currentAttachmentPage.equals("0")) {
//            currentAttachmentPage = "1";
//        }
//        FileUtils fileUtils = new FileUtils(WatchCourseActivity3.this);
//        final String sourceName = targetUrl.substring(targetUrl.lastIndexOf("/") + 1);
//        String newName = "";
//        if (!TextUtils.isEmpty(sourceName)) {
//            int houzuiindex = sourceName.lastIndexOf(".");
//            String houzui = sourceName.substring(houzuiindex);
//            String baseurl = null;
//            try {
//                baseurl = EncoderByMd5(targetUrl);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            newName = baseurl + houzui;
//            newName = newName.replaceAll("/", "_");
//        }
//
//        File fileUtils1 = new File(fileUtils.getStorageDirectory());
//        if (!fileUtils1.exists()) {
//            fileUtils1.mkdir(); //创建目录
//        }
//        File fileUtils2 = new File(fileUtils.getStorageDirectory2());
//        if (!fileUtils2.exists()) {
//            fileUtils2.mkdir();  //创建目录
//        }
//
//        final String fileLocalUrl = fileUtils.getStorageDirectory() + File.separator + meetingId + "_" + newName;  //template  address
//        final String fileLocalUrl2 = fileUtils.getStorageDirectory2() + File.separator + meetingId + "_" + newName; //pdf address
//        if (puo2 != null) {
//            puo2.DissmissPop();
//        }
//        if (fileUtils.isFileExists(fileLocalUrl2)) {
//            if (httpHandler != null) {
//                httpHandler.cancel();
//                httpHandler = null;
//            }
//            Log.e("webview-downloadPdf", " 本地文件存在  show pdf 地址  " + fileLocalUrl2 + " currentAttachmentPage= " + currentAttachmentPage + "   currentBlankPageNumber= " + currentBlankPageNumber);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (wv_show != null) {
//                        wv_show.load("javascript:ShowPDF('" + fileLocalUrl2 + "', " + currentAttachmentPage + ",'')", null);
//                        wv_show.load("javascript:Record()", null);
//                    }
//                }
//            });
//        } else {
//            if (puo2 == null) {
//                puo2 = new Popupdate2();
//                puo2.getPopwindow(WatchCourseActivity3.this);
//            }
//            if (httpHandler != null) {
//                httpHandler.cancel();
//                httpHandler = null;
//                puo2.DissmissPop();
//            }
//            HttpUtils httpUtils = getInstance(WatchCourseActivity3.this);
//            httpHandler = httpUtils.download(targetUrl, fileLocalUrl, true, true,  // 过程中
//                    new RequestCallBack<File>() {
//                        @Override
//                        public void onStart() {
//                            puo2.StartPop(wv_show);
//                            super.onStart();
//                        }
//
//                        @Override
//                        public void onLoading(long total, long current,
//                                              boolean isUploading) {
//                            puo2.setProgress(total, current);
//                            super.onLoading(total, current, isUploading);
//                        }
//
//                        @Override
//                        public void onSuccess(ResponseInfo<File> arg0) {
//                            if (puo2 != null) {
//                                puo2.DissmissPop();
//                            }
//                            Log.e("webview-downloadPdf", " 下载成功  show pdf 地址  " + fileLocalUrl);
//                            new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    int retcode = Tools.copyFileSdCard(fileLocalUrl, fileLocalUrl2);
//                                    if (retcode == 0) {
//                                        Message message = Message.obtain();
//                                        message.obj = "javascript:ShowPDF('" + fileLocalUrl2 + "', " + currentAttachmentPage + ",'')";
//                                        message.what = 0x6115;
//                                        handler.sendMessage(message);
//                                    }
//                                }
//                            }).start();
//                        }
//
//                        @Override
//                        public void onFailure(HttpException arg0, String arg1) {
//                            if (puo2 != null) {
//                                puo2.DissmissPop();
//                            }
//                            Toast.makeText(WatchCourseActivity3.this, arg1, Toast.LENGTH_LONG).show();
//                        }
//
//                        @Override
//                        public void onCancelled() {
//                            if (puo2 != null) {
//                                puo2.DissmissPop();
//                            }
//                            super.onCancelled();
//                        }
//                    });
//        }
//    }

    /**
     * 加载PDF
     */
    @org.xwalk.core.JavascriptInterface
    public void afterLoadPageFunction() {
        Log.e("webview-afterLoadPage", "afterLoadPageFunction");
        crpage = (int) Float.parseFloat(currentAttachmentPage);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (crpage == 0) {
                    downloadPdf(targetUrl, 1);
                } else {
                    downloadPdf(targetUrl, crpage);
                    crpage = 0;
                }
            }
        });

    }

    int crpage = 0;
    private String prefixPdf;
    private int pageCount = 0; //每个PDF的总页数
    private String showpdfurl;

    private int getPdfCount(String pdfurl) {
        int first = pdfurl.lastIndexOf("<");
        int last = pdfurl.lastIndexOf(">");
        Log.e("hahh------------", first + "   " + last);
        if (first == 0 || last == 0) {  // 格式错误
            return 0;
        }
        if (last > first) {
            return Integer.parseInt(pdfurl.substring(first + 1, last));
        }
        return 0;
    }


    private void downloadPdf(final String pdfurl, final int page) {
        // https://peertime.cn/CWDocs/P49/Attachment/D3191/test_<10>.pdf
        if (TextUtils.isEmpty(pdfurl)) {
            return;
        }
        FileUtils fileUtils = new FileUtils(WatchCourseActivity3.this);
        File fileUtils1 = new File(fileUtils.getStorageDirectory());
        if (!fileUtils1.exists()) {
            fileUtils1.mkdir();
        }
        File fileUtils2 = new File(fileUtils.getStorageDirectory2());
        if (!fileUtils2.exists()) {
            fileUtils2.mkdir();
        }
        pageCount = getPdfCount(pdfurl);
        if (pageCount == 0) {
            return;
        }
        String sourceName = pdfurl.substring(pdfurl.lastIndexOf("/") + 1); // 得到test_<10>.pdf

        String houzui = sourceName.substring(sourceName.lastIndexOf("."));

        // 得到 https://peertime.cn/CWDocs/P49/Attachment/D3191/test_
        prefixPdf = pdfurl.substring(0, pdfurl.lastIndexOf("<"));

        // 得到下载地址  https:peertime.cn/CWDocs/P49/Attachment/D3191/test_1.pdf
        String downloadurl;
        Log.e("DDDDD", screenWidth + "  ");
        if (screenWidth > 1920) {
            downloadurl = prefixPdf + page + "_2K" + houzui;
        } else {
            downloadurl = prefixPdf + page + houzui;
        }

        String xxx = meetingId + "_" + EncoderByMd5(targetUrl).replaceAll("/", "_") + "_";

        //保存在本地的地址
        final String fileLocalUrl = fileUtils.getStorageDirectory() + File.separator
                + xxx + page + houzui;   // --->  /ubao/19999_xxxxx_1.pdf
        final String fileLocalUrl2 = fileUtils.getStorageDirectory2() + File.separator
                + xxx + page + houzui;   // --->  /ubao2/19999_xxxxx_1.pdf

        // show pdf 地址
        showpdfurl = fileUtils.getStorageDirectory2() + File.separator + xxx + sourceName.substring(sourceName.lastIndexOf("<"));  //--->  /ubao/xxxx_<10>.pdf
        Log.e("webview-downloadPdf", "保存在本地的地址  " + fileLocalUrl2 + "   show pdf 地址  " + showpdfurl + "  downloadurl" + downloadurl);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (puo2 != null) {
                    puo2.DissmissPop();
                }
            }
        });
        if (fileUtils.isFileExists(fileLocalUrl2)) {
            Log.e("webview-downloadPdf", " 本地文件存在  show pdf 地址  " + showpdfurl + "  " + page + "  " + currentBlankPageNumber);
            if (httpHandler != null) {
                httpHandler.cancel();
                httpHandler = null;
            }
            currentAttachmentPage = page + "";
            AppConfig.currentPageNumber = currentAttachmentPage;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wv_show.load("javascript:ShowPDF('" + showpdfurl + "', " + page + ",'" + currentAttachmentId + "')", null);
                    wv_show.load("javascript:Record()", null);
                }
            });
        } else {
            if (fileUtils.isFileExists(fileLocalUrl)) {
                new File(fileLocalUrl).delete();
            }
            if (httpHandler != null) {
                httpHandler.cancel();
                httpHandler = null;
            }
            HttpUtils httpUtils = getInstance(WatchCourseActivity3.this);
            httpHandler = httpUtils.download(downloadurl, fileLocalUrl, true, true,
                    new RequestCallBack<File>() {
                        @Override
                        public void onStart() {
                            if (puo2 != null) {
                                puo2.DissmissPop();
                                try {
                                    puo2.StartPop(findViewById(R.id.layout));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            super.onStart();
                        }

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {
                            if (puo2 != null) {
                                puo2.setProgress(total, current);
                            }
                            super.onLoading(total, current, isUploading);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<File> arg0) {
                            Log.e("arg0.statusCode", arg0.statusCode + "");
                            if (arg0.statusCode == 200) {
                                currentAttachmentPage = page + "";
                                AppConfig.currentPageNumber = currentAttachmentPage;
                                puo2.DissmissPop();
                                Log.e("webview-downloadPdf", " 下载成功  show pdf 地址  " + fileLocalUrl);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        int retcode = Tools.copyFileSdCard(fileLocalUrl, fileLocalUrl2);
                                        if (retcode == 0) {
                                            Message message = Message.obtain();
                                            message.obj = "javascript:ShowPDF('" + showpdfurl + "', " + page + ",'" + currentAttachmentId + "' )";
                                            message.what = 0x6115;
                                            handler.sendMessage(message);
                                        }
                                    }
                                }).start();
                            } else if (arg0.statusCode == 404) {
                                downloadPdf(pdfurl, page);
                            }
                        }

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            puo2.DissmissPop();
                            Log.e("duangduang", pdfurl + "    pppp   " + page);
                            if (httpHandler != null && !httpHandler.isCancelled()) {
                                if (arg0.getExceptionCode() == 404) {
                                    Toast.makeText(WatchCourseActivity3.this, arg1 + " download failed", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(WatchCourseActivity3.this, arg1 + " download again", Toast.LENGTH_LONG).show();
                                    downloadPdf(pdfurl, page);
                                }
                            }
                        }

                        @Override
                        public void onCancelled() {
                            super.onCancelled();
//                            if (puo2 != null) {
//                                puo2.DissmissPop();
//                            }
                        }
                    });
        }
    }


    @org.xwalk.core.JavascriptInterface
    public void preLoadFileFunction(final String url, final int currentpageNum, int number) {
        Log.e("webview-preLoadFile", url + "   " + currentpageNum);
        downEveryOnePdf(url, currentpageNum, true);
    }

    private String fileurl;

    private void downEveryOnePdf(final String url, final int currentpageNum, final boolean isdownload) {
        if (currentpageNum <= pageCount && currentpageNum >= 0) {
            //  storage/emulated/0/ubao2/1135_RVe3eJuht5FQWiW8rvM70Q==_<14>.pdf  2  0
            final String fileurl2 = url.substring(0, url.lastIndexOf("<")) + currentpageNum + url.substring(url.lastIndexOf("."));
            fileurl = url.substring(0, url.lastIndexOf("<")) + currentpageNum + url.substring(url.lastIndexOf("."));
            fileurl = fileurl.replace("ubao2", "ubao");
            FileUtils fileUtils = new FileUtils(WatchCourseActivity3.this);
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (puo2 != null) {
                        puo2.DissmissPop();
                    }
                }
            });*/
            if (fileUtils.isFileExists(fileurl2)) {
                Log.e("webview-downEveryPdf", "存在 --->" + currentpageNum);
                if (httpHandler != null) {
                    httpHandler.cancel();
                    httpHandler = null;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        afterDownloadFile(url, currentpageNum);
                    }
                });
            } else {
                if (fileUtils.isFileExists(fileurl)) {
                    new File(fileurl).delete();
                }
                // String downloadurl = prefixPdf + currentpageNum + url.substring(url.lastIndexOf("."));  // 得到 https://peertime.cn/CWDocs/P49/Attachment/D3191/test_2.pdf
                String downloadurl;

                if (screenWidth > 1920) {
                    downloadurl = prefixPdf + currentpageNum + "_2K" + url.substring(url.lastIndexOf("."));  // 得到 https://peertime.cn/CWDocs/P49/Attachment/D3191/test_2.pdf
                } else {
                    downloadurl = prefixPdf + currentpageNum + url.substring(url.lastIndexOf("."));  // 得到 https://peertime.cn/CWDocs/P49/Attachment/D3191/test_2.pdf
                }
                Log.e("webview-downloadPdf", screenWidth + "  " + "  downloadurl" + downloadurl);
                if (httpHandler != null) {
                    httpHandler.cancel();
                    httpHandler = null;
                }
                HttpUtils httpUtils = getInstance(WatchCourseActivity3.this);
                httpHandler = httpUtils.download(downloadurl, fileurl, true, true,
                        new RequestCallBack<File>() {
                            Long totalFile;

                            @Override
                            public void onStart() {
                                /*if (puo2 != null) {
                                    puo2.DissmissPop();
                                    try {
                                        puo2.StartPop(findViewById(R.id.layout));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }*/
                                super.onStart();
                            }

                            @Override
                            public void onLoading(long total, long current,
                                                  boolean isUploading) {
                                /*if (puo2 != null) {
                                    puo2.setProgress(total, current);
                                }*/
                                totalFile = total;
                                super.onLoading(total, current, isUploading);
                            }

                            @Override
                            public void onSuccess(final ResponseInfo<File> arg0) {
                                final File file = arg0.result;
                                Log.e("webview-ruiruirui", file.length() + "   " + totalFile + "   " + (totalFile == file.length()));
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wv_show.load("javascript:log('" + file.length() + "  " + totalFile + "   " + (totalFile == file.length()) + "')" + "  " + arg0.statusCode, null);
                                    }
                                });
                                if (totalFile == file.length()) {
                                    Log.e("webview-downEveryPdf", "不存在 下载完成" + "   " + file.getPath() + "   ");
//                                  puo2.DissmissPop();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            int retcode = Tools.copyFileSdCard(fileurl, fileurl2);
                                            if (retcode == 0) {
                                                wv_show.load("javascript:AfterDownloadFile('" + url + "', " + currentpageNum + ")", null);
                                            }
                                        }
                                    });
                                } else {
                                    downEveryOnePdf(url, currentpageNum, isdownload);
                                }
                            }

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
//                                puo2.DissmissPop();
                                Log.e("duangduang", url + "pppp" + currentpageNum + "pppp" + isdownload);
                                if (httpHandler != null && !httpHandler.isCancelled()) {
                                    if (arg0.getExceptionCode() == 404) {
                                        Toast.makeText(WatchCourseActivity3.this, arg1 + " download failed", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(WatchCourseActivity3.this, arg1 + " download again", Toast.LENGTH_LONG).show();
                                        downEveryOnePdf(url, currentpageNum, isdownload);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled() {
                                super.onCancelled();
//                                if (puo2 != null) {
//                                    puo2.DissmissPop();
//                                }
                            }
                        });
            }
        }
    }

    private void afterDownloadFile(final String url, final int currentpageNum) {
        Log.e("webview-afterDown", "afterDownloadFileafterDownloadFiterDownloadFileafterDownloadFile");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wv_show.load("javascript:AfterDownloadFile('" + url + "', " + currentpageNum + ")", null);
            }
        });
    }

    /**
     * 获取每一页上的 Action
     */
    @org.xwalk.core.JavascriptInterface
    public void afterChangePageFunction(final String pageNum) {
        Log.e("webview-afterChangePage", pageNum.toString());
        currentAttachmentPage = pageNum + "";
        AppConfig.currentPageNumber = currentAttachmentPage;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = ConnectService.getIncidentbyHttpGet(AppConfig.URL_PUBLIC + "PageObject/GetPageObjects?lessonID=" + lessonId + "&itemID=" + currentAttachmentId + "&pageNumber=" + pageNum);
                    Log.e("webview-afterChangePage", AppConfig.URL_PUBLIC + "PageObject/GetPageObjects?lessonID=" + meetingId + "&itemID=" + currentAttachmentId + "&pageNumber=" + pageNum + "   " + jsonObject.toString());
                    int retCode = jsonObject.getInt("RetCode");
                    switch (retCode) {
                        case 0:
                            JSONArray data = jsonObject.getJSONArray("RetData");
                            String mmm = "";
                            for (int i = 0; i < data.length(); i++) {
                                String ddd = data.getString(i);
                                if (!TextUtil.isEmpty(ddd)) {
                                    String dd = "'" + Tools.getFromBase64(ddd) + "'";
                                    if (i == 0) {
                                        mmm += "[" + dd;
                                    } else {
                                        mmm += "," + dd;
                                    }
                                    if (i == data.length() - 1) {
                                        mmm += "]";
                                    }
                                }
                            }
                            Message msg = Message.obtain();
                            msg.what = 0x4010;
                            msg.obj = mmm;
                            handler.sendMessage(msg);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    public String EncoderByMd5(String str) {
        try {
            //确定计算方法
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64en = new BASE64Encoder();
            //加密后的字符串
            String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
            return newstr;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static HttpUtils client;

    /**
     * 单例模式获得HttpUtils对象
     *
     * @return
     */
    public synchronized static HttpUtils getInstance(Context context) {
        if (client == null) {
            // 设置请求超时时间为10秒
            client = new HttpUtils(1000 * 10);
            client.configSoTimeout(1000 * 10);
            client.configResponseTextCharset("UTF-8");
            // 保存服务器端(Session)的Cookie
            PreferencesCookieStore cookieStore = new PreferencesCookieStore(
                    context);
            cookieStore.clear(); // 清除原来的cookie
            client.configCookieStore(cookieStore);
        }
        return client;
    }


    @org.xwalk.core.JavascriptInterface
    public void reflect(String result) {
        Log.e("webview-reflect", result);
        if (identity == 1) { // 学生
            String f = AppConfig.UserID.replaceAll("-", "").toString();
            Log.e("duang", (studentCustomer.getUserID() == null) + ":" + (f == null));
            if (TextUtils.isEmpty(studentCustomer.getUserID())) {
                return;
            } else {
                if (studentCustomer.getUserID().equals(f)) {
                    Log.e("webview-reflect", "学生");

                    if (isSync) {
                        if (!TextUtils.isEmpty(result)) {
                            try {
                                JSONObject jsonObject = new JSONObject(result);
                                if (isAgoraRecord) {
                                    jsonObject.put("time", System.currentTimeMillis() - startTime);
                                } else {
                                    jsonObject.put("time", tttime);
                                }
                                result = jsonObject.toString();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String newresult = Tools.getBase64(result).replaceAll("[\\s*\t\n\r]", "");
                    try {
                        JSONObject loginjson = new JSONObject();
                        loginjson.put("action", "ACT_FRAME");
                        loginjson.put("sessionId", studentCustomer.getUsertoken());
                        loginjson.put("retCode", 1);
                        loginjson.put("data", newresult);
                        loginjson.put("itemId", currentAttachmentId);
                        loginjson.put("sequenceNumber", "3837");
                        loginjson.put("ideaType", "document");
                        String ss = loginjson.toString();
                        SpliteSocket.sendMesageBySocket(ss);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (identity == 2) { //老师端 绘制
            if (currentPresenterId.equals(teacherCustomer.getUserID())) {
                Log.e("webview-reflect", "老师");
                if (isSync) {
                    if (!TextUtils.isEmpty(result)) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            if (isAgoraRecord) {
                                jsonObject.put("time", System.currentTimeMillis() - startTime);
                            } else {
                                jsonObject.put("time", tttime);
                            }
                            result = jsonObject.toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                String newresult = Tools.getBase64(result).replaceAll("[\\s*\t\n\r]", "");
                try {
                    JSONObject loginjson = new JSONObject();
                    loginjson.put("action", "ACT_FRAME");
                    loginjson.put("sessionId", teacherCustomer.getUsertoken());
                    loginjson.put("retCode", 1);
                    loginjson.put("data", newresult);
                    loginjson.put("itemId", currentAttachmentId);
                    loginjson.put("sequenceNumber", "3837");
                    loginjson.put("ideaType", "document");
                    String ss = loginjson.toString();
                    SpliteSocket.sendMesageBySocket(ss);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isWebViewLoadFinish = true;

    /**
     * pdf 加载完成
     */
    @org.xwalk.core.JavascriptInterface
    public void afterLoadFileFunction() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("webview-afterLoadFile", "afterLoadFileFunction");
                wv_show.load("javascript:CheckZoom()", null);
                if (!TextUtils.isEmpty(zoomInfo)) {
                    Message msg3 = Message.obtain();
                    msg3.obj = zoomInfo;
                    msg3.what = 0x1109;
                    handler.sendMessage(msg3);
                }
                if (currentPresenterId.equals(AppConfig.UserID)) {
                    Log.e("---------", currentPresenterId.equals(AppConfig.UserID) + "  dd  " + (wv_show == null));
                    wv_show.load("javascript:ShowToolbar(" + true + ")", null);
                    wv_show.load("javascript:Record()", null);
                } else {
                    Log.e("---------", currentPresenterId.equals(AppConfig.UserID) + "  dd  " + (wv_show == null));
                    wv_show.load("javascript:ShowToolbar(" + false + ")", null);
                    wv_show.load("javascript:StopRecord()", null);
                }
                isWebViewLoadFinish = true;
            }
        });
    }

    /**
     * 切换文档
     *
     * @param diff
     */
    @org.xwalk.core.JavascriptInterface
    public void autoChangeFileFunction(int diff) {
        Log.e("webview-autoChangeFile", diff + "");
        if (documentList.size() == 0) {
            return;
        }
        if (isHavePresenter()) {
            for (int i = 0; i < documentList.size(); i++) {
                LineItem line = documentList.get(i);
                if (line.getAttachmentID().equals(lineItem.getAttachmentID())) { //当前文档
                    if (diff == 1) {  // 往后一页
                        if (i == documentList.size() - 1) {  // 切换到首页
                            line.setSelect(false);
                            lineItem = documentList.get(0);
                        } else {
                            line.setSelect(false);
                            lineItem = documentList.get(i + 1);
                        }

                    } else if (diff == -1) {  //往前一页
                        if (i == 0) {
                            line.setSelect(false);
                            lineItem = documentList.get(documentList.size() - 1);  //切换到最后一页
                        } else {
                            line.setSelect(false);
                            lineItem = documentList.get(i - 1);
                        }
                    }
                    break;
                }
            }
            lineItem.setSelect(true);
            myRecyclerAdapter2.notifyDataSetChanged();
            currentAttachmentId = lineItem.getAttachmentID();
            currentAttachmentId2 = lineItem.getAttachmentID2();
            targetUrl = lineItem.getUrl();
            currentBlankPageNumber = lineItem.getBlankPageNumber();
            isHtml = lineItem.isHtml5();
            if (diff == 1) {
                currentAttachmentPage = "1";
                notifySwitchDocumentSocket(lineItem, "1");
            } else if (diff == -1) {
                if (!TextUtils.isEmpty(lineItem.getUrl())) {
                    currentAttachmentPage = getPdfCount(lineItem.getUrl()) + "";
                    notifySwitchDocumentSocket(lineItem, currentAttachmentPage);
                }
            }
            AppConfig.currentPageNumber = currentAttachmentPage;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wv_show.load("file:///android_asset/index.html", null);
                }
            });
        }
    }


    // 播放视频
    @org.xwalk.core.JavascriptInterface
    public void videoPlayFunction(final int vid) {
        Log.e("videoPlayFunction", vid + " videoPlayFunction");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webVideoPlay(vid, false, 0);
            }
        });
    }

    //打开
    @org.xwalk.core.JavascriptInterface
    public void videoSelectFunction(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (favoritePopup != null) {
                    favoritePopup.StartPop(findViewById(R.id.layout));
                    favoritePopup.setData(2, false);
                }
            }
        });
    }

    // 录制
    @org.xwalk.core.JavascriptInterface
    public void audioSyncFunction(final int id, final int isRecording) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webVideoPlay(id, true, isRecording);  //录音和录action
            }
        });
    }

    /**
     * 显示老师学生  信息的列表
     *
     * @param
     */
    private void initDisplayAudienceList() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater
                .inflate(R.layout.teacherlist_pop, null);
        RelativeLayout teacherll = (RelativeLayout) view.findViewById(R.id.teacherll);
        teacherll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow1.dismiss();
            }
        });
        teacherrecycler = (RecyclerView) view.findViewById(R.id.teacherrecycleview);
        auditorrecycleview = (RecyclerView) view.findViewById(R.id.auditorrecycleview);
        ImageView addauditorimageview = (ImageView) view.findViewById(R.id.addauditorimageview);
        addauditorimageview.setOnClickListener(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        teacherrecycler.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        auditorrecycleview.setLayoutManager(linearLayoutManager2);

        //老师和学生适配器
        teacherRecyclerAdapter = new TeacherRecyclerAdapter(this, teacorstudentList);
        teacherRecyclerAdapter.setOnItemClickListener(new TeacherRecyclerAdapter.OnItemClickListener2() {
            @Override
            public void onClick(Customer position) {
                initPopuptWindowPresenter(position);
            }
        });
        teacherrecycler.setAdapter(teacherRecyclerAdapter);
        //旁听者适配器
        myRecyclerAdapter = new MyRecyclerAdapter(this, auditorList);
        myRecyclerAdapter.setOnItemClickListener3(new MyRecyclerAdapter.OnItemClickListener3() {
            @Override
            public void onClick(int position) {
                if (identity == 2) {
                    if (auditorList.get(position).isEnterMeeting()) {
                        initPopuptWindowPromote(auditorList.get(position));
                    }
                }
            }
        });
        auditorrecycleview.setAdapter(myRecyclerAdapter);

        mPopupWindow1 = new PopupWindow(view, screenWidth,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                findViewById(R.id.bottomrl).setVisibility(View.VISIBLE);
            }
        });
        mPopupWindow1.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow1.setAnimationStyle(R.style.anination2);
        mPopupWindow1.setFocusable(true);

    }

    /**
     * 文档列表popupwindow
     *
     * @param
     */
    private void initDocumentListAudienceList() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater
                .inflate(R.layout.auditor_pop, null);
        RelativeLayout auditor = (RelativeLayout) view.findViewById(R.id.auditor);
        final LinearLayout upload_linearlayout = (LinearLayout) view.findViewById(R.id.upload_linearlayout);
        auditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upload_linearlayout.getVisibility() == View.VISIBLE) {
                    upload_linearlayout.setVisibility(View.GONE);
                } else {
                    documentPopupWindow.dismiss();
                }
            }
        });
        documentrecycleview = (RecyclerView) view.findViewById(R.id.recycleview2);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        documentrecycleview.setLayoutManager(linearLayoutManager3);
        ImageView selectfile = (ImageView) view.findViewById(R.id.selectfile);
        selectfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upload_linearlayout.getVisibility() == View.GONE) {
                    upload_linearlayout.setVisibility(View.VISIBLE);
                } else if (upload_linearlayout.getVisibility() == View.VISIBLE) {
                    upload_linearlayout.setVisibility(View.GONE);
                }

            }
        });
        RelativeLayout take_photo = (RelativeLayout) view.findViewById(R.id.take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_linearlayout.setVisibility(View.GONE);
                if (!Environment.MEDIA_MOUNTED.equals(Environment
                        .getExternalStorageState())) {
                    Toast.makeText(WatchCourseActivity3.this, "请插入SD卡", Toast.LENGTH_SHORT).show();
                    return;
                }
                closeAlbum();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
            }
        });
        RelativeLayout file_library = (RelativeLayout) view.findViewById(R.id.file_library);
        file_library.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_ALBUM);
                upload_linearlayout.setVisibility(View.GONE);
            }
        });

        RelativeLayout save_file = (RelativeLayout) view.findViewById(R.id.save_file);
        save_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload_linearlayout.setVisibility(View.GONE);
                LoginGet loginGet = new LoginGet();
                loginGet.setMyFavoritesGetListener(new LoginGet.MyFavoritesGetListener() {
                    @Override
                    public void getFavorite(ArrayList<Favorite> list) {
                        myFavoriteList.clear();
                        myFavoriteList.addAll(list);
                        openMySavePopup(myFavoriteList, 1);
                    }
                });
                loginGet.MyFavoriteRequest(WatchCourseActivity3.this, 1);
            }
        });
        documentPopupWindow = new PopupWindow(view, screenWidth,
                ViewGroup.LayoutParams.MATCH_PARENT);
        documentPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                findViewById(R.id.bottomrl).setVisibility(View.VISIBLE);
            }
        });
        documentPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        documentPopupWindow.setAnimationStyle(R.style.anination2);
        documentPopupWindow.setFocusable(true);

    }


    private InviteUserPopup inviteUserPopup;

    private void flipInviteUserPopupWindow() {
        inviteUserPopup = new InviteUserPopup();
        inviteUserPopup.getPopwindow(WatchCourseActivity3.this, meetingId);
        inviteUserPopup.setInvitePopupListener(new InviteUserPopup.InvitePopupListener() {
            @Override
            public void copyLink() {
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
            }

            @Override
            public void email(String url) {
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
                String[] targetemail = {"214176156@qq.com"};
                String[] email = {"1599528112@qq.com"};
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822"); // 设置邮件格式
                intent.putExtra(Intent.EXTRA_EMAIL, targetemail); // 接收人
                intent.putExtra(Intent.EXTRA_CC, email); // 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT, "这是邮件的主题部分"); // 主题
                intent.putExtra(Intent.EXTRA_TEXT, url); // 正文
                startActivity(Intent.createChooser(intent, "请选择邮件类应用"));
            }

            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);

            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
            }
        });
        inviteUserPopup.StartPop(wv_show);

    }

    private MoreactionPopup moreactionPopup;

    private void moreActionPopupWindow() {
        moreactionPopup = new MoreactionPopup();
        moreactionPopup.getPopwindow(WatchCourseActivity3.this);
        moreactionPopup.setInvitePopupListener(new MoreactionPopup.InvitePopupListener() {
            @Override
            public void mute() {
                initListen(false);
                sendIsHidden(0);
            }

            @Override
            public void unmute() {
                initListen(true);
                sendIsHidden(1);
            }

            @Override
            public void debug() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        wv_show.load("javascript:ShowDebugInfo('" + true + "')", null);
                    }
                });
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
            }

            @Override
            public void docrecord() {
                openYinxiangList(4);
            }

        });
        moreactionPopup.StartPop(testdebug, isHavePresenter(), isAgoraRecord);
    }


    private void sendIsHidden(int stat) {
        JSONObject json = new JSONObject();
        try {
            json.put("stat", stat);
            json.put("actionType", 14);
            json.put("userId", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
    }


    /**
     * 会话 popupwindow
     *
     * @param
     */
    private String mGroupId;
    private ListView chatListview;
    private ChatAdapter chatAdapter;
    private List<TextMessage> chatMessages = new ArrayList<>();

    private void initChatAudienceList() {
        mGroupId = getResources().getString(R.string.Classroom) + lessonId;
//        Toast.makeText(WatchCourseActivity3.this, mGroupId, Toast.LENGTH_LONG).show();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater
                .inflate(R.layout.chat_pop, null);
        RelativeLayout auditor = (RelativeLayout) view.findViewById(R.id.auditor);
        auditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chatPopupWindow.dismiss();
            }
        });
        chatListview = (ListView) view.findViewById(R.id.listview);
        chatAdapter = new ChatAdapter(WatchCourseActivity3.this, chatMessages);
        chatListview.setAdapter(chatAdapter);

        final EditText editText = (EditText) view.findViewById(R.id.edit);
        ImageView send = (ImageView) view.findViewById(R.id.send);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String context = editText.getText().toString();
                if (!TextUtils.isEmpty(context)) {
                    TextMessage myTextMessage = TextMessage.obtain(context);
                    myTextMessage.setExtra(AppConfig.UserID);
                    Tools.sendMessage(WatchCourseActivity3.this, myTextMessage, mGroupId, AppConfig.UserID);
                    editText.setText("");
                }
            }
        });
        ImageView icon_socket_chat = (ImageView) view.findViewById(R.id.icon_socket_chat);
        icon_socket_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.openGroup(WatchCourseActivity3.this, mGroupId);
                findViewById(R.id.bottomrl).setVisibility(View.GONE);
            }
        });
        chatPopupWindow = new PopupWindow(view, screenWidth,
                ViewGroup.LayoutParams.MATCH_PARENT);
        chatPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                findViewById(R.id.bottomrl).setVisibility(View.VISIBLE);
            }
        });
        chatPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        chatPopupWindow.setAnimationStyle(R.style.anination2);
        chatPopupWindow.setFocusable(true);

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventGroupInfo(io.rong.imlib.model.Message message) {
        getGroupinfo(1);
    }

    private BroadcastReceiver getGroupbroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getGroupinfo(1);
        }
    };


    public synchronized void getGroupinfo(int messageNumber) {
        RongIM.getInstance().getLatestMessages(Conversation.ConversationType.GROUP, mGroupId, messageNumber, new RongIMClient.ResultCallback<List<io.rong.imlib.model.Message>>() {
            @Override
            public void onSuccess(List<io.rong.imlib.model.Message> messages) {
                for (io.rong.imlib.model.Message message : messages) {
                    MessageContent m = message.getContent();
                    if (m instanceof TextMessage) {
                        chatMessages.add((TextMessage) m);
                    }
                }
                for (TextMessage message : chatMessages) {
                    for (int i1 = 0; i1 < teacorstudentList.size(); i1++) {
                        Customer customer = teacorstudentList.get(i1);
                        if (!TextUtils.isEmpty(message.getExtra())) {
                            if (message.getExtra().equals(customer.getUserID())) {
                                message.setExtra(customer.getName());
                                break;
                            }
                        }
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });
    }

    /**
     * socket 日志显示 popupwindow
     *
     * @param
     */
    private SocketLogAdapter socketLogAdapter;

    private BroadcastReceiver logBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (socketLogAdapter != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        socketLogAdapter.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    private PopupWindow socketPopupWindow;

    private void initSocketLogAudienceList() {

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.cn.socketlogpopup");
        registerReceiver(logBroadcastReceiver, intentFilter);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater
                .inflate(R.layout.chat_pop2, null);

        ListView listview = (ListView) view.findViewById(R.id.listview);
        ImageView clear = (ImageView) view.findViewById(R.id.clear);
        socketLogAdapter = new SocketLogAdapter(WatchCourseActivity3.this, AppConfig.socketList);
        listview.setAdapter(socketLogAdapter);

        socketPopupWindow = new PopupWindow(view, screenWidth,
                ViewGroup.LayoutParams.MATCH_PARENT);
        socketPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        socketPopupWindow.setAnimationStyle(R.style.anination2);
        socketPopupWindow.setFocusable(true);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socketPopupWindow.dismiss();
                chatPopupWindow.showAtLocation(wv_show, Gravity.BOTTOM, 0, 0);
                findViewById(R.id.bottomrl).setVisibility(View.GONE);
            }
        });
    }


    /**
     * 设置 presenter
     *
     * @param
     */
    private void initPopuptWindowPresenter(final Customer customer) {

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupWindow = layoutInflater
                .inflate(R.layout.course_popup, null);
        TextView cancel = (TextView) popupWindow.findViewById(R.id.cancel);
        TextView title = (TextView) popupWindow.findViewById(R.id.title);
        title.setText("Set presenter");
        RelativeLayout relativeLayout = (RelativeLayout) popupWindow.findViewById(R.id.ssss);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow2.dismiss();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customer.isPresenter()) {
                } else {
                    if (customer.getUserID().equals(teacherCustomer.getUserID())) { //点击了老师按钮
                        sendStringBySocket3("MAKE_PRESENTER", teacherCustomer.getUsertoken(), meetingId, teacherCustomer.getUsertoken());
                    } else { //点击了某一个学生按钮
                        if (!customer.getUserID().equals(studentCustomer.getUserID())) { //此学生不是presenter
                            sendStringBySocket3("MAKE_PRESENTER", teacherCustomer.getUsertoken(), meetingId, customer.getUsertoken());
                        }
                    }
                }
                mPopupWindow2.dismiss();
            }
        });
        mPopupWindow2 = new PopupWindow(popupWindow, screenWidth - 30,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().getDecorView().setAlpha(1f);
            }
        });
        mPopupWindow2.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow2.setAnimationStyle(R.style.anination2);
        mPopupWindow2.setFocusable(true);
        mPopupWindow2.showAtLocation(wv_show, Gravity.BOTTOM, 0, 20);
        getWindow().getDecorView().setAlpha(0.5f);

    }


    /**
     * 提升旁听者
     *
     * @param
     */
    private void initPopuptWindowPromote(final Customer customer) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View popupWindow = layoutInflater
                .inflate(R.layout.course_popup, null);
        TextView cancel = (TextView) popupWindow.findViewById(R.id.cancel);
        TextView title = (TextView) popupWindow.findViewById(R.id.title);
        title.setText("Promote to student");
        RelativeLayout relativeLayout = (RelativeLayout) popupWindow.findViewById(R.id.ssss);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow4.dismiss();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 提升旁听者为学生
                try {
                    JSONObject loginjson = new JSONObject();
                    loginjson.put("action", "PROMOTE_TO_STUDENT");
                    loginjson.put("sessionId", AppConfig.UserToken);
                    loginjson.put("meetingId", meetingId);
                    loginjson.put("userId", customer.getUserID());
                    String ss = loginjson.toString();
                    SpliteSocket.sendMesageBySocket(ss);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mPopupWindow4.dismiss();
            }
        });
        mPopupWindow4 = new PopupWindow(popupWindow, screenWidth - 30,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow4.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().getDecorView().setAlpha(1f);
            }
        });
        mPopupWindow4.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow4.setAnimationStyle(R.style.anination2);
        mPopupWindow4.setFocusable(true);
        mPopupWindow4.showAtLocation(wv_show, Gravity.BOTTOM, 0, 20);
        getWindow().getDecorView().setAlpha(0.5f);
    }


    private int currentPosition = -1;
    private FavoriteVideoPopup favoritePopup;

    private void openSaveVideoPopup() {
        favoritePopup = new FavoriteVideoPopup();
        favoritePopup.getPopwindow(WatchCourseActivity3.this);
        favoritePopup.setFavoritePoPListener(new FavoriteVideoPopup.FavoriteVideoPoPListener() {
            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);
            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
            }

            @Override
            public void selectFavorite(int position) {
//                Toast.makeText(WatchCourseActivity3.this, favoritePopup.getData().get(position).getTitle(), Toast.LENGTH_LONG).show();
                currentPosition = position;
            }

            @Override
            public void cancel() {
                wv_show.load("javascript:VideoTagAfterSelect(" + null + ")", null);
            }

            @Override
            public void save(int type, boolean isYinxiang) {
                if (type == 2) {  //video
                    if (currentPosition >= 0) {
                        JSONObject jsonObject = favoritePopup.getData().get(currentPosition).getJsonObject();
                        wv_show.load("javascript:VideoTagAfterSelect(" + jsonObject + ")", null);
                    }
                } else if (type == 3) {  // audio
                    if (currentPosition >= 0) {
                        JSONObject jsonObject = favoritePopup.getData().get(currentPosition).getJsonObject();
                        wv_show.load("javascript:VideoTagAfterSelect(" + jsonObject + ")", null);
                    }
                }

                if (isYinxiang) {
                    if (yinxiangCreatePopup != null && currentPosition >= 0) {
                        if (isrecord == 0) {
                            yinxiangCreatePopup.setAudioBean(favoritePopup.getData().get(currentPosition));
                        } else if (isrecord == 1) {
                            yinxiangCreatePopup.setRecordBean(favoritePopup.getData().get(currentPosition));
                        }
                    }
                    if (yinxiangEditPopup != null && currentPosition >= 0) {
                        if (isrecord == 0) {
                            yinxiangEditPopup.setAudioBean(favoritePopup.getData().get(currentPosition));
                        } else if (isrecord == 1) {
                            yinxiangEditPopup.setRecordBean(favoritePopup.getData().get(currentPosition));
                        }

                    }

                }
            }

            @Override
            public void uploadFile() {
                AddAlbum();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.addauditorimageview: //添加旁听者
                if (identity == 1 || identity == 2) {
                    Intent startAuditor = new Intent(WatchCourseActivity3.this, AddAuditorActivity.class);
                    startAuditor.putExtra("mAttendesList", (Serializable) teacorstudentList);
                    startAuditor.putExtra("teacher", teacherCustomer);
                    startAuditor.putExtra("student", studentCustomer);
                    startActivity(startAuditor);
                }
                break;
            case R.id.menu: //弹出file audience chat列表
                if (menu_linearlayout.getVisibility() == View.VISIBLE) {
                    menu_linearlayout.setVisibility(View.GONE);
                    menu.setImageResource(R.drawable.icon_menu);
                } else if (menu_linearlayout.getVisibility() == View.GONE) {
                    if (isPrepare) {
                        prepareclose.setVisibility(View.VISIBLE);
                        prepareStart.setVisibility(View.VISIBLE);
                        prepareScanTV.setVisibility(View.VISIBLE);
                        noprepare.setVisibility(View.GONE);
                    } else {
                        prepareclose.setVisibility(View.GONE);
                        prepareStart.setVisibility(View.GONE);
                        prepareScanTV.setVisibility(View.GONE);
                        noprepare.setVisibility(View.VISIBLE);
                    }


                    menu_linearlayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.hiddenwalkview).setVisibility(View.VISIBLE);
                    menu.setImageResource(R.drawable.icon_menu_active);
                    activte_linearlayout.setVisibility(View.GONE);
                    command_active.setImageResource(R.drawable.icon_command);
                }
                break;
            case R.id.prepareclose:
                finish();
                break;
            case R.id.prepareStart:
                if (!TextUtils.isEmpty(prepareMsg)) {
                    isPrepare = false;
                    command_active.setVisibility(View.VISIBLE);
                    menu_linearlayout.setVisibility(View.GONE);
                    isopenvideo(prepareMsg);
                    if (yinxiangmode == 0) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject jsonObject = ConnectService.submitDataByJson(AppConfig.URL_PUBLIC + "Lesson/UpgradeToNormalLesson?lessonID=" + meetingId, null);
                                Log.e("sssss", jsonObject.toString());
                            }
                        }).start();
                    }
                    yinxiangmode = 2;

                }
                break;
            case R.id.prepareScanTV:
                closeAlbum();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(WatchCourseActivity3.this, MipcaActivityCapture.class);
                        intent1.putExtra("isHorization", true);
                        startActivity(intent1);
                        activte_linearlayout.setVisibility(View.GONE);
                        command_active.setImageResource(R.drawable.icon_command);
                    }
                }, 200);
                break;
            case R.id.yinxiang:
                openYinxiangList(yinxiangmode);
                menu_linearlayout.setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                break;
            case R.id.command_active:  // 弹出语音
                if (activte_linearlayout.getVisibility() == View.VISIBLE) {
                    activte_linearlayout.setVisibility(View.GONE);
                    command_active.setImageResource(R.drawable.icon_command);
                } else if (activte_linearlayout.getVisibility() == View.GONE) {
                    activte_linearlayout.setVisibility(View.VISIBLE);
                    findViewById(R.id.hiddenwalkview).setVisibility(View.VISIBLE);
                    command_active.setImageResource(R.drawable.icon_command_active);
                    menu_linearlayout.setVisibility(View.GONE);
                    menu.setImageResource(R.drawable.icon_menu);
                }
                break;
            case R.id.refresh_notify_2:
                if (teacorstudentList.size() + auditorList.size() > 1) {
                    if (mWebSocketClient != null) {
                        try {
                            JSONObject loginjson = new JSONObject();
                            loginjson.put("action", "LEAVE_MEETING");
                            loginjson.put("sessionId", AppConfig.UserToken);
                            String ss = loginjson.toString();
                            SpliteSocket.sendMesageBySocket(ss);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mProgressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent();
                intent.setAction("com.cn.refreshsocket");
                sendBroadcast(intent);
                break;
            case R.id.displayAudience:
                mPopupWindow1.showAtLocation(wv_show, Gravity.BOTTOM, 0, 0);
                menu_linearlayout.setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                findViewById(R.id.bottomrl).setVisibility(View.GONE);
                break;
            case R.id.displayFile:
                documentPopupWindow.showAtLocation(wv_show, Gravity.BOTTOM, 0, 0);
                menu_linearlayout.setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                findViewById(R.id.bottomrl).setVisibility(View.GONE);
                break;
            case R.id.leavell:
                notifyleave();
                closeCourse(0);
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
                if (isHavePresenter()) {
                    if (isAgoraRecord) {
                        stopAgoraRecording(true);
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.endll:
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
                endDialog();
                break;
            case R.id.startll:
                startCourse();
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
                break;
            case R.id.testdebug:
                moreActionPopupWindow();
                break;
            case R.id.scanll:
                closeAlbum();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent1 = new Intent(WatchCourseActivity3.this, MipcaActivityCapture.class);
                        intent1.putExtra("isHorization", true);
                        startActivity(intent1);
                        activte_linearlayout.setVisibility(View.GONE);
                        command_active.setImageResource(R.drawable.icon_command);
                    }
                }, 200);
                break;
            case R.id.inviteuser:
                flipInviteUserPopupWindow();
                break;
            case R.id.joinvideo:
                openshengwang(0);
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
                break;
            case R.id.callmelater:
                callMeOrLater(identity, callMeLaterPhone);
                activte_linearlayout.setVisibility(View.GONE);
                command_active.setImageResource(R.drawable.icon_command);
                break;
            case R.id.displaychat:
                chatPopupWindow.showAtLocation(wv_show, Gravity.BOTTOM, 0, 0);
                menu_linearlayout.setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                findViewById(R.id.bottomrl).setVisibility(View.GONE);
                break;
            case R.id.displaywebcam:
                if (mViewType == VIEW_TYPE_DEFAULT) {
                    switchToBigVideoView();
                    changevideo(1, "");
                } else if (mViewType == VIEW_TYPE_NORMAL || mViewType == VIEW_TYPE_SING_NORMAL) {
                    switchToDefaultVideoView();
                    changevideo(0, "");
                }
                menu_linearlayout.setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                findViewById(R.id.bottomrl).setVisibility(View.VISIBLE);
                break;
            case R.id.displayvideo:
                videoPopuP.startVideoPop(wv_show, menu_linearlayout, menu);
                videoPopuP.setPresenter(identity,
                        currentPresenterId,
                        studentCustomer, teacherCustomer);
                break;
            case R.id.displayautocamera:  //偷拍
                toupai();
                menu_linearlayout.setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                findViewById(R.id.bottomrl).setVisibility(View.VISIBLE);
                break;

            case R.id.setting:
                menu_linearlayout.setVisibility(View.GONE);
                menu.setImageResource(R.drawable.icon_menu);
                findViewById(R.id.settingll).setVisibility(View.VISIBLE);
                rightViewEnter();
                break;
            case R.id.settingllback:
                if (right2.getVisibility() == View.VISIBLE) {
                    right2Out("");
                } else if (right3.getVisibility() == View.VISIBLE) {
                    right3Out("");
                } else {
                    rightViewOut();
                }
                break;
            case R.id.selectwebcam:
                right1.setVisibility(View.INVISIBLE);
                right3.setVisibility(View.INVISIBLE);
                right2.setVisibility(View.VISIBLE);
                right2Enter();
                break;
            case R.id.selectconnect:
                right1.setVisibility(View.INVISIBLE);
                right2.setVisibility(View.INVISIBLE);
                right3.setVisibility(View.VISIBLE);
                right3Enter();
                break;
            case R.id.select240:
                right2Out("Webcam240p");
                setArrow(1);
                break;
            case R.id.select360:
                right2Out("Webcam360p");
                setArrow(2);
                break;
            case R.id.select480:
                right2Out("Webcam480p");
                setArrow(3);
                break;
            case R.id.select720:
                right2Out("Webcam720p");
                setArrow(4);
                break;
            case R.id.peertimebase:
                if (mode != 0) {
                    right3value = "PeerTime-Based";
                    setRight3Arrow(0);
                }
                break;
            case R.id.kloudcall:
                if (mode != 1) {
                    right3value = "KloudCall";
                    setRight3Arrow(1);
                }
                break;
            case R.id.external:
                if (mode != 2) {
                    right3value = "External Tools or No Audio";
                    setRight3Arrow(2);
                }
                break;
            case R.id.right3bnt:
                if (mode == runnmode) { //选择相同

                } else {
                    TextView tv = (TextView) findViewById(R.id.right2value);
                    tv.setText(right3value);
                    switchline(mode);
                }
                findViewById(R.id.settingll).setVisibility(View.GONE);
                break;
            case R.id.leftview:
                findViewById(R.id.settingll).setVisibility(View.GONE);
                break;
            case R.id.llpre:
                break;
            case R.id.leavepre:
                notifyleave();
                closeCourse(0);
                if (isHavePresenter()) {
                    if (isAgoraRecord) {
                        stopAgoraRecording(true);
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
            case R.id.closeVideo:
                icon_command_mic_enabel.setEnabled(true);
//                initListen(true);
                initListen(audioStreamStatus);
                if (isFileVideo) {
                    sendVideoSocket(VIDEOSTATUSCLOSE, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 0);  //  Close
                } else {
                    changevideo(0, "");
                }
                videoPopuP.notifyDataChange(-1);
                findViewById(R.id.videoll).setVisibility(View.GONE);
                videoView.suspend();
                videoView.setVisibility(View.GONE);
                break;
            case R.id.timehidden:
                timeShow.setVisibility(View.VISIBLE);
                audiosyncll.setVisibility(View.GONE);
                break;
            case R.id.timeshow:
                timeShow.setVisibility(View.GONE);
                audiosyncll.setVisibility(View.VISIBLE);
                break;
            case R.id.createblabkpage:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject jsonObject = ConnectService.submitDataByJson(AppConfig.URL_PUBLIC + "EventAttachment/AddBlankPage?lessonID=" + lessonId, null);
                            Log.e("jjjjj", AppConfig.URL_PUBLIC + "EventAttachment/AddBlankPage?lessonID=" + lessonId + jsonObject.toString());
                            if (jsonObject.getInt("RetCode") == 0) {
                                try {
                                    JSONObject lineitem = jsonObject.getJSONObject("RetData");
                                    List<LineItem> items = new ArrayList<LineItem>();
                                    LineItem item = new LineItem();
                                    item.setFileName(lineitem.getString("Title"));
                                    item.setUrl(lineitem.getString("AttachmentUrl"));
                                    item.setHtml5(false);
                                    item.setAttachmentID(lineitem.getString("ItemID"));
                                    item.setAttachmentID2(lineitem.getString("AttachmentID"));
                                    item.setFlag(0);
                                    if (lineitem.getInt("Status") == 0) {
                                        items.add(item);
                                    }
                                    Message msg = Message.obtain();
                                    msg.obj = items;
                                    msg.what = 0x1123;
                                    handler.sendMessage(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.defaultpagehaha).setVisibility(View.GONE);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.inviteattendee:
                if (identity == 1 || identity == 2) {
                    Intent startAuditor = new Intent(WatchCourseActivity3.this, AddAuditorActivity.class);
                    startAuditor.putExtra("mAttendesList", (Serializable) teacorstudentList);
                    startAuditor.putExtra("teacher", teacherCustomer);
                    startAuditor.putExtra("student", studentCustomer);
                    startActivity(startAuditor);
                }
                break;
            case R.id.sharedocument:
                LoginGet loginGet = new LoginGet();
                loginGet.setMyFavoritesGetListener(new LoginGet.MyFavoritesGetListener() {
                    @Override
                    public void getFavorite(ArrayList<Favorite> list) {
                        myFavoriteList.clear();
                        myFavoriteList.addAll(list);
                        openMySavePopup(myFavoriteList, 1);
                    }
                });
                loginGet.MyFavoriteRequest(WatchCourseActivity3.this, 1);

                break;
            default:
                break;
        }
    }


    private YinxiangPopup yinxiangPopup;

    private void openYinxiangList(int yinxiangmode) {
        yinxiangPopup = new YinxiangPopup();
        yinxiangPopup.getPopwindow(WatchCourseActivity3.this);
        yinxiangPopup.setFavoritePoPListener(new YinxiangPopup.FavoritePoPListener() {
            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);
            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
            }

            @Override
            public void createYinxiang() {
                createYinxiangPopup();
            }

            @Override
            public void editYinxiang(SoundtrackBean soundtrackBean) {
                editYinxiangPopup(soundtrackBean);
            }

            @Override
            public void playYinxiang(SoundtrackBean soundtrackBean) {
                soundtrackID = soundtrackBean.getSoundtrackID();
                favoriteAudio = soundtrackBean.getBackgroudMusicInfo();
                getAudioAction(soundtrackBean.getSoundtrackID());
                String duration = soundtrackBean.getDuration();
                openAudioSync(false, true, TextUtils.isEmpty(duration) ? 0 : Long.parseLong(duration));
                if (favoriteAudio == null || favoriteAudio.getAttachmentID() == 0) {
                    GetMediaPlay("", false);
                    sendAudioSocket(1, soundtrackID);
                } else {
                    GetMediaPlay(favoriteAudio.getFileDownloadURL(), false);
                    sendAudioSocket(1, soundtrackID);
                }
                if (soundtrackBean.getNewAudioAttachmentID() != 0) {
                    GetMediaPlay2(soundtrackBean.getNewAudioInfo().getFileDownloadURL());
                } else if (soundtrackBean.getSelectedAudioAttachmentID() != 0) {
                    GetMediaPlay2(soundtrackBean.getSelectedAudioInfo().getFileDownloadURL());
                }
            }
        });
        if (yinxiangmode == 0) {
            yinxiangPopup.StartPop(wv_show, currentAttachmentId2, "", false, isHavePresenter());
        } else if (yinxiangmode == 1) {
            yinxiangPopup.StartPop(wv_show, currentAttachmentId2, lessonId, false, isHavePresenter());
        } else if (yinxiangmode == 2) {
            yinxiangPopup.StartPop(wv_show, currentAttachmentId2, lessonId, true, isHavePresenter());
        } else if (yinxiangmode == 4) {
            yinxiangPopup.StartPop(wv_show, "0", lessonId, true, isHavePresenter());
        }
    }


    private YinxiangCreatePopup yinxiangCreatePopup;
    private int soundtrackID = -1;
    private int isrecord = 0;


    private void createYinxiangPopup() {
        yinxiangCreatePopup = new YinxiangCreatePopup();
        yinxiangCreatePopup.getPopwindow(WatchCourseActivity3.this);
        yinxiangCreatePopup.setFavoritePoPListener(new YinxiangCreatePopup.FavoritePoPListener() {
            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);
            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
            }

            @Override
            public void addrecord(int ii) {
                if (favoritePopup != null) {
                    isrecord = ii;
                    favoritePopup.StartPop(findViewById(R.id.layout));
                    favoritePopup.setData(3, true);
                }
            }

            @Override
            public void addaudio(int ii) {
                if (favoritePopup != null) {
                    isrecord = ii;
                    favoritePopup.StartPop(findViewById(R.id.layout));
                    favoritePopup.setData(3, true);
                }
            }

            @Override
            public void syncorrecord(boolean checked, SoundtrackBean soundtrackBean2) {
                favoriteAudio = soundtrackBean2.getBackgroudMusicInfo();
                soundtrackID = soundtrackBean2.getSoundtrackID();
                String duration = soundtrackBean2.getDuration();
                openAudioSync(checked, false, TextUtils.isEmpty(duration) ? 0 : Long.parseLong(duration));
                if (favoriteAudio == null || favoriteAudio.getAttachmentID() == 0) {
                    GetMediaPlay("", checked);  // 录音
                } else {
                    GetMediaPlay(favoriteAudio.getFileDownloadURL(), checked);  // 录音
                }
                sendSync("AUDIO_SYNC", AppConfig.UserToken, 1, soundtrackID);
                if (soundtrackBean2.getNewAudioAttachmentID() != 0) {
                    GetMediaPlay2(soundtrackBean2.getNewAudioInfo().getFileDownloadURL());
                } else if (soundtrackBean2.getSelectedAudioAttachmentID() != 0) {
                    GetMediaPlay2(soundtrackBean2.getSelectedAudioInfo().getFileDownloadURL());
                }
            }
        });
        yinxiangCreatePopup.StartPop(wv_show, currentAttachmentId2);
    }


    /**
     * 绑定录音
     *
     * @param soundtrackID2
     * @param audioAttachmentID
     * @param isagorareco
     */
    private void bindYinxianginterface(final int soundtrackID2, final int audioAttachmentID, final boolean isagorareco, final boolean isClose) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject returnjson = ConnectService.submitDataByJson(AppConfig.URL_PUBLIC +
                            "Soundtrack/UpdateAudioAttachmentID?soundtrackID=" + soundtrackID2 + "&audioAttachmentID=" + audioAttachmentID, null);
                    Log.e("Agora", " hhh " + returnjson.toString());
                    if (returnjson.getInt("RetCode") == 0) {
                        soundtrackID = -1;
                        isAgoraRecord = false;
                        if (isagorareco) {
                            CheckAndMerge(isClose);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    /**
     * 合并录音并退出
     */
    private void CheckAndMerge(final boolean isclose) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject returnjson2 = ConnectService.submitDataByJson(AppConfig.URL_PUBLIC +
                            "Soundtrack/CheckAndMerge?lessonID=" + meetingId + "&recordingID=" + recordingId + "&meetingID=" + meetingId, null);
                    Log.e("Agora", AppConfig.URL_PUBLIC +
                            "Soundtrack/CheckAndMerge?lessonID=" + meetingId + "&recordingID=" + recordingId + "&meetingID=" + meetingId + " hhh " + returnjson2.toString());
                    if (isclose) {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private YinxiangEditPopup yinxiangEditPopup;

    private void editYinxiangPopup(SoundtrackBean soundtrackBean) {
        yinxiangEditPopup = new YinxiangEditPopup();
        yinxiangEditPopup.getPopwindow(WatchCourseActivity3.this);
        yinxiangEditPopup.setFavoritePoPListener(new YinxiangEditPopup.FavoritePoPListener() {
            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);
            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
            }

            @Override
            public void addrecord(int ii) {
                if (favoritePopup != null) {
                    isrecord = ii;
                    favoritePopup.StartPop(findViewById(R.id.layout));
                    favoritePopup.setData(3, true);
                }
            }

            @Override
            public void addaudio(int ii) {
                if (favoritePopup != null) {
                    isrecord = ii;
                    favoritePopup.StartPop(findViewById(R.id.layout));
                    favoritePopup.setData(3, true);
                }
            }

            @Override
            public void syncorrecord(boolean checked, SoundtrackBean soundtrackBean2) {
                favoriteAudio = soundtrackBean2.getBackgroudMusicInfo();
                soundtrackID = soundtrackBean2.getSoundtrackID();
                String duration = soundtrackBean2.getDuration();
                openAudioSync(checked, false, TextUtils.isEmpty(duration) ? 0 : Long.parseLong(duration));
                if (favoriteAudio == null || favoriteAudio.getAttachmentID() == 0) {
                    GetMediaPlay("", checked);
                } else {
                    GetMediaPlay(favoriteAudio.getFileDownloadURL(), checked);
                }
                sendSync("AUDIO_SYNC", AppConfig.UserToken, 1, soundtrackID);
                // 播放录音
                if (!TextUtils.isEmpty(soundtrackBean2.getSelectedAudioInfo().getFileDownloadURL())) {
                    GetMediaPlay2(soundtrackBean2.getSelectedAudioInfo().getFileDownloadURL());
                }
            }
        });
        yinxiangEditPopup.StartPop(wv_show, soundtrackBean);
    }


    private void getyinxiangdetail(final int soundtrackID2) {
        ServiceInterfaceTools.getinstance().getSoundItem(AppConfig.URL_PUBLIC + "Soundtrack/Item?soundtrackID=" + soundtrackID2,
                ServiceInterfaceTools.GETSOUNDITEM,
                new ServiceInterfaceListener() {
                    @Override
                    public void getServiceReturnData(Object object) {
                        SoundtrackBean soundtrackBean = (SoundtrackBean) object;
                        String duration = soundtrackBean.getDuration();
                        soundtrackID = soundtrackBean.getSoundtrackID();
                        openAudioSync(false, true, TextUtils.isEmpty(duration) ? 0 : Long.parseLong(duration));
                        if (soundtrackBean.getBackgroudMusicAttachmentID() != 0) {
                            GetMediaPlay(soundtrackBean.getBackgroudMusicInfo().getFileDownloadURL(), false);
                        } else {
                            GetMediaPlay("", false);
                        }
                        // 播放录音
                        if (soundtrackBean.getNewAudioAttachmentID() != 0) {
                            GetMediaPlay2(soundtrackBean.getNewAudioInfo().getFileDownloadURL());
                        } else if (soundtrackBean.getSelectedAudioAttachmentID() != 0) {
                            GetMediaPlay2(soundtrackBean.getSelectedAudioInfo().getFileDownloadURL());
                        }
                    }
                });
    }


    private int runnmode, mode = 0;
    private String right3value;

    private void switchline(int mode) {
        switch (mode) {
            case 0:  //切换peertime
                sendMessage(0);
                break;
            case 1: //kloudphone
                sendMessage(2);
                break;
            case 2:
                sendMessage(4);
                break;
            default:
                break;
        }
    }

    private void sendMessage(int value) {
        if (value == 0) {//切回peertime
            switcKcOrPeerTime(0);
            endConference2();
            runnmode = 0;
        } else if (value == 2) {  //kloudphone
            createConference(AppConfig.Mobile);
            runnmode = 1;
        } else if (value == 4) {  //External Tools or No Audio
            switcKcOrPeerTime(4);
            runnmode = 2;
        }
    }

    private void switcKcOrPeerTime(int mode) {
        JSONObject json = new JSONObject();
        try {
            json.put("actionType", 16);
            json.put("lineId", mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
        send_message("SEND_MESSAGE", AppConfig.UserToken, 1, teacherid, Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
    }

    private void createConference(String callPhone) {

        JSONObject json = new JSONObject();
        try {
            json.put("action", "CREATE_KLOUD_CALL_CONFERENCE");
            json.put("sessionId", AppConfig.UserToken);
            json.put("phoneNumber", callPhone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ss = json.toString();
        SpliteSocket.sendMesageBySocket(ss);
    }

    private NotificationPopup startLessonPopup;

    private void callMeOrLater(final int identity, String phoneNumber) {
        if (startLessonPopup != null && startLessonPopup.isShowing()) {
            return;
        }
        startLessonPopup = new NotificationPopup();
        startLessonPopup.getPopwindow(WatchCourseActivity3.this, identity, phoneNumber);
        startLessonPopup.setStartLessonPopupListener(new NotificationPopup.StartLessonPopupListener() {
            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);
            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
            }

            @Override
            public void callMe(String callPhone) {
                scallMe(callPhone);
            }

            @Override
            public void callLater(String callPhone) {
                callMeLaterPhone = callPhone;
            }

            @Override
            public void endConference() {
                endConference2();
            }
        });
        startLessonPopup.StartPop(wv_show);
    }


    private String callMeLaterPhone;

    private void scallMe(String callPhone) {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "CALL_ME");
            json.put("sessionId", AppConfig.UserToken);
            json.put("phoneNumber", callPhone);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ss = json.toString();
        SpliteSocket.sendMesageBySocket(ss);
    }


    private void endConference2() {
        JSONObject json = new JSONObject();
        try {
            json.put("action", "END_KLOUD_CALL_CONFERENCE");
            json.put("sessionId", AppConfig.UserToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String ss = json.toString();
        SpliteSocket.sendMesageBySocket(ss);
    }

    private void rightViewEnter() {
        leftview.setVisibility(View.INVISIBLE);
        final LinearLayout rightview = (LinearLayout) findViewById(R.id.rightview);
        final float curTranslationX3 = rightview.getTranslationX();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(rightview, "translationX", curTranslationX3 + screenWidth / 2, curTranslationX3).setDuration(300);
        animator3.setDuration(300);
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rightview.setTranslationX(curTranslationX3);
                leftview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator3.start();
    }


    private void rightViewOut() {

        final LinearLayout rightview = (LinearLayout) findViewById(R.id.rightview);
        final float curTranslationX3 = rightview.getTranslationX();
        ObjectAnimator animator3 = ObjectAnimator.ofFloat(rightview, "translationX", curTranslationX3, curTranslationX3 + screenWidth / 2).setDuration(300);
        animator3.setDuration(300);
        animator3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rightview.setTranslationX(curTranslationX3);
                leftview.setVisibility(View.GONE);
                findViewById(R.id.settingll).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator3.start();
    }

    private void right2Enter() {

        final float curTranslationX = right2.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(right2, "translationX", curTranslationX + screenWidth / 2, curTranslationX).setDuration(300);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                right2.setTranslationX(curTranslationX);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();

    }

    private void right2Out(final String value) {

        final float curTranslationX = right2.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(right2, "translationX", curTranslationX, curTranslationX + screenWidth / 2);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                right1.setVisibility(View.VISIBLE);
                right2.setVisibility(View.INVISIBLE);
                right3.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(value)) {
                    TextView tv = (TextView) findViewById(R.id.right1value);
                    tv.setText(value);
                }

                right2.setTranslationX(curTranslationX);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    private void right3Enter() {

        final float curTranslationX2 = right3.getTranslationX();
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(right3, "translationX", curTranslationX2 + screenWidth / 2, curTranslationX2);
        animator2.setDuration(300);
        animator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                right3.setTranslationX(curTranslationX2);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator2.start();

    }

    private void right3Out(final String value) {

        final float curTranslationX = right3.getTranslationX();
        ObjectAnimator animator = ObjectAnimator.ofFloat(right3, "translationX", curTranslationX, curTranslationX + screenWidth / 2);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                right1.setVisibility(View.VISIBLE);
                right2.setVisibility(View.INVISIBLE);
                right3.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(value)) {
                    TextView tv = (TextView) findViewById(R.id.right2value);
                    tv.setText(value);
                }
                right3.setTranslationX(curTranslationX);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }


    private void setArrow(int i) {

        ImageView right21 = (ImageView) findViewById(R.id.right21);
        ImageView right22 = (ImageView) findViewById(R.id.right22);
        ImageView right23 = (ImageView) findViewById(R.id.right23);
        ImageView right24 = (ImageView) findViewById(R.id.right24);
        right21.setVisibility(View.GONE);
        right22.setVisibility(View.GONE);
        right23.setVisibility(View.GONE);
        right24.setVisibility(View.GONE);
        switch (i) {
            case 1:
                right21.setVisibility(View.VISIBLE);
                break;
            case 2:
                right22.setVisibility(View.VISIBLE);
                break;
            case 3:
                right23.setVisibility(View.VISIBLE);
                break;
            case 4:
                right24.setVisibility(View.VISIBLE);
                break;
        }
    }


    private void setRight3Arrow(int i) {
        mode = i;
        ImageView right31 = (ImageView) findViewById(R.id.right31);
        ImageView right32 = (ImageView) findViewById(R.id.right32);
        ImageView right33 = (ImageView) findViewById(R.id.right33);
        right31.setVisibility(View.GONE);
        right32.setVisibility(View.GONE);
        right33.setVisibility(View.GONE);
        switch (i) {
            case 0:
                right31.setVisibility(View.VISIBLE);
                break;
            case 1:
                right32.setVisibility(View.VISIBLE);
                break;
            case 2:
                right33.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 老师开始上课
     */
    private void startCourse() {
        if (identity == 2) {
            JSONObject json = new JSONObject();
            try {
                json.put("meetingID", meetingId);
                json.put("sourceID", teacherid);
                json.put("targetID", studentid);
                json.put("incidentID", lessonId);
                json.put("roleType", "1");
                json.put("attachmentUrl", targetUrl);
                json.put("actionType", 11);
                json.put("isH5", isHtml);
            } catch (Exception e) {
                e.printStackTrace();
            }
            send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
            sendvoicenet("MEETING_STATUS", AppConfig.UserToken, meetingId, 1, lessonId);
        }
    }

    private WebCamPopup webCamPopuP;
    private boolean isOpenShengwang = false;

    private void openshengwang(int i) {
        if (webCamPopuP != null && webCamPopuP.isShowing()) {
            Log.e("isopenvideo", "1");
            return;
        }
        Log.e("isopenvideo", "2");
        webCamPopuP = new WebCamPopup();
        webCamPopuP.getPopwindow(WatchCourseActivity3.this, identity, firstStatus);
        webCamPopuP.setDefault(i);
        webCamPopuP.setWebCamPopupListener(new WebCamPopup.WebCamPopupListener() {
            @Override
            public void start(boolean isListen, boolean isMute2) {
                initListen(isListen);
                initMute(isMute2);
                isOpenShengwang = true;
                switchMode();
                toggle.setVisibility(View.VISIBLE);
                joinvideo.setVisibility(View.GONE);
                startll.setVisibility(View.GONE);
                leavell.setVisibility(View.VISIBLE);
                endtextview.setText("End");
                if (currentLine == LINE_PEERTIME) {  //  声网  模式
                } else if (currentLine == LINE_KLOUDPHONE) {   // kloudcall  模式
                    createConference(AppConfig.Mobile);
                } else if (currentLine == LINE_EXTERNOAUDIO) {  //  no audio  模式
                    switcKcOrPeerTime(4);
                }
            }

            @Override
            public void changeOptions(int position) {
                switch (position) {
                    case 0:
                        currentLine = LINE_PEERTIME;
                        webCamPopuP.setDefault(0);
                        break;
                    case 1:
                        currentLine = LINE_KLOUDPHONE;
                        webCamPopuP.setDefault(1);
                        break;
                    case 2:
                        currentLine = LINE_EXTERNOAUDIO;
                        webCamPopuP.setDefault(1);
                        break;
                }

            }

            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);
                menu.setEnabled(true);
                command_active.setEnabled(true);
                endtextview.setText("End");
            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
                menu.setEnabled(false);
                command_active.setEnabled(false);
            }
        });
        webCamPopuP.StartPop(wv_show);
        activte_linearlayout.setVisibility(View.GONE);
        command_active.setImageResource(R.drawable.icon_command);
    }

    private DetchedPopup detchedPopup;

    private void detectPopwindow(int countDown) {
        detchedPopup = new DetchedPopup();
        detchedPopup.getPopwindow(WatchCourseActivity3.this, (RelativeLayout) findViewById(R.id.layout), countDown);
        detchedPopup.setDetchedPopupListener(new DetchedPopup.DetchedPopupListener() {
            @Override
            public void closeNow() {
                detchedPopup.dismiss();
                endDialog();
            }
        });
        detchedPopup.StartPop(wv_show);
    }

    private void endDialog() {
        final ConfirmDialog confirmDialog = new ConfirmDialog(this, "Do you want to save Instant Lesson?", "Save and Exit", "Exit");
        confirmDialog.show();
        confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                confirmDialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject jsonObject = ConnectService.submitDataByJson(AppConfig.URL_PUBLIC + "Lesson/SaveInstantLesson?lessonID=" + lessonId, null);
                        Log.e("ennnnnnnnnnd", jsonObject.toString() + " " + isAgoraRecord);
                        try {
                            if (jsonObject.getInt("RetCode") == 0) {
                                notifyend();
                                closeCourse(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void doCancel() {
                confirmDialog.dismiss();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notifyend();
                        closeCourse(1);
                    }
                }, 0);
            }
        });
    }


    //-----------------------------------------------video-------------------
    private VideoPopup videoPopuP;
    private final int VIDEOSTATUSPLAY = 1;
    private final int VIDEOSTATUSPAUSE = 0;
    private final int VIDEOSTATUSCLOSE = 2;
    private String currentPlayVideoId;
    private int videoDuration; //视频总长度

    private void initVideoPopup() {
        videoPopuP = new VideoPopup();
        videoPopuP.getPopwindow(WatchCourseActivity3.this, screenWidth, (RelativeLayout) findViewById(R.id.bottomrl));
        // 两种情况
        videoView.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                if (isHavePresenter()) {
//                    initListen(false);
                    if (audioStreamStatus) {
                        initListen(false);
                    }
                    icon_command_mic_enabel.setEnabled(false);
                    sendVideoSocket(VIDEOSTATUSPLAY, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 0); //播放
                }
            }

            @Override
            public void onPause() {
                if (isHavePresenter()) {
//                    initListen(true);
                    initListen(audioStreamStatus);
                    sendVideoSocket(VIDEOSTATUSPAUSE, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 0); //暂停
                }
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (isHavePresenter()) {
//                    initListen(true);
                    initListen(audioStreamStatus);
                    icon_command_mic_enabel.setEnabled(true);
                    sendVideoSocket(VIDEOSTATUSCLOSE, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 0);  //  Close
                    videoPopuP.notifyDataChange(-1);
                    findViewById(R.id.videoll).setVisibility(View.GONE);
                    videoView.suspend();
                    videoView.setVisibility(View.GONE);
                    if (isFileVideo) {  // html
                        isFileVideo = false;
                    } else {
                        changevideo(0, "");
                    }
                }
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoDuration = videoView.getDuration();
                mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                    @Override
                    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                        int currentPosition = videoView.getCurrentPosition();
                        Log.e("onBufferingUpdate", "当前播放时间    " + currentPosition + "");
                    }
                });
            }
        });

        videoPopuP.setOnVideoListener(new VideoPopup.VideoListener() {
            @Override
            public void selectVideo() {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_CAPTURE_MEDIA);
            }

            @Override
            public void takePhoto() {
                Log.e("RRRRRRRRRRRRRRR", "拍视频");
                closeAlbum();
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
            }

            @Override
            public void selectPlayVideo(final int position) {
                LineItem lineitem = videoList.get(position);
                if (!lineitem.isSelect()) {
                    videoPopuP.notifyDataChange(position);
                    findViewById(R.id.videoll).setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    playVideo(lineitem.getUrl(), 0);
                    //该currentMode为4
                    changevideo(4, "");
                    //播放通知
                    currentPlayVideoId = videoList.get(position).getAttachmentID();
                    initListen(false);
                    icon_command_mic_enabel.setEnabled(false);
                    playUrl = lineitem.getUrl();
                    sendVideoSocket(VIDEOSTATUSPLAY, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 0);
                }
            }

            @Override
            public void openSaveVideo() { // 打开我的收藏video
                LoginGet loginGet = new LoginGet();
                loginGet.setMyFavoritesGetListener(new LoginGet.MyFavoritesGetListener() {
                    @Override
                    public void getFavorite(ArrayList<Favorite> list) {
                        myFavoriteVideoList.clear();
                        myFavoriteVideoList.addAll(list);
                        openMySavePopup(myFavoriteVideoList, 2);
                    }
                });
                loginGet.MyFavoriteRequest(WatchCourseActivity3.this, 2);
            }
        });
    }

    //-----------------------------------手势回调---------------------
    @Override
    public void onDown(MotionEvent e) {
        // 每次按下的时候更新当前亮度和音量，还有进度
        oldProgress = newProgress;
        oldVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        brightness = mLayoutParams.screenBrightness;
        if (brightness == -1) {
            //一开始是默认亮度的时候，获取系统亮度，计算比例值
            brightness = mBrightnessHelper.getBrightness() / 255f;
        }
    }

    @Override
    public void onEndFF_REW(MotionEvent e) {  // 0---100
        if (identity == 1) { // 学生
            if (TextUtils.isEmpty(studentCustomer.getUserID())) {
                return;
            }
            if (studentCustomer.getUserID().equals(AppConfig.UserID.replace("-", ""))) {
                videoView.seekTo(newProgress * videoDuration / 100);
                sendVideoSocket(VIDEOSTATUSPLAY, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 0); //播放
            }
        } else if (identity == 2) { //老师端 绘制
            if (currentPresenterId.equals(teacherCustomer.getUserID())) {
                videoView.seekTo(newProgress * videoDuration / 100);
                sendVideoSocket(VIDEOSTATUSPLAY, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 0); //播放
            }
        }
    }

    //音量回调
    @Override
    public void onVolumeGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        int value = videoGestureRelativeLayout.getHeight() / maxVolume;  //最大音量
        int newVolume = (int) ((e1.getY() - e2.getY()) / value + oldVolume);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, AudioManager.FLAG_PLAY_SOUND);
        //要强行转Float类型才能算出小数点，不然结果一直为0
        int volumeProgress = (int) (newVolume / Float.valueOf(maxVolume) * 100);
        if (volumeProgress >= 50) {
            showChangeLayout.setImageResource(R.drawable.volume_higher_w);
        } else if (volumeProgress > 0) {
            showChangeLayout.setImageResource(R.drawable.volume_lower_w);
        } else {
            showChangeLayout.setImageResource(R.drawable.volume_off_w);
        }
        showChangeLayout.setProgress(volumeProgress);  //设置进度条
        showChangeLayout.show();
    }


    /**
     * APP亮度的方法
     *
     * @param e1
     * @param e2
     * @param distanceX
     * @param distanceY
     */
    @Override
    public void onBrightnessGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float newBrightness = (e1.getY() - e2.getY()) / videoGestureRelativeLayout.getHeight();
        newBrightness += brightness;
        if (newBrightness < 0) {
            newBrightness = 0;
        } else if (newBrightness > 1) {
            newBrightness = 1;
        }
        mLayoutParams.screenBrightness = newBrightness;
        mWindow.setAttributes(mLayoutParams);
        showChangeLayout.setProgress((int) (newBrightness * 100));
        showChangeLayout.setImageResource(R.drawable.brightness_w);
        showChangeLayout.show();
    }

    /**
     * 快进快退手势，手指在Layout左右滑动的时候调用
     */
    @Override
    public void onFF_REWGesture(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float offset = e2.getX() - e1.getX();
        //根据移动的正负决定快进还是快退
        if (offset > 0) {
            showChangeLayout.setImageResource(R.drawable.ff);
            newProgress = (int) (oldProgress + offset / videoGestureRelativeLayout.getWidth() * 100);
            if (newProgress > 100) {
                newProgress = 100;
            }
        } else {
            showChangeLayout.setImageResource(R.drawable.fr);
            newProgress = (int) (oldProgress + offset / videoGestureRelativeLayout.getWidth() * 100);
            if (newProgress < 0) {
                newProgress = 0;
            }
        }

        showChangeLayout.setProgress(newProgress);
        showChangeLayout.show();
    }

    @Override
    public void onSingleTapGesture(MotionEvent e) {

    }

    @Override
    public void onDoubleTapGesture(MotionEvent e) {

    }
    //-----------------------------------手势回调---------------------

    private FavoritePopup favoriteFilePopup;

    private void openMySavePopup(final List<Favorite> favoriteList, final int type) {
        favoriteFilePopup = new FavoritePopup();
        if (uploadFavorList.size() > 0) {
            favoriteList.addAll(uploadFavorList);
        }
        favoriteFilePopup.getPopwindow(WatchCourseActivity3.this, favoriteList, type);
        favoriteFilePopup.setFavoritePoPListener(new FavoritePopup.FavoritePoPListener() {
            @Override
            public void dismiss() {
                getWindow().getDecorView().setAlpha(1.0f);
            }

            @Override
            public void open() {
                getWindow().getDecorView().setAlpha(0.5f);
            }

            @Override
            public void selectFavorite(int position) {
                favoriteFilePopup.dismiss();
                final Favorite favorite = favoriteList.get(position);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject js = new JSONObject();
                            js.put("lessonID", lessonId);
                            js.put("itemIDs", favorite.getItemID());
                            JSONObject jsonObject = ConnectService.submitDataByJson
                                    (AppConfig.URL_PUBLIC + "EventAttachment/UploadFromFavorite?lessonID=" + lessonId + "&itemIDs=" + favorite.getItemID(), js);
                            Log.e("save_file", js.toString() + "   " + jsonObject.toString());
                            Message msg = Message.obtain();
                            msg.what = 0x6002;
                            msg.obj = type;
                            handler.sendMessage(msg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void uploadFile() {
                favoriteFilePopup.dismiss();
                AddAlbum();
            }
        });
        favoriteFilePopup.StartPop(wv_show);
    }

    private void AddAlbum() {
        PopAlbums pa = new PopAlbums();
        pa.getPopwindow(getApplicationContext());
        pa.setPoPDismissListener(new PopAlbums.PopAlbumsDismissListener() {
            @Override
            public void PopDismiss(boolean isAdd) {
                if (isAdd) {
                    GetVideo();
                }
            }

            @Override
            public void PopDismissPhoto(boolean isAdd) {

            }

            @Override
            public void PopBack() {

            }
        });
        pa.StartPop(wv_show);
    }

    private void GetVideo() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_SAVE_MEDIA);
    }


    /**
     * @param state        当前播放状态 status == 0,暂停，== 1 播放，== 2:Close
     * @param time
     * @param attachmentID 视频vid
     * @param url          video url
     * @param videotype    video type 0: video in list 1: video in file
     */
    private void sendVideoSocket(int state, float time, String attachmentID, String url, int videotype) {
        JSONObject json = new JSONObject();
        try {
            json.put("stat", state);
            json.put("actionType", 19);
            json.put("time", time);
            json.put("vid", attachmentID);
            json.put("url", url);
            json.put("type", isFileVideo ? 1 : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
    }


    /**
     * 播放音频socket
     *
     * @param state
     * @param
     */
    private void sendAudioSocket(int state, int soundtrackID) {
        JSONObject json = new JSONObject();
        try {
            json.put("actionType", 23);
            json.put("soundtrackId", soundtrackID);
            json.put("stat", state);
            if (isHavePresenter() && state == 4) {
                json.put("time", tttime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
    }


    /**
     * 录制socket
     */
    private boolean isSync = false;

    private void sendSync(String action, String sessionId, int status, int audioitemid) {
        try {
            JSONObject loginjson = new JSONObject();
            loginjson.put("action", action);
            loginjson.put("sessionId", sessionId);
            loginjson.put("status", status);
            loginjson.put("soundtrackId", audioitemid);
            if (status == 0) {
                findViewById(R.id.recordstatus).setVisibility(View.GONE);
                if (!isAgoraRecord) {
                    loginjson.put("duration", tttime);
                } else {
                    loginjson.put("duration", System.currentTimeMillis() - startTime);
                }
            } else if (status == 1) {
                findViewById(R.id.recordstatus).setVisibility(View.VISIBLE);
            }
            String ss = loginjson.toString();
            SpliteSocket.sendMesageBySocket(ss);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status == 1) {
            isSync = true;
            getjspagenumber();
        } else {
            isSync = false;
        }

    }


    private void getjspagenumber() {
        wv_show.evaluateJavascript("javascript:GetCurrentPageNumber()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Log.e("GetCurrentPageNumber", s);
                int id = 1;
                if (TextUtils.isEmpty(s)) {

                } else {
                    id = Integer.parseInt(s);
                }
                JSONObject js = new JSONObject();
                try {
                    js.put("type", 2);
                    js.put("page", id);
                    js.put("time", 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String newresult = Tools.getBase64(js.toString()).replaceAll("[\\s*\t\n\r]", "");
                try {
                    JSONObject loginjson = new JSONObject();
                    loginjson.put("action", "ACT_FRAME");
                    loginjson.put("sessionId", teacherCustomer.getUsertoken());
                    loginjson.put("retCode", 1);
                    loginjson.put("data", newresult);
                    loginjson.put("itemId", currentAttachmentId);
                    loginjson.put("sequenceNumber", "3837");
                    loginjson.put("ideaType", "document");
                    String ss = loginjson.toString();
                    SpliteSocket.sendMesageBySocket(ss);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 学生收到消息是否播放视频
     *
     * @param stat
     * @param time
     * @param attachmentId
     */
    private void startOrPauseVideo(int stat, float time, String attachmentId, String url, int videotype) {
        Log.e("学生收到消息是否播放视频", stat + "    " + time + "         " + attachmentId + "    " + url + "    " + videotype);
        if (stat == VIDEOSTATUSPLAY) {  //play
//            initListen(false);
            if (audioStreamStatus) {
                initListen(false);
            }
            icon_command_mic_enabel.setEnabled(false);
            if (videoView.isPlaying()) {
                videoView.seekTo((int) time * 1000);
            } else {
                // 找到播放地址---------------------------
                String playurl = "";
                if (videotype == 0) {  //
                    for (int i = 0; i < videoList.size(); i++) {
                        LineItem item = videoList.get(i);
                        if (item.getAttachmentID().equals(attachmentId)) {
                            videoPopuP.notifyDataChange(i);
                            playurl = item.getUrl();
                            break;
                        }
                    }
                } else if (videotype == 1) {
                    playurl = url;
                }
                //找到播放地址---------------------------
                if (!TextUtils.isEmpty(playurl)) {
                    findViewById(R.id.videoll).setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    playVideo(playurl, time);
                }
            }
        } else if (stat == VIDEOSTATUSPAUSE) { //pause
//            initListen(true);
            initListen(audioStreamStatus);
            if (videoView.isPlaying()) {
                videoView.pause();
            }
        } else if (stat == VIDEOSTATUSCLOSE) {  // close
//            initListen(true);
            initListen(audioStreamStatus);
            icon_command_mic_enabel.setEnabled(true);
            videoPopuP.notifyDataChange(-1);
            findViewById(R.id.videoll).setVisibility(View.GONE);
            videoView.suspend();
            videoView.setVisibility(View.GONE);
        }

    }

    private boolean isFileVideo = false;

    /**
     * html 播放视频
     *
     * @param attachmentId
     */
    String playUrl;

    private void webVideoPlay(final int vid, final boolean playorrecord, final int isRecording) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject returnjson = ConnectService.getIncidentbyHttpGet(AppConfig.URL_PUBLIC +
                            "FavoriteAttachment/GetFavoriteAttachmentByID?itemID=" + vid);
                    Log.e("MyFavoriteAttachments", returnjson.toString());
                    if (returnjson.getInt("RetCode") == 0) {
                        final JSONObject jsonObject = returnjson.getJSONObject("RetData");
                        final String url = jsonObject.getString("AttachmentUrl");
                        final int filetype = jsonObject.getInt("FileType");

                        favoriteAudio = new Favorite();
                        favoriteAudio.setFileDownloadURL(jsonObject.getString("AttachmentUrl"));
                        favoriteAudio.setItemID(jsonObject.getInt("ItemID"));
                        favoriteAudio.setTitle(jsonObject.getString("Title"));
                        favoriteAudio.setAttachmentID(jsonObject.getInt("AttachmentID"));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (filetype == 4) {  //处理视频
                                    findViewById(R.id.videoll).setVisibility(View.VISIBLE);
                                    videoView.setVisibility(View.VISIBLE);
                                    playUrl = url;
                                    isFileVideo = true;
                                    playVideo(playUrl, 0.0f);
                                    // changevideo(4, "");  // 当前仍处于声网模式
                                    //播放通知
                                    currentPlayVideoId = vid + "";
                                    initListen(false); //
                                    icon_command_mic_enabel.setEnabled(false);
                                    sendVideoSocket(VIDEOSTATUSPLAY, (float) (videoView.getCurrentPosition() / 1000), currentPlayVideoId, playUrl, 1); //1 html 播放
                                } else if (filetype == 5) {  // 处理音频
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private AudioRecorder audioRecorder;

    private void startAudioRecord() {
        if (ContextCompat.checkSelfPermission(WatchCourseActivity3.this, Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(WatchCourseActivity3.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }
        if (audioRecorder != null) {
            audioRecorder.canel();  // 取消录音
        }
        audioRecorder = AudioRecorder.getInstance();
        try {
            if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_NO_READY) {
                Log.e("audioooooooooo", "startAudioRecord");
                String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
                audioRecorder.createDefaultAudio(fileName);
                audioRecorder.startRecord(null);
            }
        } catch (IllegalStateException e) {
            Toast.makeText(WatchCourseActivity3.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void stopAudioRecord(final int vid) {
        if (audioRecorder != null) {
            audioRecorder.stopRecord(new RecordEndListener() {
                @Override
                public void endRecord() {
                    Log.e("audioooooooooo", "录音结束，开始上传");
                    List<File> list = com.ub.service.audiorecord.FileUtils.getWavFiles();
                    //拿最后一个文件上传
                    if (list.size() > 0) {
                        File file = list.get(list.size() - 1);
                        uploadAudioFile(file, vid, false, false);
                    }
                }
            });
        }
    }


    private void pauseOrStartAudioRecord() {
        if (audioRecorder != null) {
            if (audioRecorder.getStatus() == AudioRecorder.Status.STATUS_START) {
                audioRecorder.pauseRecord();
            } else {
                audioRecorder.startRecord(null);
            }
        }
    }

    private void getAudioAction(final int soundtrackID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject1 = ConnectService.getIncidentbyHttpGet(AppConfig.URL_PUBLIC + "Soundtrack/SoundtrackActions?soundtrackID=" + soundtrackID);
                Log.e("音响action", jsonObject1.toString());
                try {
                    if (jsonObject1.getInt("RetCode") == 0) {
                        JSONObject retdata = jsonObject1.getJSONObject("RetData");
                        JSONArray jsonArray = retdata.getJSONArray("SoundtackActions");
                        List<AudioActionBean> audioActionBeanList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject audiojson = jsonArray.getJSONObject(i);
                            AudioActionBean audioActionBean = new AudioActionBean();
                            audioActionBean.setTime(audiojson.getInt("Time"));
                            String action = audiojson.getString("Data").replaceAll("\"", "");
                            String msg2 = Tools.getFromBase64(action);
                            audioActionBean.setData(msg2);
                            audioActionBeanList.add(audioActionBean);
                        }
                        Message msg3 = Message.obtain();
                        msg3.obj = audioActionBeanList;
                        msg3.what = 0x7001;
                        handler.sendMessage(msg3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void uploadAudioFile(File file, int vid, final boolean isagorareco, final boolean isClose) {
        final String title = file.getName();
        Log.e("audioooooooooo", "文件名：" + title);
        RequestParams params = new RequestParams();
        params.setHeader("UserToken", AppConfig.UserToken);
        params.addBodyParameter("Content-Type", "video/mpeg4"); // 设定传送的内容类型
        // params.setContentType("application/octet-stream");
        if (file.exists()) {
            params.addBodyParameter(title, file);
            String url = null;
            try {
                url = AppConfig.URL_PUBLIC + "EventAttachment/UploadRecordedVoice4App?LessonID=" + lessonId + "&DocItemID=" + currentAttachmentId + "&AudioItemID=" + vid + "&Title=" + URLEncoder.encode(LoginGet.getBase64Password(title)) + "&Description=description";
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("audioooooooooo", url);
            HttpUtils http = new HttpUtils();
            http.configResponseTextCharset("UTF-8");
            httpHandler = http.send(HttpRequest.HttpMethod.POST, url, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            puo = new Popupdate();
                            puo.getPopwindow(WatchCourseActivity3.this, title);
                            puo.setPopCancelListener(new Popupdate.PopCancelListener() {
                                @Override
                                public void Cancel() {
                                    if (httpHandler != null) {
                                        httpHandler.cancel();
                                        httpHandler = null;
                                    }
                                }
                            });
                            if (!isagorareco) {
                                puo.StartPop(wv_show);
                            } else if (isendmeeting) {
                                isendmeeting = false;
                                puo.StartPop(wv_show);
                            }
                        }

                        @Override
                        public void onLoading(final long total, final long current,
                                              boolean isUploading) {
                            if (puo != null) {
                                puo.setProgress(total, current);
                            }
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            if (puo != null) {
                                puo.DissmissPop();
                            }
                            try {
                                JSONObject jsonObject = new JSONObject(responseInfo.result);
                                JSONObject js = jsonObject.getJSONObject("RetData");
                                Log.e("hhh", jsonObject.toString());
                                if (js.getInt("Status") == 0) {
                                    int attachmentid = js.getInt("AttachmentID");
                                    if (soundtrackID > 0) {
                                        bindYinxianginterface(soundtrackID, attachmentid, isagorareco, isClose);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            if (puo != null) {
                                puo.DissmissPop();
                            }
                            Log.e("audioooooooooo", "failure " + msg);
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.nofile),
                    Toast.LENGTH_LONG).show();
        }
    }


    private MediaPlayer mediaPlayer2;

    private void GetMediaPlay2(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.reset();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
        mediaPlayer2 = new MediaPlayer();
        try {
            mediaPlayer2.setDataSource(url);
            mediaPlayer2.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer2.setLooping(true);
            mediaPlayer2.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    private void StopMedia2() {
        if (mediaPlayer2 != null) {
            mediaPlayer2.stop();
            mediaPlayer2.reset();
            mediaPlayer2.release();
            mediaPlayer2 = null;
        }
    }

    private void pauseMedia2() {
        if (mediaPlayer2 != null) {
            mediaPlayer2.pause();
        }
    }

    private void resumeMedia2() {
        if (mediaPlayer2 != null) {
            mediaPlayer2.start();
        }
    }


    /**
     * start video
     *
     * @param path
     * @param time
     */
    private void playVideo(final String path, final float time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Uri uri = Uri.parse(path);
                videoView.setVideoURI(uri);
                if (!videoView.isPlaying()) {
                    videoView.start();
                }
                videoView.seekTo((int) time * 1000);
                if (isHavePresenter()) {
                    MediaController mc = new MediaController(WatchCourseActivity3.this);
                    mc.setVisibility(View.VISIBLE);
                    closeVideo.setVisibility(View.VISIBLE);
                    videoView.setMediaController(mc);
                } else {
                    MediaController mc = new MediaController(WatchCourseActivity3.this);
                    mc.setVisibility(View.INVISIBLE);
                    closeVideo.setVisibility(View.GONE);
                    videoView.setMediaController(mc);
                }
            }
        });
    }


    /**
     * 获得video列表
     */
    private void getVideoList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject returnjson = com.ub.techexcel.service.ConnectService
                        .getIncidentbyHttpGet(AppConfig.URL_PUBLIC
                                + "Lesson/VideoList?lessonID=" + lessonId);
                Log.e("video文档列表", returnjson.toString());
                formatServiceData2(returnjson);
            }
        }).start();
    }

    private void formatServiceData2(JSONObject returnjson) {
        try {
            int retcode = returnjson.getInt("RetCode");
            if (retcode == 0) {
                videoList.clear();
                JSONArray lineitems = returnjson.getJSONArray("RetData");
                for (int i = 0; i < lineitems.length(); i++) {
                    JSONObject lineitem = lineitems.getJSONObject(i);
                    LineItem item = new LineItem();
                    item.setFileName(lineitem.getString("FileName"));
                    item.setUrl(lineitem.getString("AttachmentUrl"));
                    item.setHtml5(false);
                    item.setAttachmentID(lineitem.getString("AttachmentID"));
                    item.setBlankPageNumber(lineitem.getString("BlankPageNumber"));
                    videoList.add(item);
                }
                handler.sendEmptyMessage(0x2105);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void notifyleave() {
        boolean isExist = false;
        for (int i = 0; i < AppConfig.progressCourse.size(); i++) {
            if (AppConfig.progressCourse.get(i).getMeetingId().equals(meetingId)) {
                AppConfig.progressCourse.get(i).setStatus(true);
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            NotifyBean notifyBean = new NotifyBean();
            notifyBean.setMeetingId(meetingId);
            notifyBean.setStatus(true);
            AppConfig.progressCourse.add(notifyBean);
        }
        Intent intent = new Intent();
        intent.setAction(getResources().getString(R.string.Receive_Course));
        sendBroadcast(intent);
    }

    private void notifyend() {
        for (int i = 0; i < AppConfig.progressCourse.size(); i++) {
            if (AppConfig.progressCourse.get(i).getMeetingId().equals(meetingId)) {
                AppConfig.progressCourse.remove(i);
                Intent removeintent = new Intent();
                removeintent.setAction(getResources().getString(R.string.Receive_Course));
                sendBroadcast(removeintent);
                break;
            }
        }
    }


    /**
     * 获得PDF文档
     */
    private void getServiceDetail() {
        if (lessonId.equals("-1")) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject returnjson = com.ub.techexcel.service.ConnectService
                        .getIncidentbyHttpGet(AppConfig.URL_PUBLIC
                                + "Lesson/Item?lessonID=" + lessonId);
                formatServiceData(returnjson);
            }
        }).start();
    }


    private void formatServiceData(JSONObject returnJson) {
        Log.e("PDF文档列表", returnJson.toString());
        try {
            int retCode = returnJson.getInt("RetCode");
            switch (retCode) {
                case AppConfig.RETCODE_SUCCESS:
                    JSONObject service = returnJson.getJSONObject("RetData");
                    JSONArray lineitems = service.getJSONArray("AttachmentList");
                    List<LineItem> items = new ArrayList<LineItem>();
                    for (int j = 0; j < lineitems.length(); j++) {
                        JSONObject lineitem = lineitems.getJSONObject(j);
                        LineItem item = new LineItem();
                        item.setFileName(lineitem.getString("Title"));
                        item.setUrl(lineitem.getString("AttachmentUrl"));
                        item.setHtml5(false);
                        item.setAttachmentID(lineitem.getString("ItemID"));
                        item.setAttachmentID2(lineitem.getString("AttachmentID"));
                        item.setFlag(0);
                        if (lineitem.getInt("Status") == 0) {
                            items.add(item);
                        }
                    }
                    Message msg = Message.obtain();
                    msg.obj = items;
                    msg.what = 0x1123;
                    handler.sendMessage(msg);
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    /**
     * 结束课程   关闭此meeting
     */
    private void closeCourse(int id) {
        if (mWebSocketClient != null) {
            try {
                JSONObject loginjson = new JSONObject();
                if (id == 1) {
                    loginjson.put("action", "END_MEETING");
                } else if (id == 0) {
                    loginjson.put("action", "LEAVE_MEETING");
                }
                loginjson.put("sessionId", AppConfig.UserToken);
                String ss = loginjson.toString();
                SpliteSocket.sendMesageBySocket(ss);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            final ConfirmDialog confirmDialog = new ConfirmDialog(this, "Do you want to Leave Lesson?", "Yes", "No");
            confirmDialog.show();
            confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
                @Override
                public void doConfirm() {
                    notifyleave();
                    closeCourse(0);
                    if (isHavePresenter()) {
                        if (isAgoraRecord) {
                            stopAgoraRecording(true);
                        } else {
                            finish();
                        }
                    } else {
                        finish();
                    }
                }

                @Override
                public void doCancel() {
                    confirmDialog.dismiss();
                }
            });
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    //-------------------------------------------------------------------------------------------------
    private static final int REQUEST_CODE_CAPTURE_ALBUM = 0;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1;
    private static final int REQUEST_CODE_CAPTURE_MEDIA = 2;
    private static final int REQUEST_CODE_TAKE_PHOTO = 3;
    private static final int REQUEST_CODE_CAPTURE_SAVE_MEDIA = 4;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 系统相册返回
        if (requestCode == REQUEST_CODE_CAPTURE_ALBUM && resultCode == Activity.RESULT_OK
                && data != null) {                                   // 选择照片
            String path = FileUtils.getPath(this, data.getData());
            String pathname = path.substring(path.lastIndexOf("/") + 1);
            Log.e("path", path + "    " + pathname + "   " + data.getData());
            LineItem attachmentBean = new LineItem();
            attachmentBean.setUrl(path); // 文件的路径
            attachmentBean.setFileName(pathname);
            uploadFile(attachmentBean, false);
        }
        // 系统相机返回
        if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA && resultCode == Activity.RESULT_OK) {      // 拍照
            String name = DateFormat.format("yyyyMMdd_hhmmss",
                    Calendar.getInstance(Locale.CHINA))
                    + ".jpg";
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data"); // 获取相机返回的数据，并转换为Bitmap图片格式
            FileOutputStream b = null;
            File localFile = new File(cache, name);
            String path = localFile.getPath();
            String pathname = path.substring(path.lastIndexOf("/") + 1);
            try {
                b = new FileOutputStream(localFile.getPath());
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b); // 把数据写入文件
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Log.e("pathname", path + "   " + pathname);
            LineItem attachmentBean = new LineItem();
            attachmentBean.setUrl(path); // 文件的路径
            attachmentBean.setFileName(pathname); // 文件名
            uploadFile(attachmentBean, false);
            openAlbum();
        }
        if (requestCode == REQUEST_CODE_CAPTURE_MEDIA && resultCode == Activity.RESULT_OK) { // // 选择视频
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            cursor.close();
            LineItem attachmentBean = new LineItem();
            attachmentBean.setUrl(path); // 文件的路径
            String title = path.substring(path.lastIndexOf("/") + 1);
            attachmentBean.setFileName(title); // 文件名
            uploadFile(attachmentBean, true);
        }
        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {     //  拍摄视频
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            Log.e("-----", "path=" + path);
            LineItem attachmentBean = new LineItem();
            attachmentBean.setUrl(path);
            String title = path.substring(path.lastIndexOf("/") + 1);
            attachmentBean.setFileName(title);
            uploadFile(attachmentBean, true);
            openAlbum();
        }

        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_CANCELED) {
            openAlbum();
        }

        if (requestCode == REQUEST_CODE_CAPTURE_SAVE_MEDIA && resultCode == RESULT_OK) {  // 选择视频
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
            cursor.close();
            String title = path.substring(path.lastIndexOf("/") + 1);
            UpdateVideo(path, title);
        }
        if (wv_show != null) {
            wv_show.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void UpdateVideo(final String path, final String title) {
        Log.e("video", path + " : " + title);
        Message msg = new Message();
        final JSONObject jsonobject = null;
        String url = null;
        File file = new File(path);

        if (file.exists()) {
            try {
                url = AppConfig.URL_PUBLIC + "FavoriteAttachment/UploadFileWithHash?Title="
                        + URLEncoder.encode(LoginGet.getBase64Password(title), "UTF-8") +
                        /*"&schoolID=" +
                        SchoolID +*/
                        "&Hash=" +
                        Md5Tool.getMd5ByFile(file);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String finalUrl = url;
            Log.e("٩(ŏ﹏ŏ、)۶", url);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responsedata = ConnectService
                                .submitDataByJson(finalUrl, jsonobject);
                        String retcode = responsedata.getString("RetCode");
                        Log.e("UploadFileWithHash", responsedata.toString() + "");
                        Message msg = new Message();
                        if (retcode.equals(AppConfig.RIGHT_RETCODE)) {
                            msg.what = AppConfig.DELETESUCCESS;
                        } else if (retcode.equals(AppConfig.Upload_NoExist + "")) {
                            msg.what = AppConfig.Upload_NoExist;
                            UpdateVideo2(path, title);
                        } else if (retcode.equals(AppConfig.Upload_Exist + "")) {
                            msg.what = AppConfig.FAILED;
                            String ErrorMessage = responsedata
                                    .getString("ErrorMessage");
                            msg.obj = ErrorMessage;
                        }
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.nofile),
                    Toast.LENGTH_LONG).show();
        }

    }

    private Popupdate puo;

    private void UpdateVideo2(String path, final String title) {

        RequestParams params = new RequestParams();
        params.setHeader("UserToken", AppConfig.UserToken);
        params.addBodyParameter("Content-Type", "video/mpeg4");// 设定传送的内容类型
        // params.setContentType("application/octet-stream");
        File file = new File(path);
        if (file.exists()) {
            params.addBodyParameter(title, file);
            String url = null;
            try {
                url = AppConfig.URL_PUBLIC + "FavoriteAttachment/AddNewFavorite?Title="
                        + URLEncoder.encode(LoginGet.getBase64Password(title), "UTF-8") +
                        /*"&schoolID=" +
                        SchoolID
                        +*/ "&Hash=" + Md5Tool.getMd5ByFile(file);
                Log.e("hahaha", url + ":" + title);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Log.e("url", url);
            HttpUtils http = new HttpUtils();
            http.configResponseTextCharset("UTF-8");
            httpHandler = http.send(HttpRequest.HttpMethod.POST, url, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            puo = new Popupdate();
                            puo.getPopwindow(WatchCourseActivity3.this, title);
                            puo.setPopCancelListener(new Popupdate.PopCancelListener() {
                                @Override
                                public void Cancel() {
                                    if (httpHandler != null) {
                                        httpHandler.cancel();
                                        httpHandler = null;
                                    }
                                }
                            });
                            puo.StartPop(wv_show);
                        }

                        @Override
                        public void onLoading(final long total, final long current,
                                              boolean isUploading) {
                            if (puo != null) {
                                puo.setProgress(total, current);
                            }
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {
                            if (puo != null) {
                                puo.DissmissPop();
                            }
                            favoritePopup.setData(2, false);
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            if (puo != null) {
                                puo.DissmissPop();
                            }
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.nofile),
                    Toast.LENGTH_LONG).show();
        }

    }


    /**
     * 修改文件名
     *
     * @param attachmentBean
     */
    public void uploadFile(LineItem attachmentBean, final boolean isVideo) {
        isAddMyFavor(attachmentBean, isVideo);
    }


    /**
     * 是否添加到我的收藏
     *
     * @param attachmentBean
     * @param isVideo
     */
    private void isAddMyFavor(final LineItem attachmentBean, final boolean isVideo) {

        final Dialog passdDialog = new Dialog(this);
        View view2 = LayoutInflater.from(this).inflate(
                R.layout.isaddmyfavordialog, null);
        final CheckBox checkBox = (CheckBox) view2.findViewById(R.id.isaddfavorcb);
        checkBox.setChecked(false);
        TextView cancel = (TextView) view2.findViewById(R.id.cancel);
        TextView save = (TextView) view2.findViewById(R.id.save);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                passdDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                passdDialog.dismiss();
                UploadFileWithHash(attachmentBean, isVideo, checkBox.isChecked());
            }
        });
        passdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        passdDialog.setContentView(view2);
        Window dlgWindow = passdDialog.getWindow();
        dlgWindow.setGravity(Gravity.CENTER);
        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dlgWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.8);
        dlgWindow.setAttributes(p);
        passdDialog.setCanceledOnTouchOutside(false);
        passdDialog.show();

    }

    /**
     * 修改上传的文件名
     */
    private void popDialog(final LineItem attachmentBean, final boolean isVideo) {
        final Dialog passdDialog = new Dialog(this);
        View view2 = LayoutInflater.from(this).inflate(
                R.layout.personinfoeditdialog, null);
        TextView title = (TextView) view2.findViewById(R.id.title);
        title.setText(getString(R.string.editdocumentname));
        final EditText editText = (EditText) view2
                .findViewById(R.id.editcontent);
        if (TextUtils.isEmpty(attachmentBean.getFileName())) {

        } else {
            String name = "";
            if (attachmentBean.getFileName().lastIndexOf(".") > 0) {
                name = attachmentBean.getFileName().substring(0,
                        attachmentBean.getFileName().lastIndexOf("."));
            } else {
                name = attachmentBean.getFileName();
            }
            editText.setText(name);
            editText.setSelection(name.length());
        }
        TextView cancel = (TextView) view2.findViewById(R.id.cancel);
        TextView save = (TextView) view2.findViewById(R.id.save);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                passdDialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            private String houzui = "";

            @Override
            public void onClick(View arg0) {
                String content = editText.getText().toString();
                String sourceFileName = attachmentBean.getFileName();
                if (sourceFileName.lastIndexOf(".") > 0) {
                    houzui = sourceFileName.substring(sourceFileName
                            .lastIndexOf("."));
                }
                attachmentBean.setFileName(content + houzui);
                passdDialog.dismiss();
                UploadFileWithHash(attachmentBean, isVideo, false);
            }
        });
        passdDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        passdDialog.setContentView(view2);
        Window dlgWindow = passdDialog.getWindow();
        dlgWindow.setGravity(Gravity.CENTER);
        WindowManager m = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dlgWindow.getAttributes();
        p.width = (int) (d.getWidth() * 0.8);
        dlgWindow.setAttributes(p);
        passdDialog.setCanceledOnTouchOutside(false);
        passdDialog.show();

    }


    private void UploadFileWithHash(final LineItem attachmentBean, final boolean isVideo, final boolean isAddToFavorite) {
        Log.e("UploadFileWithHash", "UploadFileWithHash" + "");
        final JSONObject jsonobject = null;
        String url = null;
        File file = new File(attachmentBean.getUrl());
        String title = attachmentBean.getFileName();
        if (file.exists()) {
            try {
                url = AppConfig.URL_PUBLIC + "EventAttachment/UploadFileWithHash?LessonID=" + lessonId + "&Title="
                        + URLEncoder.encode(LoginGet.getBase64Password(title), "UTF-8") +
                        /*"&schoolID=" +
                        SchoolID +*/
                        "&Hash=" +
                        Md5Tool.getMd5ByFile(file);
                Log.e("UploadFileWithHash", url + "");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            final String finalUrl = url;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject responsedata = ConnectService
                                .submitDataByJson(finalUrl, jsonobject);
                        String retcode = responsedata.getString("RetCode");
                        Log.e("UploadFileWithHash", responsedata.toString() + "");
                        Message msg = new Message();
                        if (retcode.equals(AppConfig.RIGHT_RETCODE)) {  //刷新
                            msg.what = AppConfig.DELETESUCCESS;
                        } else if (retcode.equals(AppConfig.Upload_NoExist + "")) { // 添加
                            uploadFile2(attachmentBean, isVideo, isAddToFavorite);
                        } else if (retcode.equals(AppConfig.Upload_Exist + "")) { //不要重复上传
                            msg.what = AppConfig.FAILED;
                            String ErrorMessage = responsedata
                                    .getString("ErrorMessage");
                            msg.obj = ErrorMessage;
                        }
                        handler.sendMessage(msg);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.nofile),
                    Toast.LENGTH_LONG).show();
        }
    }


    private String fileNamebase;
    private HttpHandler httpHandler;

    private List<LineItem> uploadList = new ArrayList<>();

    public void uploadFile2(final LineItem attachmentBean, final boolean isVideo, boolean isAddToFavorite) {
        String fileName = attachmentBean.getFileName();
        attachmentBean.setFileName(fileName.replace(" ", "_"));
        RequestParams params = new RequestParams();
        params.setHeader("UserToken", AppConfig.UserToken);
        if (isVideo) {
            params.addBodyParameter("Content-Type", "video/mpeg4");// 设定传送的内容类型
        } else {
            params.addBodyParameter("Content-Type", "multipart/form-data");// 设定传送的内容类型
        }
        File file = new File(attachmentBean.getUrl());
        if (file.exists()) {
            String name = attachmentBean.getFileName();
            Log.e("filename----",
                    name + "      文件大小 " + file.length());
            params.addBodyParameter(name, file);
            String url = null;
            try {
                String baseurl = LoginGet.getBase64Password(name);
                fileNamebase = URLEncoder.encode(baseurl, "UTF-8");
                url = AppConfig.URL_PUBLIC + "EventAttachment/UploadFile?LessonID=" + lessonId + "&Title=" + fileNamebase + "&Hash=" + Md5Tool.getMd5ByFile(file) + "&IsAddToFavorite=" + (isAddToFavorite ? 1 : 0);
                Log.e("URRRRRRRRRL", url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("url", url);
            HttpUtils http = new HttpUtils();
            http.configResponseTextCharset("UTF-8");
            httpHandler = http.send(HttpRequest.HttpMethod.POST, url, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onStart() {
                            Log.e("iiiiiiiiii", "onStart");
                            attachmentBean.setAttachmentID(-1 + "");
                            attachmentBean.setFlag(1);
                            uploadList.add(attachmentBean);
                            documentList.add(attachmentBean);
                            if (myRecyclerAdapter2 == null) {
                                myRecyclerAdapter2 = new MyRecyclerAdapter2(WatchCourseActivity3.this, documentList);
                                documentrecycleview.setAdapter(myRecyclerAdapter2);
                            } else {
                                myRecyclerAdapter2.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onLoading(long total, long current,
                                              boolean isUploading) {
                            Log.e("iiiiiiiiii", total + "  " + current);
                            myRecyclerAdapter2.setProgress(total, current, attachmentBean);
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> responseInfo) {   // converting
                            Log.e("iiiiiiiiii", responseInfo.result);
                            try {
                                JSONObject jsonObject = new JSONObject(responseInfo.result);
                                JSONObject js = jsonObject.getJSONObject("RetData");
                                if (js.getInt("Status") == 10) { // 上传成功  开始转换
                                    int attachmentid = js.getInt("ItemID");
                                    attachmentBean.setAttachmentID(attachmentid + "");
                                    attachmentBean.setFlag(2);
                                    myRecyclerAdapter2.setProgress(1, 0, attachmentBean);
                                    final Timer timer = new Timer();
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            convertingPercentage(attachmentBean, timer);
                                        }
                                    };
                                    timer.schedule(timerTask, 0, 1000);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(HttpException error, String msg) {
                            Toast.makeText(getApplicationContext(),
                                    msg,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.nofile),
                    Toast.LENGTH_LONG).show();
        }
    }


    private void convertingPercentage(final LineItem attachmentBean, final Timer timer) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = ConnectService.getIncidentbyHttpGet(AppConfig.URL_PUBLIC + "Attachment/AttachmentConvertingPercentage?attachmentIDs=" + attachmentBean.getAttachmentID());
                Log.e("iiiiiiiiii4444", jsonObject.toString());
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("RetData");
                    if (jsonArray == null) {
                        timer.cancel();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String attachmentid = jsonObject1.getString("AttachmentID");
                        int status = jsonObject1.getInt("Status");
                        int Percentage = jsonObject1.getInt("Percentage");
                        if (status == 0) {
                            Log.e("iiiiiiiiii4444", "cancel");
                            attachmentBean.setProgress(100);
//                            uploadList.remove(attachmentBean);
//                            documentList.remove(attachmentBean); //进入刷新
                            timer.cancel();
                        } else {
                            if (attachmentid.equals(attachmentBean.getAttachmentID())) {
                                attachmentBean.setProgress(Percentage);
                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myRecyclerAdapter2.notifyDataSetChanged();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    //  ----------------------------------------------------------  video  ----------------------------------------------------
    private RecyclerView mLeftRecycler;
    private LeftAgoraAdapter mLeftAgoraAdapter;

    private RecyclerView mRightRecycler;
    private LeftAgoraAdapter mRightAgoraAdapter;

    private RecyclerView mBigRecycler;
    private BigAgoraAdapter mBigAgoraAdapter;
    private LinearLayout bigbg;

    private ImageView icon_command_mic_enabel;
    private ImageView icon_command_webcam_enable;
    private ImageView icon_command_switch;
    private ImageView icon_ear_active;

    private LinearLayout toggle;
    private LinearLayout togglelinearlayout;
    private boolean isShowDefaultVideo = false;
    private ImageView toggleicon;
    private ImageView icon_back;
    private final List<AgoraBean> mUidsList = new ArrayList<>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    private boolean isBroadcaster() {
        return isBroadcaster(config().mClientRole);
    }

    boolean isRoute = true;
    int cRole = 0;
    int clickNumber = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUIandEvent() {
        event().addEventHandler(this);
        cRole = Constants.CLIENT_ROLE_BROADCASTER;
        if (identity == 2 || identity == 1) { // 学生或老师
            cRole = Constants.CLIENT_ROLE_BROADCASTER;
        } else if (identity == 3) {   // 旁听者
            cRole = Constants.CLIENT_ROLE_AUDIENCE;
        }
        if (cRole == 0) {
            throw new RuntimeException("Should not reach here");
        }
        doConfigEngine(cRole);

        icon_command_mic_enabel = (ImageView) findViewById(R.id.icon_command_mic_enabel);
        icon_command_webcam_enable = (ImageView) findViewById(R.id.icon_command_webcam_enable);
        icon_ear_active = (ImageView) findViewById(R.id.icon_ear_active);
        icon_command_switch = (ImageView) findViewById(R.id.icon_command_switch);
        icon_back = (ImageView) findViewById(R.id.icon_back);
        icon_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changevideo(0, "");
                switchToDefaultVideoView();
            }
        });
        toggle = (LinearLayout) findViewById(R.id.toggle);
        togglelinearlayout = (LinearLayout) findViewById(R.id.togglelinearlayout);
        toggle.setOnTouchListener(new View.OnTouchListener() {
            private int startY;
            private int startX;
            private boolean isOnClick = true;
            private int endX, endY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isOnClick = true;
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        endX = startX;
                        endY = startY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int move_bigX = moveX - startX;
                        int move_bigY = moveY - startY;
                        Log.e("手指移动距离的大小", move_bigX + "    " + move_bigY);
                        if (Math.abs(move_bigX) > 0 || Math.abs(move_bigY) > 0) {
                            isOnClick = false;
                            //拿到当前控件未移动的坐标
                            int left = togglelinearlayout.getLeft();
                            int top = togglelinearlayout.getTop();
                            left += move_bigX;
                            top += move_bigY;
                            int right = left + togglelinearlayout.getWidth();
                            int bottom = top + togglelinearlayout.getHeight();
                            togglelinearlayout.layout(left, top, right, bottom);
                        } else {
                            isOnClick = true;
                        }
                        startX = moveX;
                        startY = moveY;
                        break;
                    case MotionEvent.ACTION_UP:
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.leftMargin = togglelinearlayout.getLeft();
                        params.topMargin = togglelinearlayout.getTop();
                        params.setMargins(togglelinearlayout.getLeft(), togglelinearlayout.getTop(), 0, 0);
                        togglelinearlayout.setLayoutParams(params);
                        Log.e("手指移动距离的大小", isOnClick + "");
                        int moveX1 = (int) event.getRawX();
                        int moveY1 = (int) event.getRawY();
                        int move_bigX1 = moveX1 - endX;
                        int move_bigY1 = moveY1 - endY;

                        if (isOnClick && Math.abs(move_bigX1) < 5 && Math.abs(move_bigY1) < 5) {
                            if (isShowDefaultVideo == true) {
                                isShowDefaultVideo = false;
//                                initMute(false); //禁止推流
                                toggleicon.setImageResource(R.drawable.eyeclose);
                                mLeftAgoraAdapter.setData(null, teacherid);
                                mLeftRecycler.setVisibility(View.GONE);
                                toggle.setTag(false);
                                if (isHavePresenter()) {
                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("actionType", 21);
                                        json.put("isHide", 1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
                                }

                            } else {
                                isShowDefaultVideo = true;
//                                initMute(true);
                                toggleicon.setImageResource(R.drawable.eyeopen);
                                mLeftAgoraAdapter.setData(mUidsList, teacherid);
                                mLeftRecycler.setVisibility(View.VISIBLE);
                                toggle.setTag(true);
                                if (isHavePresenter()) {
                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("actionType", 21);
                                        json.put("isHide", 0);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    send_message("SEND_MESSAGE", AppConfig.UserToken, 0, "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
                                }
                            }
                            activte_linearlayout.setVisibility(View.GONE);
                            command_active.setImageResource(R.drawable.icon_command);
                        }
                        break;
                }
                return true;
            }
        });
        toggleicon = (ImageView) findViewById(R.id.toggleicon);
        icon_command_mic_enabel.setTag(false);
        icon_command_mic_enabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Boolean) icon_command_mic_enabel.getTag()) {
                    initListen(false);
                } else {
                    initListen(true);
                }
            }
        });
        icon_ear_active.setOnClickListener(new View.OnClickListener() {  //耳机  语音路由
            @Override
            public void onClick(View view) {
                clickNumber++;
                if (clickNumber == 3) {
                    clickNumber = 0;
                    worker().getRtcEngine().muteAllRemoteAudioStreams(true);  //我想静一静
                    icon_ear_active.setImageResource(R.drawable.voiceallclose);
                } else {
                    worker().getRtcEngine().muteAllRemoteAudioStreams(false);  //我不想静一静
                    if (isRoute) {
                        isRoute = false;
                        worker().getRtcEngine().setDefaultAudioRoutetoSpeakerphone(false);
                        worker().getRtcEngine().setEnableSpeakerphone(false);
                        icon_ear_active.setImageResource(R.drawable.icon_ear_active);
                    } else {
                        isRoute = true;
                        worker().getRtcEngine().setDefaultAudioRoutetoSpeakerphone(true);
                        worker().getRtcEngine().setEnableSpeakerphone(true);
                        icon_ear_active.setImageResource(R.drawable.icon_voice_active_1);
                    }
                }
            }
        });
        icon_command_webcam_enable.setTag(false);
        icon_command_webcam_enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Boolean) icon_command_webcam_enable.getTag()) {
                    initMute(false);
                } else {
                    initMute(true);
                }
                openVideoByViewType();
            }
        });
        icon_command_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                worker().getRtcEngine().switchCamera();
            }
        });
        mLeftRecycler = (RecyclerView) findViewById(R.id.grid_video_view_container2);
        mLeftRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mRightRecycler = (RecyclerView) findViewById(R.id.grid_video_view_container3);
        mRightRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        mBigRecycler = (RecyclerView) findViewById(R.id.small_video_view_container);
        mBigRecycler.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        bigbg = (LinearLayout) findViewById(R.id.bigbg);
        mLeftAgoraAdapter = new LeftAgoraAdapter(WatchCourseActivity3.this);
        mLeftAgoraAdapter.setItemEventHandler(new VideoControlCallback() {
            @Override
            public void onItemDoubleClick(Object item) {
                changevideo(1, "");
                switchToBigVideoView();
            }
        });
        mLeftAgoraAdapter.setHasStableIds(true);
        mLeftRecycler.setAdapter(mLeftAgoraAdapter);
        mLeftRecycler.setDrawingCacheEnabled(true);
        mLeftRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        mLeftRecycler.setVisibility(View.GONE);
        toggle.setVisibility(View.GONE);
        mRightAgoraAdapter = new LeftAgoraAdapter(WatchCourseActivity3.this);
        mRightAgoraAdapter.setItemEventHandler(new VideoControlCallback() {
            @Override
            public void onSwitchVideo(AgoraBean item) {   // 切换大小屏
                switchVideo(item);
                changevideo(2, item.getuId() + "");
            }
        });
        mRightAgoraAdapter.setHasStableIds(true);
        mRightRecycler.setAdapter(mRightAgoraAdapter);
        mRightRecycler.setDrawingCacheEnabled(true);
        mRightRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        mBigAgoraAdapter = new BigAgoraAdapter(WatchCourseActivity3.this);
        mBigAgoraAdapter.setItemEventHandler(new VideoControlCallback() {
            @Override
            public void isEnlarge(AgoraBean user) {
                if (mViewType == VIEW_TYPE_NORMAL) {
                    if (mUidsList.size() > 1) {
                        switchVideo(user);
                        changevideo(2, user.getuId() + "");
                    }
                } else if (mViewType == VIEW_TYPE_SING_NORMAL) {
                    if (mUidsList.size() > 1) {
                        switchToBigVideoView();
                        changevideo(1, "");
                    }
                }
            }

            @Override
            public void closeOtherAudio(AgoraBean user) {
                // presenter 关闭其他人的Audio
                if (isHavePresenter() || config().mUid == user.getuId()) {  //有权限
                    JSONObject json = new JSONObject();
                    try {
                        json.put("stat", 0);
                        json.put("actionType", 14);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    send_message("SEND_MESSAGE", AppConfig.UserToken, 1, user.getuId() + "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
                }
            }

            @Override
            public void closeOtherVideo(AgoraBean user) {
                // presenter 关闭其他人的Video
                if (isHavePresenter() || config().mUid == user.getuId()) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("stat", 0);
                        json.put("actionType", 15);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    send_message("SEND_MESSAGE", AppConfig.UserToken, 1, user.getuId() + "", Tools.getBase64(json.toString()).replaceAll("[\\s*\t\n\r]", ""));
                }
            }

            @Override
            public void openMyAudio(AgoraBean user) {
                if (config().mUid == user.getuId()) {
                    initListen(true);
                }
                super.openMyAudio(user);
            }

            @Override
            public void openMyVideo(AgoraBean user) {
                if (config().mUid == user.getuId()) {
                    initMute(true);
                    openVideoByViewType();
                }
                super.openMyVideo(user);
            }
        });
        mBigAgoraAdapter.setHasStableIds(true);
        mBigRecycler.setAdapter(mBigAgoraAdapter);
        mBigRecycler.setDrawingCacheEnabled(true);
        mBigRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
        //加入频道
        worker().joinChannel(meetingId.toUpperCase(), config().mUid);
    }


    private boolean isHavePresenter() {
        if (identity == 1) { // 学生
            if (TextUtils.isEmpty(studentCustomer.getUserID())) {
                return false;
            }
            if (studentCustomer.getUserID().equals(AppConfig.UserID.replace("-", ""))) {
                return true;
            }
        } else if (identity == 2) {
            if (currentPresenterId.equals(teacherCustomer.getUserID())) {
                return true;
            }
        }
        return false;
    }

    /**
     * isVoice  true
     * false 话筒禁止
     */
    private boolean audioStreamStatus;//保存状态

    private void initListen(boolean isVoice) {
        audioStreamStatus = isVoice;
        icon_command_mic_enabel.setTag(isVoice);
        if (isVoice == false) { // false
            worker().getRtcEngine().muteLocalAudioStream(true);
            icon_command_mic_enabel.setImageResource(R.drawable.icon_command_mic_disable);
        } else if (isVoice == true) {
            worker().getRtcEngine().muteLocalAudioStream(false);
            icon_command_mic_enabel.setImageResource(R.drawable.icon_command_mic_enabel);
        }
    }

    /**
     * isMute false  不让推流
     * true    正常
     */
    private boolean videoStreamStatus;//保存状态

    private void initMute(boolean isMute) {
        icon_command_webcam_enable.setTag(isMute);
        videoStreamStatus = isMute;
        for (AgoraBean agoraBean : mUidsList) {
            if (agoraBean.getuId() == config().mUid) {
                agoraBean.setMuteVideo(!isMute);
            }
        }
        if (isMute == true) {  // true 可以推流
            worker().getRtcEngine().muteLocalVideoStream(false);
            icon_command_webcam_enable.setImageResource(R.drawable.icon_command_webcam_enable);
        } else if (isMute == false) {  // false 不让推流
            worker().getRtcEngine().muteLocalVideoStream(true);  //没有禁用摄像头
            icon_command_webcam_enable.setImageResource(R.drawable.icon_command_webcam_disable);
        }
    }

    private void closeAlbum() {
        worker().getRtcEngine().disableAudio();
        worker().getRtcEngine().enableLocalVideo(false);
    }

    private void openAlbum() {
        worker().getRtcEngine().enableAudio();
        worker().getRtcEngine().enableVideo();
        worker().getRtcEngine().enableLocalVideo(true);
    }

    private long currentTime = 0;

    private void doConfigEngine(int cRole) {
        int vProfile = Constants.VIDEO_PROFILE_480P;
        worker().configEngine(cRole, vProfile);
        //启用说话者音量提示
        worker().getRtcEngine().enableAudioVolumeIndication(200, 3);
        worker().getRtcEngine().enableWebSdkInteroperability(true);
        //记录当前时间
        currentTime = System.currentTimeMillis();
        Log.e("onAudioVolumeIndication", currentTime + ":");
        worker().getRtcEngine().enableVideo();
    }


    /**
     * onDestory
     */
    @Override
    protected void deInitUIandEvent() {
        AppConfig.isPresenter = false;
        AppConfig.status = "0";
        AppConfig.currentLine = 0;
        AppConfig.currentMode = "0";
        watch3instance = false;
        wl.release();
        doLeaveChannel();
        event().removeEventHandler(this);
        mUidsList.clear();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if (logBroadcastReceiver != null) {
            unregisterReceiver(logBroadcastReceiver);
        }
        if (getGroupbroadcastReceiver != null) {
            unregisterReceiver(getGroupbroadcastReceiver);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        Tools.removeGroupMeaage(mGroupId);

        sendAudioSocket(0, soundtrackID);

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (audiotimer != null) {
            audiotimer.cancel();
            audiotimer = null;
        }
        if (audioplaytimer != null) {
            audioplaytimer.cancel();
            audioplaytimer = null;
        }
        if (actionTimer != null) {
            actionTimer.cancel();
            actionTimer = null;
        }
        StopMedia2();
        if (audioRecorder != null) {
            audioRecorder.canel();  //取消录音
        }
        if (wv_show != null) {
            wv_show.removeAllViews();
            wv_show.onDestroy();
            wv_show = null;
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        StopMedia2();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (wv_show != null) {
            wv_show.pauseTimers();
            wv_show.onHide();
        }
        Log.e("onPPPause", "onPPPause");
        if (isJoinChannel) {
            if (togglelinearlayout.getVisibility() == View.VISIBLE) {
                isJoinChannel = false;
                doLeaveChannel();
                mUidsList.clear();
                mLeftAgoraAdapter.setData(null, "");
                togglelinearlayout.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (wv_show != null) {
            wv_show.onNewIntent(intent);
        }
    }

    private void doLeaveChannel() {
        worker().leaveChannel(config().mChannel);
        if (isBroadcaster()) {
            worker().preview(false, null, 0);
        }
    }


    private RelativeLayout remotevideoRelative;
    private FrameLayout remotevideoframe;

    /**
     * 远端视频接收解码回调
     *
     * @param uid
     * @param width
     * @param height
     * @param elapsed
     */
    @Override
    public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {

    }

    /**
     * @param uid
     * @return
     */
    private boolean isExistUid(int uid) {
        for (AgoraBean agoraBean : mUidsList) {
            if (agoraBean.getuId() == uid) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onJoinChannelSuccess(final String channel, final int uid, final int elapsed) {
        Log.e("RRRRRRRRRRRRRRRRRR", "onJoinChannelSuccess    " + uid + "  " + mUidsList.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }
                if (isExistUid(uid)) {
                    Log.e("RRRRRRRRRRRRRRRRRR", "已有    ");
                    return;
                }
                Log.e("RRRRRRRRRRRRRRRRRR", "没有    " + cRole);
                if (isBroadcaster(cRole)) {
                    Log.e("RRRRRRRRRRRRRRRRRR", "没有2    ");
                    AgoraBean agoraBean = new AgoraBean();
                    agoraBean.setuId(uid);
                    SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                    rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, 0));
                    surfaceV.setZOrderOnTop(true);
                    surfaceV.setZOrderMediaOverlay(true);
                    agoraBean.setSurfaceView(surfaceV);
                    for (int i1 = 0; i1 < teacorstudentList.size(); i1++) {
                        if (teacorstudentList.get(i1).getUserID().equals(uid + "")) {
                            agoraBean.setUserName(teacorstudentList.get(i1).getName());
                        }
                    }
                    mUidsList.add(agoraBean);
                }
                worker().getEngineConfig().mUid = uid;
                videoByUser();
                if (issetting) {
                    issetting = false;
                    initMute(videoStreamStatus);
                    initListen(audioStreamStatus);
                    openVideoByViewType();
                }

                if (!isPrepare) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startAgoraRecording();
                        }
                    }, 5000);

                }
            }
        });
    }

    String mAudioWavPath;
    private boolean isAgoraRecord = false;
    private long startTime;

    private void startAgoraRecording() {
        if (isHavePresenter()) {
            String url = AppConfig.URL_PUBLIC + "Soundtrack/CreateSoundtrack";
            ServiceInterfaceTools.getinstance().createYinxiang(url, ServiceInterfaceTools.CREATESOUNDTOLESSON, currentAttachmentId2, recordingId,
                    new ServiceInterfaceListener() {
                        @Override
                        public void getServiceReturnData(Object object) {
                            SoundtrackBean soundtrackBean = (SoundtrackBean) object;
                            soundtrackID = soundtrackBean.getSoundtrackID();
                            String AUDIO_WAV_BASEPATH = "/" + "pauseRecordDemo" + "/wav/";
                            String fileBasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + AUDIO_WAV_BASEPATH;
                            File file = new File(fileBasePath);
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            String fileName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()) + ".wav";
                            mAudioWavPath = fileBasePath + fileName;
                            File file2 = new File(mAudioWavPath);
                            int issuccess = worker().getRtcEngine().startAudioRecording(mAudioWavPath, 2);
                            Log.e("Agora", issuccess + "      " + mAudioWavPath + "  " + soundtrackID);
                            if (issuccess == 0) {
                                isAgoraRecord = true;
                                startTime = System.currentTimeMillis();
                                sendSync("AUDIO_SYNC", AppConfig.UserToken, 1, soundtrackID);
                            }
                        }
                    });
        }
    }

    private void stopAgoraRecording(boolean isClose) {
        sendSync("AUDIO_SYNC", AppConfig.UserToken, 0, soundtrackID);
        int d = worker().getRtcEngine().stopAudioRecording();
        //上传
        File file2 = new File(mAudioWavPath);
        if (file2.exists()) {
            Log.e("Agora", file2.getAbsolutePath() + "  " + d);
            uploadAudioFile(file2, soundtrackID, true, isClose);
        }
    }

    /**
     * 视频列表按user排序
     */
    private void videoByUser() {
        for (int i = teacherRecyclerAdapter.getmDatas().size() - 1; i >= 0; i--) {
            Customer customer = teacherRecyclerAdapter.getmDatas().get(i);
            if (TextUtils.isEmpty(customer.getUserID())) {
                return;
            } else {
                for (AgoraBean mData : mUidsList) {
                    AgoraBean agoraBean = new AgoraBean();
                    agoraBean.setuId(mData.getuId());
                    agoraBean.setSurfaceView(mData.getSurfaceView());
                    agoraBean.setMuteAudio(mData.isMuteAudio());
                    agoraBean.setMuteVideo(mData.isMuteVideo());
                    agoraBean.setUserName(mData.getUserName());
                    Log.e("duang1", (customer.getUserID() == null) + ":" + (mData.getuId() + ""));
                    if (customer.getUserID().equals(mData.getuId() + "")) {
                        mUidsList.add(0, agoraBean);
                        mUidsList.remove(mData);
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onUserJoined(final int uid, int elapsed) {
        Log.e("RRRRRRRRRRRRRRRRRR", "onUserJoined    " + uid + "  " + mUidsList.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uid < 1000000000) {
                    if (isFinishing()) {
                        return;
                    }
                    if (isExistUid(uid)) {
                    } else {
                        AgoraBean agoraBean = new AgoraBean();
                        agoraBean.setuId(uid);
                        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
                        surfaceV.setZOrderOnTop(true);
                        surfaceV.setZOrderMediaOverlay(true);
                        if (config().mUid == uid) {
                            rtcEngine().setupLocalVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                        } else {
                            rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
                        }
                        agoraBean.setSurfaceView(surfaceV);
                        for (int i1 = 0; i1 < teacorstudentList.size(); i1++) {
                            if (teacorstudentList.get(i1).getUserID().equals(uid + "")) {
                                agoraBean.setUserName(teacorstudentList.get(i1).getName());
                            }
                        }
                        mUidsList.add(agoraBean);
                    }
                    videoByUser();
                    openVideoByViewType();

                } else {
                    if (uid > 1000000000 && uid < 1500000000) {
                        openScreenShare(uid);
                    }
                }
            }
        });
    }


    private void openScreenShare(int uid) {
        if (isFinishing()) {
            return;
        }
        SurfaceView surfaceV = RtcEngine.CreateRendererView(getApplicationContext());
        surfaceV.setZOrderOnTop(true);
        surfaceV.setZOrderMediaOverlay(true);
        rtcEngine().setupRemoteVideo(new VideoCanvas(surfaceV, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        remotevideoframe = (FrameLayout) findViewById(R.id.remotevideoframe);
        remotevideoRelative = (RelativeLayout) findViewById(R.id.remotevideoRelative);
        remotevideoRelative.setVisibility(View.VISIBLE);
        if (remotevideoframe.getChildCount() == 0) {
            ViewParent parent = surfaceV.getParent();
            if (parent != null) {
                ((FrameLayout) parent).removeView(surfaceV);
            }
        }
        remotevideoframe.addView(surfaceV, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    //关闭屏幕共享
    private void closeScreenShare() {
        remotevideoframe = (FrameLayout) findViewById(R.id.remotevideoframe);
        remotevideoRelative = (RelativeLayout) findViewById(R.id.remotevideoRelative);
        SurfaceView surfaceView = (SurfaceView) remotevideoframe.getChildAt(0);
        if (surfaceView != null) {
            surfaceView.setVisibility(View.GONE);
        }
        remotevideoframe.removeAllViews();
        remotevideoframe = null;
        remotevideoRelative.setVisibility(View.GONE);
    }


    @Override
    public void onUserOffline(final int uid, int reason) {
        Log.e("RRRRRRRRRRRRRRRRRR", "onUserOffline    " + uid + "  " + mUidsList.size());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (uid > 1000000000 && uid < 1500000000) {
                    closeScreenShare();
                } else if (uid < 1000000000) {
                    if (isFinishing()) {
                        return;
                    }
                    for (int i1 = 0; i1 < mUidsList.size(); i1++) {
                        AgoraBean agoraBean = mUidsList.get(i1);
                        if (agoraBean.getuId() == uid) {
                            mUidsList.remove(agoraBean);
                        }
                    }
                    int bigBgUid = -1;
                    if (mViewType == VIEW_TYPE_DEFAULT || uid == bigBgUid) {
                        switchToDefaultVideoView();
                        Log.e("wahaha", 1 + "");
                    } else {
                        if (mViewType == VIEW_TYPE_NORMAL) {
                            if (mUidsList.size() >= 1) {
                                switchToBigVideoView();
                            }
                        } else if (mViewType == VIEW_TYPE_SING_NORMAL) {
                            if (mUidsList.size() > 1) {
                                switchVideo(mUser);
                            }
                        }
                    }
                }

            }
        });
    }


    public static final int AUDIO_ROUTE_HEADSET = 0;
    public static final int AUDIO_ROUTE_EARPIECE = 1;
    public static final int AUDIO_ROUTE_SPEAKERPHONE = 3;
    public static final int AUDIO_ROUTE_HEADSETBLUETOOTH = 5;

    /**
     * 语音路由已变更回调
     *
     * @param routing
     */
    @Override
    public void onAudioRouteChanged(final int routing) {
        Log.e("onAudioRouteChanged", routing + "    语音路由 ");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (routing) {
                    case AUDIO_ROUTE_HEADSET:
                        icon_ear_active.setEnabled(false);
                        isRoute = false;
                        icon_ear_active.setImageResource(R.drawable.icon_headphone_active);
                        break;
                    case AUDIO_ROUTE_EARPIECE:
                        icon_ear_active.setEnabled(true);
                        isRoute = false;
                        icon_ear_active.setImageResource(R.drawable.icon_ear_active);
                        break;
                    case AUDIO_ROUTE_SPEAKERPHONE:
                        icon_ear_active.setEnabled(true);
                        isRoute = true;
                        icon_ear_active.setImageResource(R.drawable.icon_voice_active_1);
                        break;
                    case AUDIO_ROUTE_HEADSETBLUETOOTH:
                        icon_ear_active.setEnabled(false);
                        isRoute = false;
                        icon_ear_active.setImageResource(R.drawable.icon_headphone_active);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 其他用户已停发/已重发视频流
     *
     * @param uid
     * @param muted
     */
    @Override
    public void onUserMuteVideo(final int uid, final boolean muted) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (AgoraBean agoraBean : mUidsList) {
                    if (agoraBean.getuId() == uid) {
                        agoraBean.setMuteVideo(muted);
                    }
                }
                openVideoByViewType();
            }
        });
    }

    /**
     * 其他用户已停发/已重发音频流
     *
     * @param uid
     * @param muted
     */
    @Override
    public void onUserMuteAudio(final int uid, final boolean muted) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (AgoraBean agoraBean : mUidsList) {
                    if (agoraBean.getuId() == uid) {
                        agoraBean.setMuteAudio(muted);
                    }
                }
                if (mViewType == VIEW_TYPE_SING_NORMAL || mViewType == VIEW_TYPE_SING_NORMAL) {
                    openVideoByViewType();
                }
            }
        });
    }

    /**
     * 远端视频统计回调
     *
     * @param stats
     */
    @Override
    public void onRemoteVideoStats(final IRtcEngineEventHandler.RemoteVideoStats stats) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (stats.uid > 1000000000) {
                    Log.e("-------doRenderRemoteUi", "onRemoteVideoStats");
                    int[] matrix = getMaxSizeSgareScreenWithWidth(stats.width, stats.height);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) remotevideoframe.getLayoutParams();
                    params.width = matrix[0];
                    params.height = matrix[1];
                    remotevideoframe.setLayoutParams(params);
                }
            }
        });
    }

    private int[] getMaxSizeSgareScreenWithWidth(int width, int height) {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int screenwidth = mDisplayMetrics.widthPixels;
        int screenheight = mDisplayMetrics.heightPixels;
        float scale = (float) height / (float) width;
        int[] matrix = new int[2];
        if (scale > 0) {
            if (screenwidth * scale > screenheight) {
                matrix[0] = (int) (screenheight / scale);
                matrix[1] = screenheight;
            } else {
                matrix[0] = screenwidth;
                matrix[1] = (int) (screenwidth * scale);
            }
        }
        return matrix;
    }

    /**
     * 打开声网显示  本地切換
     */
    private void openVideoByViewType() {
        if (isOpenShengwang) {
            if (mViewType == VIEW_TYPE_DEFAULT) {
                switchToDefaultVideoView();
            } else if (mViewType == VIEW_TYPE_NORMAL) {
                if (mUidsList.size() >= 1) {
                    switchToBigVideoView();
                }
            } else if (mViewType == VIEW_TYPE_SING_NORMAL) {
                if (mUidsList.size() > 1) {
                    if (mUser == null) {
                        String currentSessionID = currentMaxVideoUserId;
                        if (currentMaxVideoUserId.equals("null")) {
                            return;
                        }
                        if (!TextUtils.isEmpty(currentSessionID)) {
                            int uid = Integer.parseInt(currentSessionID);
                            for (AgoraBean agoraBean : mUidsList) {
                                if (agoraBean.getuId() == uid) {
                                    switchVideo(agoraBean);
                                    break;
                                }
                            }
                        }
                    } else {
                        switchVideo(mUser);
                    }
                }
            }
        }
    }

    /**
     * socket 切換
     */
    private void switchMode() {
        if (currentMode.equals("0") || currentMode.equals("3")) {
            switchToDefaultVideoView();
            Log.e("wahaha", 4 + "");
        } else if (currentMode.equals("1")) {
            if (mUidsList.size() >= 1) {
                switchToBigVideoView();
            }
        } else if (currentMode.equals("2")) {
            String currentSessionID = currentMaxVideoUserId;
            if (currentMaxVideoUserId.equals("null")) {
                return;
            }
            if (!TextUtils.isEmpty(currentSessionID)) {
                int uid = Integer.parseInt(currentSessionID);
                if (mUidsList.size() > 1) {
                    for (AgoraBean agoraBean : mUidsList) {
                        if (agoraBean.getuId() == uid) {
                            switchVideo(agoraBean);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean selfIsSpeaker = false;

    //说话声音音量提示回调
    @Override
    public void onAudioVolumeIndication(final IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (IRtcEngineEventHandler.AudioVolumeInfo info : speakers) {
                    //0代表本地用户
                    if (info.uid == 0 && info.volume >= 100) { //自己是否说话了
                        Log.e("onAudioVolumeIndication", info.uid + "  " + info.volume);
                        selfIsSpeaker = true;
                        break;
                    }
                    long time1 = System.currentTimeMillis() - currentTime;
                    if (time1 > 30000 && selfIsSpeaker) {
                        selfIsSpeaker = false;
                        currentTime = System.currentTimeMillis();
                        // 监控说话的是否是自己，如果自己30s内说过话，就发这条消息MEMBER_SPEAKING给server
                        try {
                            JSONObject loginjson = new JSONObject();
                            loginjson.put("action", "MEMBER_SPEAKING");
                            loginjson.put("sessionId", AppConfig.UserToken);
                            String ss = loginjson.toString();
                            SpliteSocket.sendMesageBySocket(ss);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    private void switchToDefaultVideoView() {
        icon_back.setVisibility(View.GONE);
        toggle.setVisibility(View.VISIBLE);
        if (mRightAgoraAdapter != null) {
            mRightAgoraAdapter.setData(null, teacherid);
            mRightRecycler.setVisibility(View.GONE);
        }
        if (mBigAgoraAdapter != null) {
            mBigAgoraAdapter.setData(null, teacherid);
            mBigRecycler.setVisibility(View.GONE);
            bigbg.setVisibility(View.GONE);
        }
        mViewType = VIEW_TYPE_DEFAULT;
        if (isShowDefaultVideo == true) {
            mLeftAgoraAdapter.setData(mUidsList, teacherid);
            mLeftRecycler.setVisibility(View.VISIBLE);
            toggleicon.setImageResource(R.drawable.eyeopen);
        } else {
            mLeftAgoraAdapter.setData(null, teacherid);
            mLeftRecycler.setVisibility(View.GONE);
            toggleicon.setImageResource(R.drawable.eyeclose);
        }
    }


    //----------------------- big video ----------------------
    private void switchToBigVideoView() {
        icon_back.setVisibility(View.VISIBLE);
        toggle.setVisibility(View.GONE);
        if (mLeftAgoraAdapter != null) {
            mLeftAgoraAdapter.setData(null, teacherid);
            mLeftRecycler.setVisibility(View.GONE);
        }
        if (mRightAgoraAdapter != null) {
            mRightAgoraAdapter.setData(null, teacherid);
            mRightRecycler.setVisibility(View.GONE);
        }
        mViewType = VIEW_TYPE_NORMAL;
        bindToBigVideoView(mUidsList);
    }

    public static int mViewType = 0;
    public static final int VIEW_TYPE_DEFAULT = 0;
    public static final int VIEW_TYPE_NORMAL = 1;
    public static final int VIEW_TYPE_SING_NORMAL = 2;

    private void bindToBigVideoView(final List<AgoraBean> mUidsList) {
        icon_back.setVisibility(View.VISIBLE);
        mBigRecycler.setVisibility(View.VISIBLE);
        bigbg.setVisibility(View.VISIBLE);
        GridLayoutManager s = (GridLayoutManager) mBigRecycler.getLayoutManager();
        int currentSpanCount = s.getSpanCount();
        if (mUidsList.size() == 1) {
            if (currentSpanCount != 1) {
                mBigRecycler.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
            }
        } else if (mUidsList.size() > 1 && mUidsList.size() <= 4) {
            if (currentSpanCount != 2) {
                mBigRecycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
            }
        } else if (mUidsList.size() > 4 && mUidsList.size() <= 6) {
            if (currentSpanCount != 3) {
                mBigRecycler.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
            }
        } else if (mUidsList.size() > 6 && mUidsList.size() <= 8) {
            if (currentSpanCount != 4) {
                mBigRecycler.setLayoutManager(new GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false));
            }
        } else {
            if (currentSpanCount != 5) {
                mBigRecycler.setLayoutManager(new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false));
            }
        }
        mBigAgoraAdapter.setData(mUidsList, teacherid);
    }

    private AgoraBean mUser;

    /**
     * 切换 全屏单个video
     *
     * @param user 被放大的user
     */
    private void switchVideo(AgoraBean user) {  // 要全屏的user
        if (user == null) {
            return;
        }
        if (mLeftAgoraAdapter != null) {
            mLeftAgoraAdapter.setData(null, teacherid);
            mLeftRecycler.setVisibility(View.GONE);
        }
        mViewType = VIEW_TYPE_SING_NORMAL;
        mUser = user;
        if (mUidsList.size() > 1) {
            List<AgoraBean> mUidsList2 = new ArrayList<>();
            List<AgoraBean> mUidsList3 = new ArrayList<>();
            for (AgoraBean agoraBean : mUidsList) {
                if (agoraBean.getuId() != user.getuId()) {
                    mUidsList2.add(agoraBean);
                } else {
                    mUidsList3.add(agoraBean);
                }
            }
            mRightAgoraAdapter.hiddenText(true);
            mRightAgoraAdapter.setData(mUidsList2, teacherid);
            mRightRecycler.setVisibility(View.VISIBLE);
            bindToBigVideoView(mUidsList3);
        }
    }

    private SurfaceView mySurfaceView;
    private SurfaceHolder myHolder;
    private Camera myCamera;
    private static final String TAG = "CameraActivity";

    private void toupai() {
        closeAlbum();
        mySurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        myHolder = mySurfaceView.getHolder();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initCamera();
            }
        }, 500);
    }

    // 初始化摄像头
    private void initCamera() {
        if (checkCameraHardware(getApplicationContext())) {
            // 获取摄像头（首选前置，无前置选后置）
            if (openFacingFrontCamera()) {
                Log.i(TAG, "openCameraSuccess");
                autoFocus();
            } else {
                Log.i(TAG, "openCameraFailed");
            }
        }
    }

    // 对焦并拍照
    private void autoFocus() {
        try {
            // 因为开启摄像头需要时间，这里让线程睡两秒
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myCamera.autoFocus(myAutoFocus);
        // 对焦后拍照
        myCamera.takePicture(null, null, myPicCallback);
    }


    // 判断是否存在摄像头
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    // 得到后置摄像头
    private boolean openFacingFrontCamera() {
        Log.e(TAG, "1");
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        if (myCamera == null) {
            for (int camIdx = 0, cameraCount = Camera.getNumberOfCameras(); camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        Log.e(TAG, "2");
                        myCamera = Camera.open(camIdx);
                    } catch (RuntimeException e) {
                        return false;
                    }
                }
            }
        }
        Log.e(TAG, "3");
        try {
            Camera.Parameters parameters = myCamera.getParameters();
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setJpegQuality(100); // 设置照片质量

            List<Camera.Size> supportedPreviewSizes =
                    parameters.getSupportedPreviewSizes();// 获取支持预览照片的尺寸
            Camera.Size previewSize = supportedPreviewSizes.get(getPictureSize(supportedPreviewSizes));// 从List取出Size
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            List<Camera.Size> supportedPictureSizes =
                    parameters.getSupportedPictureSizes();// 获取支持保存图片的尺寸
            Camera.Size pictureSize = supportedPictureSizes.get(getPictureSize(supportedPictureSizes));// 从List取出Size
            parameters.setPictureSize(pictureSize.width, pictureSize.height);
            myCamera.setParameters(parameters);
            myCamera.setPreviewDisplay(myHolder);
        } catch (IOException e) {
            e.printStackTrace();
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
        }
        myCamera.startPreview();
        return true;
    }


    private int getPictureSize(List<Camera.Size> sizes) {
        int index = -1;
        for (int i = 0; i < sizes.size(); i++) {
            if (Math.abs(screenWidth - sizes.get(i).width) == 0) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            index = sizes.size() / 2;
        }
        return index;
    }


    private Camera.AutoFocusCallback myAutoFocus = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {

        }
    };

    // 拍照成功回调函数
    private Camera.PictureCallback myPicCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//            Matrix matrix = new Matrix();
//            matrix.preRotate(270);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                    bitmap.getHeight(), matrix, true);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                    .format(new Date());
            // 创建并保存图片文件
            File pictureFile = new File(cache, "IMG_" + timeStamp + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception error) {
                Log.i(TAG, "保存照片失败" + error.toString());
                error.printStackTrace();
                myCamera.stopPreview();
                myCamera.release();
                myCamera = null;
            }
            Log.i(TAG, "获取照片成功");
//            Toast.makeText(WatchCourseActivity3.this, "获取照片成功", Toast.LENGTH_SHORT)
//                    .show();
            myCamera.stopPreview();
            myCamera.release();
            myCamera = null;
            openAlbum();
            LineItem attachmentBean = new LineItem();
            attachmentBean.setUrl(pictureFile.getAbsolutePath()); // 文件的路径
            attachmentBean.setFileName("IMG_" + timeStamp + ".jpg"); // 文件名
            uploadFile(attachmentBean, false);
        }
    };

}



