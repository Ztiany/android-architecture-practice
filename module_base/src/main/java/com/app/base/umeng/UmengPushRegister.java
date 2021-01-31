package com.app.base.umeng;

import android.content.Context;
import android.text.TextUtils;

import com.app.base.push.PushCallBack;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import timber.log.Timber;

final class UmengPushRegister {

    private static volatile boolean sIsRegistering = false;
    private static volatile boolean sIsRegisteringSuccessfully = false;

    private volatile static String sToken;

    static boolean isRegisteringSuccessfully() {
        return sIsRegisteringSuccessfully;
    }

    synchronized static void registerUmengPush(final Context context, PushCallBack pushCallBack) {
        if (sIsRegistering) {
            return;
        }

        if (sIsRegisteringSuccessfully) {
            return;
        }

        register(context, pushCallBack);
    }

    private static void register(final Context context, PushCallBack pushCallBack) {
        sIsRegistering = true;
        PushAgent mPush = PushAgent.getInstance(context);
        // 设置消息显示数量
        mPush.setDisplayNotificationNumber(10);
        // 开启声音
        mPush.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
        // 应用在前台不展示消息
        mPush.setNotificaitonOnForeground(false);
        mPush.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String s) {
                if (TextUtils.isEmpty(s)) {
                    onFailure("", "");
                } else {
                    sIsRegistering = false;
                    sToken = s;
                    sIsRegisteringSuccessfully = true;
                    Timber.d("消息推送注册成功，token = %s", s);
                    if (pushCallBack != null) {
                        pushCallBack.onRegisterPushSuccess(s);
                    }
                }
            }

            @Override
            public void onFailure(String s, String s1) {
                sIsRegistering = false;
                pushCallBack.onRegisterPushFail();
                Timber.d("消息推送注册失败，message = %s", s);
            }

        });
    }

    public static String getToken() {
        return sToken;
    }

}