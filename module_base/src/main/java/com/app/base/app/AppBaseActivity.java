package com.app.base.app;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.android.base.app.activity.BaseActivity;
import com.android.base.utils.android.compat.SystemBarCompat;
import com.app.base.R;
import com.app.base.router.RouterManager;

/**
 * @author Ztiany
 * Date : 2018-09-21 14:34
 */
public abstract class AppBaseActivity extends BaseActivity {

    @Override
    protected void initialize(@Nullable Bundle savedInstanceState) {
        super.initialize(savedInstanceState);
        if (hasRouterArguments()) {
            RouterManager.inject(this);
        }
    }

    @Override
    @CallSuper
    protected void setupView(@Nullable Bundle savedInstanceState) {
        if (tintStatusBar()) {
            SystemBarCompat.setTranslucentStatusOnKitkat(this);
            SystemBarCompat.setStatusBarColorOnKitkat(this, ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    protected boolean hasRouterArguments() {
        return false;
    }

    protected boolean tintStatusBar() {
        return true;
    }

}