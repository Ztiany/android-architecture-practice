package com.app.base.data;

import android.app.Application;

import com.android.base.utils.android.SpCache;
import com.app.base.BuildConfig;
import com.blankj.utilcode.util.NetworkUtils;

/**
 * Data层配置，抽象为DataContext
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-04-12 11:16
 */
public class DataContext {

    public synchronized static void init(Application application) {
        if (sDataContext != null) {
            throw new IllegalStateException("DataContext was  initialized");
        }
        sDataContext = new DataContext(application);
    }

    private static DataContext sDataContext;

    public static DataContext getInstance() {
        if (sDataContext == null) {
            throw new IllegalStateException("DataContext has not initialize");
        }
        return sDataContext;
    }

    private DataContext(Application application) {
        mSpCache = new SpCache(application, NAME);
        //ParamsUtils.init(AppSecurity.getAppToken(), AppSecurity.getRsaPublicKey(), DevicesUtils.getDeviceId(), AppUtils.getAppVersionName());
        init();
    }

    private static final String NAME = "data_context";
    private static final String HOST_KEY = "host_key";

    private final SpCache mSpCache;

    private int mHostEnvIdentification;
    private boolean mIsAppPreparedCalled;

    /*环境配置*/
    public static final int BUILD_DEBUG = 0x0110;
    public static final int BUILD_PRE_RELEASE = 0x0111;
    public static final int BUILD_RELEASE = 0x01112;

    /*永远不要修改*/
    private static final String DEBUG = "debug";
    private static final String PRE = "pre";
    private static final String RELEASE = "release";

    /**
     * 获取当前构建的主机地址标识，如：<font color="red">调试、预发布、生产</font>
     *
     * @return Identification
     */
    public int getHostIdentification() {
        return mHostEnvIdentification;
    }

    /**
     * 获取当前构建的主机地址标识对象字符串，如：<font color="red">调试、预发布、生产</font>
     *
     * @return Identification
     */
    public String getHostTag() {
        if (mHostEnvIdentification == BUILD_RELEASE) {
            return RELEASE;
        } else if (mHostEnvIdentification == BUILD_PRE_RELEASE) {
            return PRE;
        }
        return DEBUG;
    }

    /**
     * 获取语言环境和主机环境的综合标识，比如<font color="red">调试环境的中文</font>
     *
     * @return tag
     */
    String getEnvTag() {
        return getHostTag();
    }

    private void init() {
        if (BuildConfig.openDebug) {
            mHostEnvIdentification = mSpCache.getInt(HOST_KEY, BUILD_DEBUG);
        } else {
            mHostEnvIdentification = BUILD_RELEASE;
        }
    }

    /**
     * just for debug
     */
    public void switchHost(int host) {
        mSpCache.putInt(HOST_KEY, host);
    }

    public boolean isConnected() {
        return NetworkUtils.isConnected();
    }

}
