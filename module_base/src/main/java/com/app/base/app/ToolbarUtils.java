package com.app.base.app;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.android.base.app.fragment.tools.FragmentUtils;

import com.android.base.app.CommonId;
import com.android.base.utils.android.TintUtils;
import com.app.base.R;

import static com.android.base.app.CommonId.TOOLBAR_ID;

/**
 * Fragment中使用Toolbar的帮助类，需要设置toolbar的id为：{@link CommonId#TOOLBAR_ID}
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-12-20 16:16
 */
public class ToolbarUtils {

    private static final int INVALIDATE_ID = 0;

    private static int sTintColor = -1;
    private static int sBackDrawableId = R.drawable.icon_back;

    private View.OnClickListener onNavigationOnClickListener;

    private final int mTitleResId;
    private final int mMenuResId;

    private final Fragment mFragment;

    private Toolbar mToolbar;

    public static void setDrawableTintColor(@ColorInt int tintColor) {
        sTintColor = tintColor;
    }

    private ToolbarUtils(int titleResId, int menuResId, Fragment fragment) {
        mTitleResId = titleResId;
        mMenuResId = menuResId;
        mFragment = fragment;
    }

    public static ToolbarUtils setupToolBar(Fragment fragment) {
        return setupToolBar(fragment, fragment.getView(), INVALIDATE_ID);
    }

    public static ToolbarUtils setupToolBar(Fragment fragment, int titleResId) {
        if (fragment.getView() == null) {
            throw new NullPointerException(fragment.getClass().getName() + "getView() = null");
        }
        return setupToolBar(fragment, fragment.getView(), titleResId, INVALIDATE_ID);
    }

    public static ToolbarUtils setupToolBar(Fragment fragment, View layoutView) {
        return setupToolBar(fragment, layoutView, INVALIDATE_ID);
    }

    public static ToolbarUtils setupToolBar(Fragment fragment, View layoutView, int titleResId) {
        return setupToolBar(fragment, layoutView, titleResId, INVALIDATE_ID);
    }

    public static ToolbarUtils setupToolBar(Fragment fragment, View layoutView, int titleResId, int menuResId) {
        ToolbarUtils toolbarUtils = new ToolbarUtils(titleResId, menuResId, fragment);
        toolbarUtils.mToolbar = layoutView.findViewById(TOOLBAR_ID);
        toolbarUtils.setupToolbar();
        return toolbarUtils;
    }

    private void setupToolbar() {
        if (mToolbar == null) {
            throw new NullPointerException(" you need provide toolbar in your xml");
        }
        //nav
        Drawable drawable = ContextCompat.getDrawable(mToolbar.getContext(), sBackDrawableId);
        if (drawable != null) {
            if (sTintColor != -1) {
                Drawable mutate = drawable.mutate();
                mToolbar.setNavigationIcon(TintUtils.tint(mutate, sTintColor));
            } else {
                mToolbar.setNavigationIcon(drawable);
            }
        }
        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.setNavigationOnClickListener(this::onNavigationOnClick);
        //title
        if (mTitleResId != INVALIDATE_ID) {
            mToolbar.setTitle(mTitleResId);
        }
        //menu
        if (mMenuResId != INVALIDATE_ID) {
            mToolbar.inflateMenu(mMenuResId);
        }
    }

    private void onNavigationOnClick(View v) {
        if (onNavigationOnClickListener != null) {
            onNavigationOnClickListener.onClick(v);
            return;
        }
        FragmentActivity activity = mFragment.getActivity();
        if (activity != null) {
            FragmentUtils.fragmentBack(activity, false);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public Menu getMenu() {
        return mToolbar.getMenu();
    }

    public ToolbarUtils inflateMenu(@MenuRes int idRes) {
        getToolbar().inflateMenu(idRes);
        return this;
    }

    public ToolbarUtils setOnMenuClick(Toolbar.OnMenuItemClickListener onMenuClick) {
        getToolbar().setOnMenuItemClickListener(onMenuClick);
        return this;
    }

    public ToolbarUtils setOnNavigationOnClickListener(View.OnClickListener onNavigationOnClickListener) {
        this.onNavigationOnClickListener = onNavigationOnClickListener;
        return this;
    }

    public ToolbarUtils setTitle(CharSequence title) {
        getToolbar().setTitle(title);
        return this;
    }

    public ToolbarUtils setTitle(@StringRes int title) {
        getToolbar().setTitle(title);
        return this;
    }

}
