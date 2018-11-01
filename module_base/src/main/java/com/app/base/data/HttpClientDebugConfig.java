package com.app.base.data;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.readystatesoftware.chuck.ChuckInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

final class HttpClientDebugConfig {

    static void config(OkHttpClient.Builder builder, Context context) {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                message -> Timber.tag("===OkHttp===").i(message)
        );

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ChuckInterceptor(context))
                .addNetworkInterceptor(new StethoInterceptor());
    }

}
