package com.app.base.widget.insets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;

import java.lang.reflect.Field;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2019-04-18 18:31
 */
public class NoInsetsCoordinatorLayout extends CoordinatorLayout {

    {
        try {
            Field mApplyWindowInsetsListener = CoordinatorLayout.class.getDeclaredField("mApplyWindowInsetsListener");
            mApplyWindowInsetsListener.setAccessible(true);
            mApplyWindowInsetsListener.set(this, (android.support.v4.view.OnApplyWindowInsetsListener) (view, windowInsetsCompat) -> null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public NoInsetsCoordinatorLayout(@NonNull Context context) {
        super(context);
    }

    public NoInsetsCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoInsetsCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}
