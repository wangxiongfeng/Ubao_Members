package com.ubao.techexcel.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ubao.techexcel.R;
import com.ubao.techexcel.config.AppConfig;
import com.ubao.techexcel.personal.PersanalCollectionActivity;
import com.ubao.techexcel.ui.UpoadNull;

public class UploadService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showPop();
            }
        }, 1000);

    }

    private void showPop() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpoadNull.instance);
        builder.setTitle("")
                .setMessage(getString(R.string.ask_update_out))
                .setPositiveButton(getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        if (PersanalCollectionActivity.instance != null && !PersanalCollectionActivity.instance.isFinishing()) {
                            PersanalCollectionActivity.instance.finish();
                        }
                        Intent intent = new Intent(UploadService.this, PersanalCollectionActivity.class);
                        intent.putExtra("path", AppConfig.OUTSIDE_PATH);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                        AppConfig.OUTSIDE_PATH = "";
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
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                UpoadNull.instance.finish();
                AppConfig.OUTSIDE_PATH = "";
            }
        });
        dialog.show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
