package com.ub.techexcel.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ub.techexcel.bean.SoundtrackBean;
import com.ubao.techexcel.R;
import com.ubao.techexcel.config.AppConfig;
import com.ubao.techexcel.info.Favorite;
import com.ubao.techexcel.service.ConnectService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wang on 2017/9/18.
 */

public class YinxiangEditPopup implements View.OnClickListener {

    public Context mContext;
    public int width;
    public PopupWindow mPopupWindow;
    private View view;

    private TextView createDuration;
    private TextView tv2;
    private TextView tv3;
    private TextView tv5;
    private TextView tv6;
    private TextView tv4;
    private TextView tv7;
    private TextView tv11;
    private TextView tv12;

    private ImageView im4, im5;

    private EditText tv0;

    private ImageView img1;

    private Favorite backgroundFavorite = new Favorite();
    private Favorite selectedFavorite = new Favorite();

    private SoundtrackBean soundtrackBean = new SoundtrackBean();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x1001: //创建成功
                    dismiss();
                    break;
                case 0x1003:
                    dismiss();
                    mFavoritePoPListener.syncorrecord(true, (SoundtrackBean) msg.obj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private static FavoritePoPListener mFavoritePoPListener;


    public interface FavoritePoPListener {
        void dismiss();

        void open();

        void addrecord(int isrecord);

        void addaudio(int isrecord);

        void syncorrecord(boolean checked, SoundtrackBean soundtrackBean);
    }

    public void setFavoritePoPListener(FavoritePoPListener documentPoPListener) {
        this.mFavoritePoPListener = documentPoPListener;
    }


    public void getPopwindow(Context context) {
        this.mContext = context;
        width = mContext.getResources().getDisplayMetrics().widthPixels;
        getPopupWindowInstance();
    }

    public void getPopupWindowInstance() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    public void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        view = layoutInflater.inflate(R.layout.yinxiang_edit_popup, null);
        tv2 = (TextView) view.findViewById(R.id.tv2);
        img1 = (ImageView) view.findViewById(R.id.img1);
        tv0 = (EditText) view.findViewById(R.id.tv0);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        tv3 = (TextView) view.findViewById(R.id.tv3);
        tv4 = (TextView) view.findViewById(R.id.tv4);
        tv5 = (TextView) view.findViewById(R.id.tv5);
        tv6 = (TextView) view.findViewById(R.id.tv6);
        tv7 = (TextView) view.findViewById(R.id.tv7);
        tv11 = (TextView) view.findViewById(R.id.tv11);
        tv12 = (TextView) view.findViewById(R.id.tv12);
        im4 = (ImageView) view.findViewById(R.id.img4);
        im5 = (ImageView) view.findViewById(R.id.img5);

        createDuration = (TextView) view.findViewById(R.id.duration);

        tv4.setOnClickListener(this);
        im4.setOnClickListener(this);
        im5.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv11.setOnClickListener(this);
        tv12.setOnClickListener(this);

        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mFavoritePoPListener.dismiss();
            }
        });
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    @SuppressLint("NewApi")
    public void StartPop(View v, SoundtrackBean soundtrackBean) {
        if (mPopupWindow != null) {
            mFavoritePoPListener.open();
            this.soundtrackBean = soundtrackBean;

            backgroundFavorite = soundtrackBean.getBackgroudMusicInfo();
            if (backgroundFavorite == null || backgroundFavorite.getAttachmentID() == 0) {
                backgroundFavorite = new Favorite();
                tv4.setVisibility(View.VISIBLE);
                im4.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
            } else {
                im4.setVisibility(View.VISIBLE);
                tv4.setVisibility(View.GONE);
                tv2.setVisibility(View.VISIBLE);
                tv3.setVisibility(View.VISIBLE);
            }

            selectedFavorite = soundtrackBean.getSelectedAudioInfo();
            if (selectedFavorite == null || selectedFavorite.getAttachmentID() == 0) {
                selectedFavorite = new Favorite();
                tv7.setVisibility(View.VISIBLE);
                im5.setVisibility(View.GONE);
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
            } else {
                im5.setVisibility(View.VISIBLE);
                tv7.setVisibility(View.GONE);
                tv5.setVisibility(View.VISIBLE);
                tv6.setVisibility(View.VISIBLE);
            }

            mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

            tv0.setText(soundtrackBean.getTitle());
            if (!TextUtils.isEmpty(soundtrackBean.getTitle())) {
                tv0.setSelection(soundtrackBean.getTitle().length());
            }

            if (!TextUtils.isEmpty(soundtrackBean.getDuration())) {
                String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Long.parseLong(soundtrackBean.getCreatedDate()));
                createDuration.setText("Synced at " + time);
            }

            //设置 backgroundFavorite
            tv2.setText("Audio: " + soundtrackBean.getBackgroudMusicTitle());
            backgroundFavorite.setTitle(soundtrackBean.getBackgroudMusicTitle());
            //设置 selectFavorite
            tv5.setText("Voice: " + soundtrackBean.getSelectedAudioTitle());
            selectedFavorite.setTitle(soundtrackBean.getSelectedAudioTitle());

            if (!TextUtils.isEmpty(soundtrackBean.getDuration())) {
                String time = new SimpleDateFormat("mm:ss").format(Long.parseLong(soundtrackBean.getDuration()));
                tv3.setText("Time: " + time);
                tv6.setText("Time: " + time);
            }

        }
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }


    public void setAudioBean(Favorite favorite2) {
        backgroundFavorite = favorite2;
        tv2.setText("Audio: " + backgroundFavorite.getTitle());
        if (!TextUtils.isEmpty(soundtrackBean.getDuration())) {
            String time = new SimpleDateFormat("mm:ss").format(Long.parseLong(soundtrackBean.getDuration()));
            tv3.setText("Time: " + time);
        }
        tv2.setVisibility(View.VISIBLE);
        tv3.setVisibility(View.VISIBLE);
        im4.setVisibility(View.VISIBLE);
        tv4.setVisibility(View.GONE);
    }

    public void setRecordBean(Favorite favorite) {
        selectedFavorite = favorite;
        tv5.setText("Audio: " + selectedFavorite.getTitle());
        if (!TextUtils.isEmpty(soundtrackBean.getDuration())) {
            String time = new SimpleDateFormat("mm:ss").format(Long.parseLong(soundtrackBean.getDuration()));
            tv6.setText("Time: " + time);
        }

        tv5.setVisibility(View.VISIBLE);
        tv6.setVisibility(View.VISIBLE);
        im5.setVisibility(View.VISIBLE);
        tv7.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close:
                dismiss();
                break;
            case R.id.tv4:
                mFavoritePoPListener.addaudio(0);
                break;
            case R.id.tv7:
                mFavoritePoPListener.addrecord(1);
                break;
            case R.id.tv11:
                createSoundtrack(1);
                break;
            case R.id.tv12:
                createSoundtrack(2);
                break;
            case R.id.img4:
                backgroundFavorite = new Favorite();
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
                im4.setVisibility(View.GONE);
                tv4.setVisibility(View.VISIBLE);
                break;
            case R.id.img5:
                selectedFavorite = new Favorite();
                tv5.setVisibility(View.GONE);
                tv6.setVisibility(View.GONE);
                im5.setVisibility(View.GONE);
                tv7.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }


    private void createSoundtrack(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("AttachmentID", Integer.parseInt(soundtrackBean.getAttachmentId()));
                    jsonObject.put("SoundtrackID", soundtrackBean.getSoundtrackID());

                    if (selectedFavorite == null) {
                        selectedFavorite = new Favorite();
                        selectedFavorite.setAttachmentID(0);
                    }
                    jsonObject.put("SelectedAudioAttachmentID", selectedFavorite.getAttachmentID());
                    jsonObject.put("SelectedAudioTitle", selectedFavorite.getAttachmentID() == 0 ? "" : selectedFavorite.getTitle());
                    if (backgroundFavorite == null) {
                        backgroundFavorite = new Favorite();
                        backgroundFavorite.setAttachmentID(0);
                    }
                    jsonObject.put("BackgroudMusicAttachmentID", backgroundFavorite.getAttachmentID());
                    jsonObject.put("BackgroudMusicTitle", backgroundFavorite.getAttachmentID() == 0 ? "" : backgroundFavorite.getTitle());

                    jsonObject.put("Title", tv0.getText().toString());
                    jsonObject.put("EnableBackgroud", 1);
                    jsonObject.put("EnableSelectVoice", 1);
                    jsonObject.put("EnableRecordNewVoice", 1);
                    JSONObject returnjson = ConnectService.submitDataByJson(AppConfig.URL_PUBLIC + "Soundtrack/UpdateSoundtrack", jsonObject);
                    Log.e("hhh", jsonObject.toString() + "      " + returnjson.toString());
                    if (returnjson.getInt("RetCode") == 0) {

                        soundtrackBean.setBackgroudMusicInfo(backgroundFavorite);
                        soundtrackBean.setSelectedAudioInfo(selectedFavorite);

                        Message msg3 = Message.obtain();
                        msg3.obj = soundtrackBean;
                        if (i == 1) {
                            msg3.what = 0x1003;
                        } else {
                            msg3.what = 0x1001;
                        }
                        handler.sendMessage(msg3);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
