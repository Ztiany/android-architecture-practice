package com.gwchina.sdk.social.share;

import android.os.Parcel;
import android.os.Parcelable;

public class SharePlatform implements Parcelable {

    final int mPlatformId;
    private String mPlatformName;
    private int mDrawableId;

    /**
     * @param platformId {@link com.gwchina.sdk.social.Platforms} 中定义的平台标识。
     */
    public static SharePlatform newSharePlatform(int platformId) {
        return new SharePlatform(platformId);
    }

    private SharePlatform(int platformId) {
        mPlatformId = platformId;
    }

    @SuppressWarnings("all")
    protected SharePlatform(Parcel in) {
        mPlatformName = in.readString();
        mDrawableId = in.readInt();
        mPlatformId = in.readInt();
    }

    public static final Creator<SharePlatform> CREATOR = new Creator<SharePlatform>() {
        @Override
        public SharePlatform createFromParcel(Parcel in) {
            return new SharePlatform(in);
        }

        @Override
        public SharePlatform[] newArray(int size) {
            return new SharePlatform[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPlatformName);
        dest.writeInt(mDrawableId);
        dest.writeInt(mPlatformId);
    }

    public SharePlatform setDrawableId(int drawableId) {
        mDrawableId = drawableId;
        return this;
    }

    public SharePlatform setPlatformName(String platformName) {
        mPlatformName = platformName;
        return this;
    }

    int getDrawableId() {
        return mDrawableId;
    }

    public String getPlatformName() {
        return mPlatformName;
    }

}
