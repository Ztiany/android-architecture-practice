package me.ztiany.arch.home.main;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.base.utils.android.compat.AndroidVersion;
import com.android.base.utils.android.compat.SystemBarCompat;

import me.ztiany.architecture.home.R;


final class TranslucentHelper {

    private AppCompatActivity mHost;

    TranslucentHelper(AppCompatActivity appCompatActivity) {
        mHost = appCompatActivity;
    }

    void setTranslucentStatus() {
        //设置TranslucentStatus
        SystemBarCompat.setTranslucent(mHost, true, false);
        addStatusBarView();
    }

    private void addStatusBarView() {
        if (!AndroidVersion.atLeast(19)) {
            return;
        }
        View statusBarTintView = new View(mHost);
        FrameLayout.LayoutParams mStatusBarParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, SystemBarCompat.getStatusBarHeight(mHost));
        mStatusBarParams.gravity = Gravity.TOP;
        statusBarTintView.setLayoutParams(mStatusBarParams);
        ViewGroup decorView = (ViewGroup) mHost.getWindow().getDecorView();
        decorView.addView(statusBarTintView, decorView.getChildCount());
        statusBarTintView.setBackgroundColor(ContextCompat.getColor(mHost, R.color.colorPrimary));
    }

}
