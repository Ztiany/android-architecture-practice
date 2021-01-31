package com.app.base.upgrade;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import com.app.base.AppContext;
import com.app.base.R;
import com.blankj.utilcode.util.AppUtils;

import androidx.core.app.NotificationCompat;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-10-31 17:07
 */
class NotificationHelper {

    private static final int ID = 10;
    private static final String CHANNEL_ID = "bh-upgrade";

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private void init() {
        mNotificationManager = (NotificationManager) AppContext.get().getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "upgrade", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("for show downloading apk progress");
            channel.setSound(null, null);
            mNotificationManager.createNotificationChannel(channel);
        }

        mBuilder = new NotificationCompat.Builder(AppContext.get(), CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(AppUtils.getAppName())
                .setContentText("正在下载新版本")
                .setSmallIcon(R.drawable.icon_clear/*replace the real icon*/);
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
        if (total == -1) {
            mBuilder.setProgress((int) total, (int) progress, true);
        } else {
            mBuilder.setProgress((int) total, (int) progress, false);
        }
        mNotificationManager.notify(ID, mBuilder.build());
    }

}