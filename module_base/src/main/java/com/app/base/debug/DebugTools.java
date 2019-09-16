package com.app.base.debug;

import android.util.Log;

import com.app.base.AppContext;

import org.joor.Reflect;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import timber.log.Timber;

public class DebugTools {

    public static void init(AppContext appContext) {
        if (Debug.isOpenDebug()) {
            Log.e("DebugTools", "=============================Base Debug mode is activate=============================");
            Log.e("DebugTools", "=============================Base Debug mode is activate=============================");
            Log.e("DebugTools", "=============================Base Debug mode is activate=============================");

            installLogger();
            installStetho(appContext);
            installLeakCanary(appContext);
        }
    }

    private static void installLogger() {
        Timber.plant(new Timber.DebugTree());
    }

    private static void installStetho(AppContext appContext) {
        Reflect.on("com.facebook.stetho.Stetho").call("initializeWithDefaults", appContext);
    }

    private static void installLeakCanary(AppContext appContext) {
        boolean isInAnalyzerProcess = Reflect.on("com.squareup.leakcanary.LeakCanary").call("isInAnalyzerProcess", appContext).get();
        if (!isInAnalyzerProcess) {
            Reflect.on("com.squareup.leakcanary.LeakCanary").call("install", appContext).get();
        }
    }

    public static void installStethoHttp(OkHttpClient.Builder builder) {
        if (Debug.isOpenDebug()) {
            Interceptor interceptor = Reflect.on("com.facebook.stetho.okhttp3.StethoInterceptor").create().get();
            builder.addNetworkInterceptor(interceptor);
        }
    }

}
