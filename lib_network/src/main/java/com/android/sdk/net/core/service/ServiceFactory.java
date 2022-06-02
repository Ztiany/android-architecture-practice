package com.android.sdk.net.core.service;

import com.android.sdk.net.core.json.ErrorJsonLenientConverterFactory;
import com.android.sdk.net.core.json.GsonUtils;
import com.android.sdk.net.core.progress.RequestProgressInterceptor;
import com.android.sdk.net.core.progress.ResponseProgressInterceptor;
import com.android.sdk.net.core.progress.UrlProgressListener;
import com.android.sdk.net.core.provider.HttpConfig;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Ztiany
 */
public class ServiceFactory {

    private final OkHttpClient mOkHttpClient;
    private final String mBaseUrl;
    private final Retrofit mRetrofit;

    private Boolean mHasRxJava2 = null;

    ServiceFactory(OkHttpClient okHttpClient, HttpConfig httpConfig) {
        mOkHttpClient = okHttpClient;
        mBaseUrl = httpConfig.baseUrl();

        Retrofit.Builder builder = new Retrofit.Builder();

        if (!httpConfig.configRetrofit(mOkHttpClient, builder)) {
            builder.baseUrl(mBaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(new ErrorJsonLenientConverterFactory(GsonConverterFactory.create(GsonUtils.gson())));

            addRxJava2CallAdapterIfNeed(builder);

        }

        mRetrofit = builder.build();
    }

    private void addRxJava2CallAdapterIfNeed(Retrofit.Builder builder) {
        if (mHasRxJava2 == null) {
            try {
                Class.forName("retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory");
                mHasRxJava2 = true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                mHasRxJava2 = false;
            }
        }

        if (mHasRxJava2) {
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
        }
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }

    public <T> T createWithUploadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient
                .newBuilder()
                .addNetworkInterceptor(new RequestProgressInterceptor(urlProgressListener))
                .build();
        Retrofit newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build();
        return newRetrofit.create(clazz);
    }

    public <T> T createWithDownloadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient
                .newBuilder()
                .addNetworkInterceptor(new ResponseProgressInterceptor(urlProgressListener))
                .build();
        Retrofit newRetrofit = mRetrofit.newBuilder().client(okHttpClient).build();
        return newRetrofit.create(clazz);
    }

    public String baseUrl() {
        return mBaseUrl;
    }

}
