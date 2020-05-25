package com.app.base.data;

import android.app.Application;

import com.android.sdk.net.NetContext;
import com.app.base.AppContext;
import com.app.base.BuildConfig;
import com.app.base.data.app.AppDataSource;
import com.app.base.debug.Debug;
import com.app.base.debug.EnvironmentContext;
import com.blankj.utilcode.util.NetworkUtils;

import timber.log.Timber;

import static com.app.base.data.NetProviderImplKt.newApiHandler;
import static com.app.base.data.NetProviderImplKt.newErrorDataAdapter;
import static com.app.base.data.NetProviderImplKt.newErrorMessage;
import static com.app.base.data.NetProviderImplKt.newHttpConfig;
import static com.app.base.data.URLProviderKt.API_HOST;
import static com.app.base.data.URLProviderKt.H5_HOST;
import static com.app.base.data.URLProviderKt.getAPIBaseURL;
import static com.app.base.data.URLProviderKt.getBaseWebURL;
import static com.app.base.data.URLSKt.addHost;

/**
 * Data层配置，抽象为DataContext
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-04-12 11:16
 */
public class DataContext {

    private static DataContext sDataContext;

    public synchronized static void init(Application application) {
        if (sDataContext != null) {
            throw new IllegalStateException("DataContext was  initialized");
        }
        sDataContext = new DataContext(application);
    }

    public static DataContext getInstance() {
        if (sDataContext == null) {
            throw new IllegalStateException("DataContext has not initialized");
        }
        return sDataContext;
    }

    private AppDataSource mAppDataSource;

    @SuppressWarnings("unused")
    private DataContext(Application application) {
        //环境初始化
        initEnvironment();
        //初始化网络库
        NetContext.get()
                .newBuilder()
                .aipHandler(newApiHandler())
                .httpConfig(newHttpConfig())
                .networkChecker(NetworkUtils::isConnected)
                //.postTransformer(null)
                .errorDataAdapter(newErrorDataAdapter())
                .errorMessage(newErrorMessage())
                .exceptionFactory(result -> null)
                .setup();
    }

    void publishLoginExpired(Throwable throwable) {
        AppContext.errorHandler().handleGlobalError(throwable);
        Timber.d("用户登录过期");
    }

    private void initEnvironment() {
        addHost();
        //如果规定了，选择某一个环境，则优先选择该环境
        if (Debug.isOpenDebug() && !BuildConfig.specifiedHost.equalsIgnoreCase("AUTO")) {
            Timber.d("BuildConfig.specifiedHost=>%s", BuildConfig.specifiedHost);
            EnvironmentContext.INSTANCE.select(API_HOST, BuildConfig.specifiedHost);
            EnvironmentContext.INSTANCE.select(H5_HOST, BuildConfig.specifiedHost);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // public
    ///////////////////////////////////////////////////////////////////////////
    public void onAppDataSourcePrepared(AppDataSource appDataSource) {
        if (mAppDataSource != null) {
            throw new IllegalStateException("DataContext.onAppDataSourcePrepared already called");
        }
        mAppDataSource = appDataSource;
    }

    public String baseWebUrl() {
        return getBaseWebURL();
    }

    static String baseAPIUrl() {
        return getAPIBaseURL();
    }

}