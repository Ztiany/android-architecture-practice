package com.app.base.common.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import timber.log.Timber;

public class PullBackLayout extends FrameLayout {

    private final ViewDragHelper drag;

    private final int minimumFlingVelocity;

    private Callback callback;

    public PullBackLayout(Context context) {
        this(context, null);
    }

    public PullBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        drag = ViewDragHelper.create(this, 1f, new ViewDragCallback());
        minimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return drag.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        try {
            drag.processTouchEvent(event);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void computeScroll() {
        if (drag.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public interface Callback {

        void onPullStart();

        void onPull(float progress);

        void onPullCancel();

        void onPullComplete();

    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NotNull View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(@NotNull View child, int left, int dx) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(@NotNull View child, int top, int dy) {
            return Math.max(0, top);
        }

        @Override
        public boolean checkInterceptionSlope(View toCapture, float dx, float dy) {
            float slope = Math.abs(dy) / Math.abs(dx);
            Timber.d("dx = %f, dy = %f, slope = %f", dx, dy, slope);
            return slope > 1.5F;
        }

        @Override
        public int getViewHorizontalDragRange(@NotNull View child) {
            return 0;
        }

        @Override
        public int getViewVerticalDragRange(@NotNull View child) {
            return getHeight();
        }

        @Override
        public void onViewCaptured(@NotNull View capturedChild, int activePointerId) {
            if (callback != null) {
                callback.onPullStart();
            }
        }

        @Override
        public void onViewPositionChanged(@NotNull View changedView, int left, int top, int dx, int dy) {
            if (callback != null) {
                callback.onPull((float) top / (float) getHeight());
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int slop = yvel > minimumFlingVelocity ? getHeight() / 6 : getHeight() / 3;
            if (releasedChild.getTop() > slop) {
                if (callback != null) {
                    callback.onPullComplete();
                }
            } else {
                if (callback != null) {
                    callback.onPullCancel();
                }
                drag.settleCapturedViewAt(0, 0);
                invalidate();
            }
        }

    }

}