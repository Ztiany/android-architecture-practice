package com.app.base.push;

import android.annotation.SuppressLint;
import android.app.Application;

import com.android.base.rx.RxKit;
import com.app.base.AppContext;
import com.app.base.data.app.AppDataSource;
import com.app.base.data.models.User;
import com.app.base.debug.DebugInfoDispatcher;
import com.app.base.utils.ChannelKt;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public final class PushManager {

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
                AppContext.get().schedulerProvider.ui().scheduleDirect(() -> {
                    Timber.d("推送注册成功失败，自动重试");
                    doRegister();
                }, 60, TimeUnit.SECONDS);
            }
        });
    }

    @SuppressLint("CheckResult")
    private void addUserInfoToPushing(@NotNull String token) {
        AppContext.appDataSource()
                .observableUser()
                .subscribe(
                        user -> processUserStateChanged(user, token),
                        RxKit.logErrorHandler()
                );
    }

    private void processUserStateChanged(User user, @NotNull String token) {

    }

    @SuppressLint("CheckResult")
    private void uploadPushDevice(AppDataSource appDataSource, String token) {

    }

}