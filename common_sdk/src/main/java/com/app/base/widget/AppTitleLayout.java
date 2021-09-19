package com.app.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import com.android.base.app.fragment.tools.Fragments;
import com.android.base.utils.android.compat.AndroidVersion;
import com.android.base.utils.android.compat.SystemBarCompat;
import com.android.base.utils.android.views.TintUtils;
import com.android.base.utils.common.Checker;
import com.app.base.R;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import timber.log.Timber;

import static com.android.base.utils.android.views.ViewExKt.getRealContext;

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
        boolean showCuttingLime = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_show_cutting_line, false);
        boolean disableNavigation = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_disable_navigation, false);
        Drawable navigationIcon = typedArray.getDrawable(R.styleable.AppTitleLayout_atl_navigation_icon);
        int iconTintColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_navigation_icon_tint, -1);
        int titleColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_title_color, Color.WHITE);
        int menuColor = typedArray.getColor(R.styleable.AppTitleLayout_atl_menu_color, Color.WHITE);
        boolean adjustForStatusBar = typedArray.getBoolean(R.styleable.AppTitleLayout_atl_adjust_for_status, false);
        //add layout
        inflateLayout(context, adjustForStatusBar);
        //get resource
        iniToolbar(title, showCuttingLime, titleColor);
        //icon
        initNavigationIcon(disableNavigation, navigationIcon, iconTintColor);
        //menu
        initMenu(menuResId, menuColor);
    }

    private void inflateLayout(@NonNull Context context, boolean adjustForStatusBar) {
        mOriginalTopPadding = getPaddingTop();
        adjustForStatusBar(adjustForStatusBar);
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
                mToolbar.setNavigationIcon(TintUtils.tint(navigationIcon.mutate(), iconTintColor));
            }
        } else {
            if (iconTintColor == -1) {
                mToolbar.setNavigationIcon(R.drawable.icon_back);
            } else {
                mToolbar.setNavigationIcon(TintUtils.tint(Checker.requireNonNull(ContextCompat.getDrawable(getContext(), R.drawable.icon_back)).mutate(), iconTintColor));
            }
        }
    }

    private void iniToolbar(String title, boolean showCuttingLime, int titleColor) {
        mToolbar = findViewById(R.id.common_toolbar);
        View cuttingLineView = findViewById(R.id.widgetAppTitleCuttingLine);
        cuttingLineView.setVisibility(showCuttingLime ? View.VISIBLE : View.GONE);
        //nav
        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.setNavigationOnClickListener(this::onNavigationOnClick);
        if (getBackground() != null) {
            mToolbar.setBackground(getBackground());
        }
        //title
        mToolbar.setTitle(title);
        mToolbar.setTitleTextColor(titleColor);
    }

    public void adjustForStatusBar(boolean adjustForStatusBar) {
        //adjust for status bar
        if (AndroidVersion.atLeast(19)) {
            if (adjustForStatusBar) {
                setPadding(getPaddingLeft(), mOriginalTopPadding + SystemBarCompat.getStatusBarHeight(getContext()), getPaddingRight(), getPaddingBottom());
            } else {
                setPadding(getPaddingLeft(), mOriginalTopPadding, getPaddingRight(), getPaddingBottom());
            }
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

}