package me.ztiany.architecture.home;


import com.app.base.AppContext;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class HomeAppContext extends AppContext {

    @Override
    public void onCreate() {
        initBeforeInject();
        super.onCreate();
    }

}