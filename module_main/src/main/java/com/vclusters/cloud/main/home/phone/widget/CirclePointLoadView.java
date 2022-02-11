package com.vclusters.cloud.main.home.phone.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.vclusters.cloud.main.R;

import me.ztiany.widget.common.Sizes;


public class CirclePointLoadView extends RelativeLayout {

    private final int defaultLeftColor = Color.parseColor("#94A9FD");
    private final int defaultMiddleColor = Color.parseColor("#2953FF");
    private final int defaultRightColor = Color.parseColor("#FCCA1E");

    private int leftColor;
    private int middleColor;
    private int rightColor;
    private int translationDistance;

    private static final long ANIMATION_DURATION = 400;
    private int radius;

    private boolean isAnimationRunning = false;

    private CircleItemPointView leftView;
    private CircleItemPointView middleView;
    private CircleItemPointView rightView;
    private AnimatorSet spreadAnimation;

    public CirclePointLoadView(Context context) {
        this(context, null);
    }

    public CirclePointLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CirclePointLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        addViewToLayout(context);
    }

    private void addViewToLayout(Context context) {
        if (null == context) {
            return;
        }
        leftView = createView(context);
        leftView.setColor(leftColor);
        middleView = createView(context);
        middleView.setColor(middleColor);
        rightView = createView(context);
        rightView.setColor(rightColor);
        addView(leftView);
        addView(rightView);
        addView(middleView);
    }

    /**
     * 展开动画
     */
    private void spreadAnimation() {
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(leftView, "translationX", 0, -translationDistance);
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(rightView, "translationX", 0, translationDistance);
        spreadAnimation = new AnimatorSet();
        spreadAnimation.setDuration(ANIMATION_DURATION);
        spreadAnimation.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        spreadAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                closedAnimation();
            }
        });
        spreadAnimation.start();
    }

    private void closedAnimation() {
        ObjectAnimator leftTranslationAnimator = ObjectAnimator.ofFloat(leftView, "translationX", -translationDistance, 0);
        ObjectAnimator rightTranslationAnimator = ObjectAnimator.ofFloat(rightView, "translationX", translationDistance, 0);
        AnimatorSet closedAnimation = new AnimatorSet();
        closedAnimation.setInterpolator(new AccelerateInterpolator());
        closedAnimation.setDuration(ANIMATION_DURATION);
        closedAnimation.playTogether(leftTranslationAnimator, rightTranslationAnimator);
        closedAnimation.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                int leftColor = leftView.getColor();
                int rightColor = rightView.getColor();
                int middleColor = middleView.getColor();
                middleView.changeColor(leftColor);
                rightView.changeColor(middleColor);
                leftView.changeColor(rightColor);
                spreadAnimation();
            }
        });
        closedAnimation.start();
    }

    public CircleItemPointView createView(Context context) {
        CircleItemPointView itemPointView = new CircleItemPointView(context);
        LayoutParams params = new LayoutParams(Sizes.dpToPx(context, radius), Sizes.dpToPx(context, radius));
        params.addRule(CENTER_IN_PARENT);
        itemPointView.setLayoutParams(params);
        return itemPointView;
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        if (null == context || null == attrs) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirclePointLoadView);
        leftColor = typedArray.getColor(R.styleable.CirclePointLoadView_cpl_leftPointColor, defaultLeftColor);
        middleColor = typedArray.getColor(R.styleable.CirclePointLoadView_cpl_middlePointColor, defaultMiddleColor);
        rightColor = typedArray.getColor(R.styleable.CirclePointLoadView_cpl_rightPointColor, defaultRightColor);
        int defaultCircleRadius = 10;
        radius = typedArray.getInteger(R.styleable.CirclePointLoadView_cpl_radius, defaultCircleRadius);
        int defaultTranslationDistance = 20;
        translationDistance = (int) typedArray.getDimension(R.styleable.CirclePointLoadView_cpl_translationDistance, defaultTranslationDistance);
        translationDistance = Sizes.dpToPx(context, translationDistance);
        typedArray.recycle();
    }

    public void startLoad() {
        if (spreadAnimation == null) {
            spreadAnimation();
            isAnimationRunning = true;
        }
    }

    public void stopLoad() {
        clearAnimation();
        isAnimationRunning = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (isAnimationRunning) {
            startLoad();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clearAnimation();
    }

    public void setLeftColor(int leftColor) {
        this.leftColor = leftColor;
    }

    public void setMiddleColor(int middleColor) {
        this.middleColor = middleColor;
    }

    public void setRightColor(int rightColor) {
        this.rightColor = rightColor;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public static class CircleItemPointView extends View {

        private final Paint mPaint;
        private int mColor;

        public CircleItemPointView(Context context) {
            this(context, null);
        }

        public CircleItemPointView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CircleItemPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            canvas.drawCircle(cx, cy, cx, mPaint);
        }

        public void changeColor(int color) {
            mColor = color;
            mPaint.setColor(color);
            invalidate();
        }

        public void setColor(int color) {
            mColor = color;
            mPaint.setColor(color);
            invalidate();
        }

        public int getColor() {
            return mColor;
        }

    }


}