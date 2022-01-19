package com.vclusters.cloud.account.widget;

import android.graphics.Bitmap;

public class IconsAttrs {

    private Bitmap mClearBitmap;
    private Bitmap mTailingOnBitmap;
    private Bitmap mTailingOffBitmap;
    private boolean mTailingIconEnable;
    private boolean mContentClearableEnable;

    public Bitmap getClearBitmap() {
        return mClearBitmap;
    }

    public void setClearBitmap(Bitmap clearBitmap) {
        mClearBitmap = clearBitmap;
    }

    public Bitmap getTailingOnBitmap() {
        return mTailingOnBitmap;
    }

    public void setTailingOnBitmap(Bitmap tailingOnBitmap) {
        mTailingOnBitmap = tailingOnBitmap;
    }

    public Bitmap getTailingOffBitmap() {
        return mTailingOffBitmap;
    }

    public void setTailingOffBitmap(Bitmap tailingOffBitmap) {
        mTailingOffBitmap = tailingOffBitmap;
    }

    public boolean isTailingIconEnable() {
        return mTailingIconEnable;
    }

    public void setTailingIconEnable(boolean tailingIconEnable) {
        mTailingIconEnable = tailingIconEnable;
    }

    public boolean isContentClearableEnable() {
        return mContentClearableEnable;
    }

    public void setContentClearableEnable(boolean contentClearableEnable) {
        mContentClearableEnable = contentClearableEnable;
    }

}