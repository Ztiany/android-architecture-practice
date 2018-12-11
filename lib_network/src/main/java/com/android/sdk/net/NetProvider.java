package com.android.sdk.net;

import android.support.annotation.NonNull;

import com.android.sdk.net.provider.ApiHandler;
import com.android.sdk.net.provider.ErrorDataAdapter;
import com.android.sdk.net.provider.ErrorMessage;
import com.android.sdk.net.provider.HttpConfig;
import com.android.sdk.net.provider.NetworkChecker;

public interface NetProvider {

    boolean isConnected();

    ApiHandler aipHandler();

    HttpConfig httpConfig();

    @NonNull
    ErrorMessage errorMessage();

    @NonNull
    ErrorDataAdapter errorDataAdapter();

}

class NetProviderImpl implements NetProvider {

    ApiHandler mApiHandler;
    HttpConfig mHttpConfig;
    ErrorMessage mErrorMessage;
    ErrorDataAdapter mErrorDataAdapter;
    NetworkChecker mNetworkChecker;

    @Override
    public boolean isConnected() {
        return mNetworkChecker.isConnected();
    }

    @NonNull
    @Override
    public ApiHandler aipHandler() {
        return mApiHandler;
    }

    @NonNull
    @Override
    public HttpConfig httpConfig() {
        return mHttpConfig;
    }

    @NonNull
    @Override
    public ErrorMessage errorMessage() {
        return mErrorMessage;
    }

    @NonNull
    @Override
    public ErrorDataAdapter errorDataAdapter() {
        return mErrorDataAdapter;
    }

    void checkRequired() {
        if (mErrorMessage == null || mErrorDataAdapter == null || mNetworkChecker == null || mHttpConfig == null) {
            throw new NullPointerException("缺少必要的参数，必须提供：ErrorMessage、mErrorDataAdapter、mNetworkChecker、HttpConfig。");
        }
    }

}