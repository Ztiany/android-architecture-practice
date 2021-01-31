package com.app.base.umeng;

import android.content.Context;

import com.app.base.AppContext;
import com.app.base.debug.Debug;
import com.app.base.debug.DebugInfoDispatcher;
import com.app.base.utils.ChannelKt;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.statistics.common.DeviceConfig;

import org.json.JSONObject;

import androidx.annotation.RestrictTo;
import timber.log.Timber;

/**
 * 友盟信息注册。
 * <br/><br/>
 * 已开启的功能：
 * <pre>
 *     1. 友盟推送
 * </pre>
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-10-29 11:40
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public class Umeng {

    private static final String APP_KEY_RELEASE = "?";
    private static final String MESSAGE_SECRET_RELEASE = "?";

    private static final String APP_KEY_DEV = "?";
    private static final String MESSAGE_SECRET_DEV = "?";

    public static void init(AppContext appContext) {
        //通用配置
        String appChannel = ChannelKt.getAppChannel(appContext);
        if (Debug.isOpenDebug()) {
            UMConfigure.setLogEnabled(true);
            UMConfigure.init(appContext, APP_KEY_DEV, appChannel, UMConfigure.DEVICE_TYPE_PHONE, MESSAGE_SECRET_DEV);
            dispatchDebugInfo(appContext);
        } else {
            UMConfigure.setLogEnabled(false);
            UMConfigure.init(appContext, APP_KEY_RELEASE, appChannel, UMConfigure.DEVICE_TYPE_PHONE, MESSAGE_SECRET_RELEASE);
        }
    }

    private static void dispatchDebugInfo(AppContext appContext) {
        String testDeviceInfo = getTestDeviceInfo(appContext);
        Timber.d("UmengStatisticTestInfo：%s", testDeviceInfo);
        DebugInfoDispatcher.INSTANCE.dispatchUmengDeviceInfo(testDeviceInfo);
    }

    private static String getTestDeviceInfo(Context context) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (context != null) {
                jsonObject.put("device_id", getUmengDeviceId(context));
                jsonObject.put("mac", getUmengMac(context));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getUmengDeviceId(Context context) {
        return DeviceConfig.getDeviceIdForGeneral(context);
    }

    public static String getUmengMac(Context context) {
        return DeviceConfig.getMac(context);
    }

}