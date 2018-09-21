package com.app.base.data.net;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Ztiany
 * Date : 2018-08-13 15:48
 */
public class ServiceFactory {

    @NonNull
    private Retrofit createRetrofit(OkHttpClient okHttpClient, Gson gson, String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(new ErrorJsonLenientConverterFactory(GsonConverterFactory.create(gson)))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

}
