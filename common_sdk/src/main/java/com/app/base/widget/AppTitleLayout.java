package com.app.base.widget;


import static com.android.base.utils.android.views.ViewEx.getRealContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.base.architecture.fragment.tools.Fragments;
import com.android.base.ui.compat.Toolbar;
import com.android.base.utils.android.compat.AndroidVersion;
import com.android.base.utils.android.compat.SystemBarCompat;
import com.android.base.utils.android.views.TintKit;
import com.android.base.utils.common.Checker;
import com.app.base.R;
import com.google.android.material.internal.ToolbarUtils;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-12-12 16:43
 */
public class AppTitleLayout extends LinearLayout {

    private Toolbar mToolbar;

    private int mOriginalTopPadding;

    private View.OnClickListener onNavigationOnClickListener;

    private static final int INVALIDATE_ID = -1;

    public AppTitleLayout(@NonNull Context context) {
        this(context, null);
    }

    public AppTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AppTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AppTitleLayout);
        fillAttributes(context, typedArray);
        typedArray.recycle();
    }

    private void fillAttributes(@NonNull Context context, TypedArray typedArray) {
        //get all attributes
        String title = typedArray.getString(R.styleable.AppTitleLayout_atl_title);
        int menuResId = typedArray.getResourceId(R.styleable.AppTitleLayout_atl_menu_id, INVALIDATE_ID);
        boolean showCuttingLine = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_show_cutting_line, false);
        int cuttingLineBg = typedArray.getColor(R.styleable.AppTitleLayout_atl_show_cutting_line_bg, ContextCompat.getColor(getContext(), R.color.divider_color));
        boolean disableNavigation = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_disable_navigation, false);
        Drawable navigationIcon = typedArray.getDrawable(R.styleable.AppTitleLayout_atl_navigation_icon);
        int iconTintColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_navigation_icon_tint, -1);
        int titleColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_title_color, Color.BLACK);
        int menuColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_menu_color, Color.BLACK);
        boolean fitStatusInsetFor19 = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_fits_status_inset_for19, false);
        boolean fitStatusInsetAfter19 = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_fits_status_inset_after19, false);
        boolean titleCentered = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_title_centered, false);
        //add layout
        inflateLayout(context, fitStatusInsetFor19, fitStatusInsetAfter19);
        //get resource
        iniToolbar(title, titleCentered, showCuttingLine, titleColor, cuttingLineBg);
        //icon
        initNavigationIcon(disableNavigation, navigationIcon, iconTintColor);
        //menu
        initMenu(menuResId, menuColor);
    }

    private void inflateLayout(@NonNull Context context, boolean fitStatusInsetFor19, boolean fitStatusInsetAfter19) {
        mOriginalTopPadding = getPaddingTop();
        fitStatusInset(fitStatusInsetFor19, fitStatusInsetAfter19);
        setOrientation(VERTICAL);
        inflate(context, R.layout.widget_title_layout, this);
    }

    private void initMenu(int menuResId, int menuColor) {
        if (menuResId != INVALIDATE_ID) {
            mToolbar.inflateMenu(menuResId);
            setMenuColor(menuColor);
        }
    }

    private void initNavigationIcon(boolean disableNavigation, Drawable navigationIcon, int iconTintColor) {
        if (disableNavigation) {
            return;
        }

        if (navigationIcon != null) {
            if (iconTintColor == -1) {
                mToolbar.setNavigationIcon(navigationIcon);
            } else {
                mToolbar.setNavigationIcon(TintKit.tint(navigationIcon.mutate(), iconTintColor));
            }
        } else {
            if (iconTintColor == -1) {
                mToolbar.setNavigationIcon(R.drawable.icon_back);
            } else {
                mToolbar.setNavigationIcon(TintKit.tint(Checker.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.icon_back)).mutate(), iconTintColor));
            }
        }
    }

    private void iniToolbar(String title, boolean titleCentered, boolean showCuttingLime, int titleColor, int cuttingLineBg) {
        mToolbar = findViewById(R.id.common_toolbar);
        View cuttingLineView = findViewById(R.id.widgetAppTitleCuttingLine);
        cuttingLineView.setVisibility(showCuttingLime ? View.VISIBLE : View.GONE);
        cuttingLineView.setBackgroundColor(cuttingLineBg);
        //nav
        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.setTitleCentered(titleCentered);
        mToolbar.setNavigationOnClickListener(this::onNavigationOnClick);
        if (getBackground() != null) {
            mToolbar.setBackground(getBackground());
        }
        //title
        mToolbar.setTitle(title);
        mToolbar.setTitleTextColor(titleColor);
    }

    public void fitStatusInset(boolean fitStatusInsetFor19, boolean fitStatusInsetAfter19) {
        FragmentActivity realContext = getRealContext(this);
        if (realContext == null) {
            return;
        }
        //adjust for status bar
        if ((fitStatusInsetFor19 && AndroidVersion.at(19)) || (fitStatusInsetAfter19 && AndroidVersion.above(20))) {
            setPadding(getPaddingLeft(), mOriginalTopPadding + SystemBarCompat.getStatusBarHeight(realContext), getPaddingRight(), getPaddingBottom());
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setTitle(String title) {
        mToolbar.setTitle(title);

    }

    public void setLogo(int resID) {
        mToolbar.setLogo(resID);
    }

    public void setTitle(int titleId) {
        mToolbar.setTitle(getContext().getString(titleId));
    }

    public Menu getMenu() {
        return mToolbar.getMenu();
    }

    public void setOnNavigationOnClickListener(View.OnClickListener onNavigationOnClickListener) {
        this.onNavigationOnClickListener = onNavigationOnClickListener;
    }

    private void onNavigationOnClick(View v) {
        if (onNavigationOnClickListener != null) {
            onNavigationOnClickListener.onClick(v);
            return;
        }
        FragmentActivity realContext = getRealContext(this);
        if (realContext != null) {
            Fragments.exitFragment(realContext, false);
        } else {
            Timber.w("perform onNavigationOnClick --> fragmentBack, but real context can not be found");
        }
    }

    public void setMenuColor(@ColorInt int color) {
        setMenuColor(color, "");
    }

    public void setMenuColor(@ColorInt int color, String target) {
        View view;
        View innerView;

        for (int i = 0; i < mToolbar.getChildCount(); i++) {

            view = mToolbar.getChildAt(i);

            if (view instanceof ActionMenuView) {

                for (int j = 0; j < ((ActionMenuView) view).getChildCount(); j++) {
                    innerView = ((ActionMenuView) view).getChildAt(j);

                    if (!TextUtils.isEmpty(target)) {
                        if (innerView instanceof ActionMenuItemView && ((ActionMenuItemView) innerView).getText().equals(target)) {
                            ((ActionMenuItemView) innerView).setTextColor(color);
                            break;
                        }
                    } else {
                        if (innerView instanceof ActionMenuItemView) {
                            ((ActionMenuItemView) innerView).setTextColor(color);
                        }
                    }
                }

                break;
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    public View findMenuView(@IdRes int menuId) {
        return ToolbarUtils.getActionMenuItemView(getToolbar(), menuId);
    }

}