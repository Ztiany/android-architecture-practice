package com.android.architecture;


import android.app.Activity;
import android.content.Intent;

import com.android.architecture.launcher.AppLauncherActivity;
import com.app.base.AppContext;

import org.jetbrains.annotations.NotNull;

import dagger.hilt.android.HiltAndroidApp;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-02 16:32
 */
@HiltAndroidApp
public class ArchAppContext extends AppContext {

    @Override
    public void restartApp(@NotNull Activity activity) {
        Intent intent = new Intent(this, AppLauncherActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
