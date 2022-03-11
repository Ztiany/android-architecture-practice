package com.app.base.common.web;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import timber.log.Timber;

/**
 * @author Ztiany
 * Date : 2017-09-06 11:47
 */
class WebProgress {

    private static final int MAX_PROGRESS = 100;
    private ObjectAnimator mProgressAnimator;
    private final ProgressBar mProgressBar;

    WebProgress(ProgressBar progressBar) {
        mProgressBar = progressBar;
        mProgressBar.setMax(MAX_PROGRESS * MAX_PROGRESS);

        mProgressBar.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
            @Override
            public void onWindowAttached() {
                Timber.d("onWindowAttached() called");
            }

            @Override
            public void onWindowDetached() {
                Timber.d("onWindowDetached() called");
                if (mProgressAnimator != null) {
                    mProgressAnimator.cancel();
                }
            }
        });
    }

    void onProgress(int newProgress) {
        Timber.d("newProgress =%s", newProgress);
        if (newProgress < MAX_PROGRESS) { //没有到达100
            if (mProgressAnimator != null) {
                mProgressAnimator.cancel();
            }
            mProgressBar.setAlpha(1);
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", mProgressBar.getProgress(), newProgress * MAX_PROGRESS);
            mProgressAnimator.setDuration(500);
            mProgressAnimator.setInterpolator(new DecelerateInterpolator());
            mProgressAnimator.start();
        } else { //加载完毕
            if (mProgressAnimator != null) {
                mProgressAnimator.cancel();
            }
            mProgressAnimator = ObjectAnimator.ofInt(mProgressBar, "progress", mProgressBar.getProgress(), MAX_PROGRESS * MAX_PROGRESS);
            mProgressAnimator.setDuration(500);
            mProgressAnimator.setInterpolator(new DecelerateInterpolator());
            mProgressAnimator.start();
            mProgressAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.animate().alpha(0).setDuration(500).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mProgressBar.setVisibility(View.GONE);
                            mProgressBar.setProgress(0);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            super.onAnimationCancel(animation);
                            mProgressBar.setVisibility(View.GONE);
                            mProgressBar.setProgress(0);
                        }
                    });
                }
            });
        }
    }

}