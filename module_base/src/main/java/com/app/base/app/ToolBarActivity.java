package com.app.base.app;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.app.base.R;


/**
 * 封装toolbar的操作
 *
 * @author Ztiany
 * Date : 2016-09-02 18:33
 */
public abstract class ToolBarActivity extends AppBaseActivity {

    protected Toolbar mToolbar;

    @Override
    @CallSuper
    protected void setupView(@Nullable Bundle savedInstanceState) {
        super.setupView(savedInstanceState);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = findViewById(R.id.common_toolbar);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            mToolbar.setContentInsetStartWithNavigation(0);
            mToolbar.setNavigationOnClickListener(v -> onNavigationClick());
        }
    }

    @SuppressWarnings("unused")
    protected final void setToolbarTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @SuppressWarnings("unused")
    protected final void setToolbarTitle(@StringRes int titleRes) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleRes);
        }
    }

    protected void onNavigationClick() {
        onBackPressed();
    }

}
