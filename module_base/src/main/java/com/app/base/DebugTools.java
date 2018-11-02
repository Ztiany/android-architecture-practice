package com.app.base;

import com.android.base.utils.android.DebugUtils;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

class DebugTools {

    static void init(AppContext appContext) {
        if (!BuildConfig.openDebug) {
            return;
        }

        Timber.plant(new Timber.DebugTree());

        Timber.w("=============================Base Debug mode is activate=============================");

        Stetho.initializeWithDefaults(appContext);

        DebugUtils.startStrictMode();

        if (!LeakCanary.isInAnalyzerProcess(appContext)) {
            LeakCanary.install(appContext);
        }

    }
}
