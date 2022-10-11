package com.android.base.ui.insets;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import java.lang.reflect.Field;

/**
 * @author Ztiany
 */
public class NoInsetsCoordinatorLayout extends CoordinatorLayout {

    {
        try {
            Field mApplyWindowInsetsListener = CoordinatorLayout.class.getDeclaredField("mApplyWindowInsetsListener");
            mApplyWindowInsetsListener.setAccessible(true);
            mApplyWindowInsetsListener.set(this, (OnApplyWindowInsetsListener) (view, windowInsetsCompat) -> null);
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
