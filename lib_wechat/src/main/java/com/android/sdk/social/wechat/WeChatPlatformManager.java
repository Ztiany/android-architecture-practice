package com.android.sdk.social.wechat;

import android.annotation.SuppressLint;
import android.arch.lifecycle.GenericLifecycleObserver;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.android.sdk.social.Status;
import com.android.sdk.social.Utils;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import io.reactivex.functions.Consumer;

/**
 * 微信登录实现
 */
public class WeChatPlatformManager {

    static final String TAG = WeChatPlatformManager.class.getSimpleName();

    /*默认的state，用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验*/
    private static final String DEFAULT_STATE = "wechat_sdk_login";
    /*固定的请求域，应用授权作用域，如获取用户个人信息则填写snsapi_userinfo*/
    private static final String GET_USER_INFO_SCOPE = "snsapi_userinfo";
    private static String sAppId;
    private static String sAppSecret;
    private static WeChatPlatformManager sWeChatPlatformManager;

    public static void initWeChatSDK(String appId, String appSecret) {
        sAppId = appId;
        sAppSecret = appSecret;
    }

    static String getAppId() {
        Utils.requestNotNull(sAppId, "weChat app id");
        return sAppId;
    }

    static String getAppSecret() {
        Utils.requestNotNull(sAppSecret, "weChat appSecret");
        return sAppSecret;
    }

    static boolean handleIntent(Intent intent, IWXAPIEventHandler iwxapiEventHandler) {
        if (sWeChatPlatformManager != null) {
            return sWeChatPlatformManager.mWxApi.handleIntent(intent, iwxapiEventHandler);
        } else {
            Log.w(TAG, "WeChatPlatformManager handleIntent called, but sWeChatPlatformManager was  destroyed");
        }
        return false;
    }

    static void handleOnResp(BaseResp baseResp) {
        if (BaseResp.ErrCode.ERR_OK == baseResp.errCode) {
            Log.i(TAG, "WeChatPlatformManager handleOnResp success, baseResp = " + baseResp);
            handleOnRespSuccess(baseResp);
        } else {
            Log.w(TAG, "WeChatPlatformManager handleOnResp fail, baseResp = " + baseResp);
            handleOnRespFail(baseResp);
        }
    }

    private static void handleOnRespFail(BaseResp baseResp) {
        if (sWeChatPlatformManager != null) {
            MutableLiveData<Status<WXUser>> mutableLiveData = sWeChatPlatformManager.mMutableLiveData;
            if (mutableLiveData != null) {
                mutableLiveData.postValue(Status.<WXUser>error(new WeChatLoginException(baseResp.errCode, baseResp.errStr)));
            }
        }
    }

    private static void handleOnRespSuccess(BaseResp baseResp) {
        if (baseResp instanceof SendAuth.Resp) {
            handleChatLoginResp((SendAuth.Resp) baseResp);
        }
    }

    private static void handleChatLoginResp(SendAuth.Resp resp) {
        if (sWeChatPlatformManager != null) {
            if (resp.state.equals(sWeChatPlatformManager.currentState)) {
                sWeChatPlatformManager.handWeChatLoginResp(resp);
            } else {
                Log.w(TAG, "WeChatPlatformManager handleChatLoginResp called, but state is not matched");
            }
        } else {
            Log.w(TAG, "WeChatPlatformManager handleChatLoginResp called, but sWeChatPlatformManager was  destroyed");
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 实例方法
    ///////////////////////////////////////////////////////////////////////////
    private String currentState;
    private final IWXAPI mWxApi;
    private MutableLiveData<Status<WXUser>> mMutableLiveData = new MutableLiveData<>();

    public WeChatPlatformManager(Context context, LifecycleOwner lifecycleOwner) {
        mWxApi = WXAPIFactory.createWXAPI(context, getAppId(), false);
        mWxApi.registerApp(getAppId());
        sWeChatPlatformManager = this;
        lifecycleOwner.getLifecycle().addObserver(new GenericLifecycleObserver() {
            @Override
            public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    destroy();
                }
            }
        });
    }

    @SuppressWarnings("unused")
    public boolean isInstalledWeChat() {
        return mWxApi.isWXAppInstalled();
    }

    /**
     * 请求微信登录
     *
     * @param state 第三方程序发送时用来标识其请求的唯一性的标志，由第三方程序调用sendReq时传入，由微信终端回传，state字符串长度不能超过1K
     */
    public LiveData<Status<WXUser>> requestChatLogin(String state) {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = GET_USER_INFO_SCOPE;
        req.state = TextUtils.isEmpty(state) ? DEFAULT_STATE : state;
        currentState = req.state;
        mWxApi.sendReq(req);
        return mMutableLiveData;
    }

    @SuppressLint("CheckResult")
    private void handWeChatLoginResp(SendAuth.Resp resp) {
        mMutableLiveData.postValue(Status.<WXUser>loading());
        Log.i(TAG, "handWeChatLoginResp called, requesting user info.......");
        WeChatLoginImpl.doWeChatLogin(resp)
                .subscribe(new Consumer<WXUser>() {
                    @Override
                    public void accept(WXUser wxUser) {
                        Log.i(TAG, "handWeChatLoginResp success and result = " + wxUser);
                        processRequestUserInfoResult(wxUser);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.i(TAG, "handWeChatLoginResp fail and result = " + throwable);
                        if (mMutableLiveData != null) {
                            mMutableLiveData.postValue(Status.<WXUser>error(throwable));
                        }
                    }
                });
    }

    private void processRequestUserInfoResult(WXUser wxUser) {
        if (mMutableLiveData != null) {
            if (wxUser != null) {
                if (wxUser.getErrcode() != 0) {
                    mMutableLiveData.postValue(Status.<WXUser>error(new WeChatLoginException(wxUser.getErrcode(), wxUser.getErrmsg())));
                } else {
                    mMutableLiveData.postValue(Status.success(wxUser));
                }
            } else {
                mMutableLiveData.postValue(Status.<WXUser>error(new WeChatLoginException(BaseResp.ErrCode.ERR_COMM, null)));
            }
        }

    }

    private void destroy() {
        sWeChatPlatformManager = null;
        currentState = null;
        mWxApi.unregisterApp();
        mWxApi.detach();
    }

}
