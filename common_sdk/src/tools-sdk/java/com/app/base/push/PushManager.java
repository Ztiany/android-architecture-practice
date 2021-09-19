package com.app.base.push;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.app.base.services.usermanager.UserManager;
import com.app.base.debug.DebugInfoDispatcher;
import com.app.base.utils.ChannelKt;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public final class PushManager {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private static volatile PushManager PUSH_MANAGER;

    private Push mPush;

    private final AppMessageHandler mMessageHandler;

    private static final String DEFAULT_TYPE = "APP_DEFAULT";

    private String previousUserId = "";

    private Application mApplication;

    private PushManager() {
        mMessageHandler = new AppMessageHandler();
    }

    public static PushManager getInstance() {
        if (PUSH_MANAGER == null) {
            synchronized (PushManager.class) {
                if (PUSH_MANAGER == null) {
                    PUSH_MANAGER = new PushManager();
                }
            }
        }
        return PUSH_MANAGER;
    }

    public void init(Application application) {
        //初始化推送
        mPush = new DummyPush();
        mApplication = application;
        //设置消息处理器
        mPush.setChannel(ChannelKt.getAppChannel(application));
        mPush.setMessageHandler(mMessageHandler);
        //注册推送
        doRegister();
    }

    public void setPushProcessorEnable(boolean enable) {
        mMessageHandler.setEnable(enable);
    }

    private void doRegister() {
        mPush.registerPush(new PushCallBack() {
            @SuppressLint("CheckResult")
            @Override
            public void onRegisterPushSuccess(@NotNull String token) {
                Timber.d("推送注册成功，token = %s", token);
                DebugInfoDispatcher.INSTANCE.dispatchPushToken(token);
                mPush.setMessageHandler(mMessageHandler);
                addUserInfoToPushing(token);
            }

            @Override
            public void onRegisterPushFail() {
                Timber.d("推送注册成功失败");
                mHandler.postDelayed(() -> {
                    Timber.d("推送注册成功失败，自动重试");
                    doRegister();
                }, 60000);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void addUserInfoToPushing(@NotNull String token) {
        //TODO: If the current user is out, unregister the subscription associated with this user.
    }

    @SuppressLint("CheckResult")
    private void uploadPushDevice(UserManager userManager, String token) {

    }

}