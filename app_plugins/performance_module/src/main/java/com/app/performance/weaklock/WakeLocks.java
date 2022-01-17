package com.app.performance.weaklock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;

/**
 * WakeLock 封装：Android 为了确保应用程序中关键代码的正确执行，提供了 Wake Lock 的 API，使得应用程序有权限通过代码阻止 APP 进入休眠状态。
 * WakeLock 阻止应用处理器（Application Processor）挂起，确保关键代码的运行，通过中断唤起应用处理器（Application Processor），可以阻止屏幕变暗。
 * 所有的 WakeLock 被释放后，系统会挂起。
 *
 * <p>
 * 【TODO 改善封装】
 */
public class WakeLocks {

    private static PowerManager.WakeLock sWakeLock;

    public static void acquire(Context context) {
        if (sWakeLock == null) {
            sWakeLock = createWakeLock(context);
        }
        if (sWakeLock != null && !sWakeLock.isHeld()) {
            sWakeLock.acquire();
            sWakeLock.acquire(1000);
        }
    }

    public static void release() {
        // 一些逻辑
        try {

        } catch (Exception e) {

        } finally {
            // 为了演示正确的使用方式
            if (sWakeLock != null && sWakeLock.isHeld()) {
                sWakeLock.release();
                sWakeLock = null;
            }
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private static PowerManager.WakeLock createWakeLock(Context context) {
        PowerManager pm = (PowerManager) context.getApplicationContext().getSystemService(Context.POWER_SERVICE);
        if (pm != null) {
            return pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        }
        return null;
    }

}
