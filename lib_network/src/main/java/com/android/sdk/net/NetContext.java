package com.android.sdk.net;

import android.support.annotation.NonNull;

import com.android.sdk.net.provider.NetProvider;
import com.android.sdk.net.service.ServiceFactory;
import com.android.sdk.net.service.ServiceHelper;

import okhttp3.OkHttpClient;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-08 11:06
 */
public class NetContext {

    private static volatile NetContext CONTEXT;

    public static NetContext get() {
        if (CONTEXT == null) {
            synchronized (NetContext.class) {
                if (CONTEXT == null) {
                    CONTEXT = new NetContext();
                }
            }
        }
        return CONTEXT;
    }

    private NetContext() {
        mServiceHelper = new ServiceHelper();
    }

    private NetProvider mDefaultProvider;
    private ServiceHelper mServiceHelper;

    public void initialize(@NonNull NetProvider netProvider) {
        mDefaultProvider = netProvider;
    }

    public boolean connected() {
        return mDefaultProvider.isConnected();
    }

    public NetProvider netProvider() {
        NetProvider retProvider = mDefaultProvider;

        if (retProvider == null) {
            throw new RuntimeException("NetContext has not be initialized");
        }
        return retProvider;
    }

    public OkHttpClient httpClient() {
        return mServiceHelper.getOkHttpClient(netProvider().httpConfig());
    }

    public ServiceFactory serviceFactory() {
        return mServiceHelper.getServiceFactory(netProvider().httpConfig());
    }

}
