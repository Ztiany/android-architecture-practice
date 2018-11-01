package com.app.base.data;

import android.support.annotation.NonNull;

import com.app.base.data.net.ErrorJsonLenientConverterFactory;
import com.app.base.data.progress.RequestProgressInterceptor;
import com.app.base.data.progress.ResponseProgressInterceptor;
import com.app.base.data.progress.UrlProgressListener;
import com.google.gson.Gson;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-06-07 18:19
 */
public class ServiceFactory {

    private final OkHttpClient mOkHttpClient;
    private final URLProvider mURLProvider;
    private final Gson mGson;

    private Retrofit mRetrofit;
    private int mEnvIdentification;
    private String mBaseUrl;

    ServiceFactory(OkHttpClient okHttpClient, Gson gson, URLProvider urlProvider) {
        mOkHttpClient = okHttpClient;
        mGson = gson;
        mURLProvider = urlProvider;
    }

    @NonNull
    private Retrofit createRetrofit(OkHttpClient okHttpClient, Gson gson) {
        mBaseUrl = mURLProvider.baseUrl();
        return new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(new ErrorJsonLenientConverterFactory(GsonConverterFactory.create(gson)))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    private Retrofit getRetrofit() {
        if (mEnvIdentification != DataContext.getInstance().getHostIdentification()) {
            mRetrofit = null;
            mEnvIdentification = DataContext.getInstance().getHostIdentification();
        }
        if (mRetrofit == null) {
            mRetrofit = createRetrofit(mOkHttpClient, mGson);
        }
        return mRetrofit;
    }

    public <T> T create(Class<T> clazz) {
        return getRetrofit().create(clazz);
    }

    public <T> T createWithUploadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient.newBuilder()
                .addNetworkInterceptor(new RequestProgressInterceptor(urlProgressListener)).build();
        Retrofit retrofit = getRetrofit().newBuilder().client(okHttpClient).build();
        return retrofit.create(clazz);
    }

    public <T> T createWithDownloadProgress(Class<T> clazz, UrlProgressListener urlProgressListener) {
        OkHttpClient okHttpClient = mOkHttpClient.newBuilder()
                .addNetworkInterceptor(new ResponseProgressInterceptor(urlProgressListener)).build();
        Retrofit retrofit = getRetrofit().newBuilder().client(okHttpClient).build();
        return retrofit.create(clazz);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Base uri
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取当前App的API接口使用的url
     *
     * @return base url
     */
    public String baseApiUrl() {
        return mBaseUrl;
    }

    /**
     * 获取当前App的基础url
     *
     * @return base url
     */
    public String baseUrl() {
        return mBaseUrl.replace("/api/", "");
    }

}
