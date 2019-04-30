package com.ubao.techexcel.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.support.multidex.MultiDex;

import com.pgyersdk.crash.PgyCrashManager;
import com.ubao.techexcel.config.AppConfig;
import com.ubao.techexcel.dialog.message.ChangeItemMessage;
import com.ubao.techexcel.dialog.message.CourseMessage;
import com.ubao.techexcel.dialog.message.CustomizeMessage;
import com.ubao.techexcel.dialog.message.DemoContext;
import com.ubao.techexcel.dialog.message.FriendMessage;
import com.ubao.techexcel.dialog.message.GroupMessage;
import com.ubao.techexcel.dialog.message.KnowledgeMessage;
import com.ubao.techexcel.dialog.message.SendFileMessage;
import com.ubao.techexcel.dialog.message.SpectatorMessage;
import com.ubao.techexcel.dialog.message.SystemMessage;
import com.ubao.techexcel.help.CrashHandler;
import com.ubao.techexcel.start.StartUbao;

import org.xutils.x;

import java.util.Locale;

import io.agora.openlive.model.WorkerThread;
import io.rong.imkit.RongIM;


public class App extends Application {
    //lalalalalalala

    private CrashHandler mCrashHandler;
	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
		x.Ext.init(this);

        mCrashHandler = CrashHandler.getInstance();
        mCrashHandler.init(this);
		PgyCrashManager.register(this);
		/**
		 * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
		 * io.rong.push 为融云 push 进程名称，不可修改。
		 */
		if (getApplicationInfo().packageName
				.equals(getCurProcessName(getApplicationContext()))
				|| "io.rong.push"
						.equals(getCurProcessName(getApplicationContext()))) {

			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(this);
			RongIM.registerMessageType(CustomizeMessage.class);
			RongIM.registerMessageType(KnowledgeMessage.class);
			RongIM.registerMessageType(SystemMessage.class);
			RongIM.registerMessageType(FriendMessage.class);
			RongIM.registerMessageType(GroupMessage.class);
			RongIM.registerMessageType(CourseMessage.class);
			RongIM.registerMessageType(ChangeItemMessage.class);
			RongIM.registerMessageType(SpectatorMessage.class);
			RongIM.registerMessageType(SendFileMessage.class);

//			RongIMClient.init(this);
	        DemoContext.getInstance().init(this);
//			initWorkerThread();
		}
	}



    private WorkerThread mWorkerThread;

    public synchronized void initWorkerThread() {
        if (mWorkerThread == null) {
            mWorkerThread = new WorkerThread(getApplicationContext());
            mWorkerThread.start();
            mWorkerThread.waitForReady();
        }
    }

    public synchronized WorkerThread getWorkerThread() {
        return mWorkerThread;
    }

    public synchronized void deInitWorkerThread() {

        mWorkerThread.exit();
        try {
            mWorkerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mWorkerThread = null;

    }


    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (AppConfig.LANGUAGEID > 0) {
            switch (AppConfig.LANGUAGEID) {
                case 1:
                    StartUbao.updateLange(this, Locale.ENGLISH);
                    break;
                case 2:
                    StartUbao.updateLange(this, Locale.SIMPLIFIED_CHINESE);
                    break;
                default:
                    break;
            }
        }

    }
}
