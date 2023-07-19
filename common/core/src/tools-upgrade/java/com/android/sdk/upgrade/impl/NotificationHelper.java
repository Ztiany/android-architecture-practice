package com.android.sdk.upgrade.impl;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.android.base.utils.BaseUtils;
import com.blankj.utilcode.util.AppUtils;
import com.app.base.R;

/**
 * @author Ztiany
 */
final class NotificationHelper {

    private static final int ID = 10;
    private static final String CHANNEL_ID = "auto-ls-upgrade";

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private void init() {
        mNotificationManager = (NotificationManager) BaseUtils.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "upgrade", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("for show downloading apk progress");
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
        }

        mBuilder = new NotificationCompat.Builder(BaseUtils.getAppContext(), CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(AppUtils.getAppName())
                .setContentText("正在下载新版本")
                .setSmallIcon(R.drawable.img_logo/*replace the real icon*/);
    }

    void cancelNotification() {
        if (mBuilder != null) {
            mBuilder.setProgress(0, 0, false);
            mNotificationManager.notify(ID, mBuilder.build());
            mNotificationManager.cancel(ID);
        }
        mNotificationManager = null;
        mBuilder = null;
    }

    void notifyProgress(long total, long progress) {
        if (mNotificationManager == null) {
            init();
        }
        mBuilder.setProgress((int) total, (int) progress, total == -1);
        mNotificationManager.notify(ID, mBuilder.build());
    }

}