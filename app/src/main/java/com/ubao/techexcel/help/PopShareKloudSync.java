package com.ubao.techexcel.help;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.ubao.techexcel.R;
import com.ubao.techexcel.config.AppConfig;
import com.kloudsync.techexcel.docment.WeiXinApi;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;
import com.ub.kloudsync.activity.TeamSpaceBeanFile;

import java.util.ArrayList;
import java.util.List;

public class PopShareKloudSync {

    public Context mContext;

    TeamSpaceBeanFile lesson;

    int Syncid;

    private static PopShareKloudSyncDismissListener popShareKloudSyncDismissListener;

    public interface PopShareKloudSyncDismissListener {
        void CopyLink();

        void Wechat();

        void Moment();

        void Scan();

        void PopBack();
    }

    public void setPoPDismissListener(PopShareKloudSyncDismissListener popShareKloudSyncDismissListener) {
        this.popShareKloudSyncDismissListener = popShareKloudSyncDismissListener;
    }

    public void getPopwindow(Context context, TeamSpaceBeanFile lesson, int Syncid) {
        this.mContext = context;
        this.lesson = lesson;
        this.Syncid = Syncid;

        getPopupWindowInstance();
        mPopupWindow.setAnimationStyle(R.style.PopupAnimation5);
    }


    public PopupWindow mPopupWindow;

    public void getPopupWindowInstance() {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            return;
        } else {
            initPopuptWindow();
        }
    }

    private LinearLayout lin_copy;
    private LinearLayout lin_wechat;
    private LinearLayout lin_moment;
    private LinearLayout lin_Scan;
    private List<LinearLayout> lin_all = new ArrayList<>();


    @SuppressWarnings("deprecation")
    public void initPopuptWindow() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View popupWindow = layoutInflater.inflate(R.layout.pop_share_kloudsync, null);

        lin_copy = (LinearLayout) popupWindow.findViewById(R.id.lin_copy);
        lin_wechat = (LinearLayout) popupWindow.findViewById(R.id.lin_wechat);
        lin_moment = (LinearLayout) popupWindow.findViewById(R.id.lin_moment);
        lin_Scan = (LinearLayout) popupWindow.findViewById(R.id.lin_Scan);
        lin_all.add(lin_copy);
        lin_all.add(lin_wechat);
        lin_all.add(lin_moment);
        lin_all.add(lin_Scan);

        mPopupWindow = new PopupWindow(popupWindow, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);

        mPopupWindow.getWidth();
        mPopupWindow.getHeight();

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (popShareKloudSyncDismissListener != null) {
                    popShareKloudSyncDismissListener.PopBack();
                }
            }
        });

        lin_copy.setOnClickListener(new myOnClick());
        lin_wechat.setOnClickListener(new myOnClick());
        lin_moment.setOnClickListener(new myOnClick());
        lin_Scan.setOnClickListener(new myOnClick());

        EnterAnim();

        // 使其聚焦
        mPopupWindow.setFocusable(true);
        // 设置允许在外点击消失
        mPopupWindow.setOutsideTouchable(true);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    private void EnterAnim() {
        for (int i = 0; i < lin_all.size(); i++) {
            final LinearLayout lin = lin_all.get(i);
            ObjectAnimator animator1 = ObjectAnimator.ofFloat(lin,
                    "translationY", 300, 0F);
            ObjectAnimator animator2 = ObjectAnimator.ofFloat(lin,
                    "alpha", 0.3F, 1F);
            AnimatorSet set = new AnimatorSet();
            set.play(animator1).with(animator2);
            set.setDuration(300);
            set.setInterpolator(new OvershootInterpolator());
            set.setStartDelay((i + 1) * 100);
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            set.start();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    lin.setVisibility(View.VISIBLE);
                }
            },(i + 1) * 100 + 50);
        }
    }


    private class myOnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.lin_copy:
                    popShareKloudSyncDismissListener.CopyLink();
                    CopyLink();
                    mPopupWindow.dismiss();
                    break;
                case R.id.lin_wechat:
                    popShareKloudSyncDismissListener.Wechat();
                    GetUrl(lesson, Syncid);
                    mPopupWindow.dismiss();
                    break;
                case R.id.lin_moment:
                    popShareKloudSyncDismissListener.Moment();
                    mPopupWindow.dismiss();
                    break;
                case R.id.lin_Scan:
                    popShareKloudSyncDismissListener.Scan();
                    mPopupWindow.dismiss();
                    break;

                default:
                    break;
            }

        }


    }

    private ClipboardManager mClipboard = null;

    private void CopyLink() {
        String url = AppConfig.SHARE_DOCUMENT + lesson.getItemID();
        if (Syncid > 0) {
            url = AppConfig.SHARE_SYNC + Syncid;
        }
        if (null == mClipboard) {
            mClipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        }

        mClipboard.setPrimaryClip(ClipData.newPlainText(null, url));
        Toast.makeText(mContext, "Copy link success!", Toast.LENGTH_LONG).show();
    }

    private void GetUrl(final TeamSpaceBeanFile lesson, final int id) {
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        final LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(
                20);
        ImageCache imageCache = new ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lruCache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lruCache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        String url = lesson.getSourceFileUrl();
        url = url.substring(0, url.lastIndexOf("<")) + "1_thumbnail" + url.substring(url.lastIndexOf("."), url.length());
        Log.e("url", url + "      ");

        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.e("error", error + "");
                weixinshare(lesson, null, id);

            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                // TODO Auto-generated method stub
                if (response.getBitmap() != null) {
                    weixinshare(lesson, response.getBitmap(), id);
                }

            }
        });
    }

    private void weixinshare(TeamSpaceBeanFile lesson, Bitmap b, int id) {
        String url = AppConfig.SHARE_DOCUMENT + lesson.getItemID();
        if (id > 0) {
            url = AppConfig.SHARE_SYNC + id;
        }
        if (isWXAppInstalledAndSupported(WeiXinApi.getInstance().GetApi())) {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = lesson.getTitle();
            msg.description = "请点击此框跳转至" + url;

            Bitmap thumb = b;
            if (null == thumb) {
                thumb = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.ubaoapph);
            }

            msg.thumbData = Util.bmpToByteArray(thumb, true);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("webpage");

            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;
            WeiXinApi.getInstance().GetApi().sendReq(req);
        } else {
            Toast.makeText(mContext, "微信客户端未安装，请确认",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isWXAppInstalledAndSupported(IWXAPI api) {
        return api.isWXAppInstalled() && api.isWXAppSupportAPI();
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis())
                : type + System.currentTimeMillis();
    }

    public boolean isShow() {
        return mPopupWindow.isShowing();
    }

    public void StartPop(View v) {
        mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
    }


}
