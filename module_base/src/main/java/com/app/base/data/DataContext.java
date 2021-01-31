package com.app.base.data;

import com.android.sdk.net.NetContext;
import com.app.base.AppContext;
import com.app.base.BuildConfig;
import com.app.base.data.app.AppDataSource;
import com.blankj.utilcode.util.NetworkUtils;

import timber.log.Timber;

import static com.app.base.data.ENVSKt.addEnvironmentValues;
import static com.app.base.data.NetProviderImplKt.newApiHandler;
import static com.app.base.data.NetProviderImplKt.newErrorDataAdapter;
import static com.app.base.data.NetProviderImplKt.newErrorMessage;
import static com.app.base.data.NetProviderImplKt.newHttpConfig;
import static com.app.base.data.URLProviderKt.getAPIBaseURL;
import static com.app.base.data.URLProviderKt.getBaseWebURL;
import static com.app.base.data.URLProviderKt.selectSpecified;

/**
 * Data 层配置。
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-04-12 11:16
 */
public class DataContext {

    private static DataContext sDataContext;

    public synchronized static void init(AppContext appContext) {
        if (sDataContext != null) {
            throw new IllegalStateException("DataContext was  initialized");
        }
        sDataContext = new DataContext(appContext);
    }

    public static DataContext getInstance() {
        if (sDataContext == null) {
            throw new IllegalStateException("DataContext has not initialized");
        }
        return sDataContext;
    }

    private AppDataSource mAppDataSource;

    @SuppressWarnings("unused")
    private DataContext(AppContext appContext) {
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
        addEnvironmentValues();
        //如果规定了，选择某一个环境，则优先选择该环境
        if (!"AUTO".equalsIgnoreCase(BuildConfig.specifiedHost)) {
            Timber.d("BuildConfig.specifiedHost =>%s", BuildConfig.specifiedHost);
            selectSpecified(BuildConfig.specifiedHost);
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

    public static String baseWebUrl() {
        return getBaseWebURL();
    }

    public static String baseApiUrl() {
        return getAPIBaseURL();
    }

}