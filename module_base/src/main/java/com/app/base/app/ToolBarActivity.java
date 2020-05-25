package com.app.base.app;

import android.os.Bundle;

import com.app.base.R;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;


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
    protected void initializeView(@Nullable Bundle savedInstanceState) {
        super.initializeView(savedInstanceState);
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
