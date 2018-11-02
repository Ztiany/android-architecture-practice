package com.app.base.widget.banner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.base.imageloader.ImageLoaderFactory;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;


class BannerPagerAdapter extends PagerAdapter {

    private final String mTransitionName;
    private final Context mContext;
    private final List<String> mEntities;
    private OnPageClickListener mClickListener;
    private boolean mIsLooper;

    BannerPagerAdapter(Context context, List<String> entities, String transitionName) {
        mContext = context;
        this.mEntities = entities;
        mTransitionName = transitionName;
        mIsLooper = mEntities.size() > 1;
    }

    void setOnBannerClickListener(OnPageClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public int getCount() {
        return mEntities.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        PhotoView bannerLayout = new PhotoView(mContext);
        setTransitionName(bannerLayout);

        bannerLayout.setOnPhotoTapListener((view, x, y) -> {
            if (mClickListener != null) {
                mClickListener.onClick(bannerLayout, mIsLooper ? position - 1 : position);
            }
        });
        String url = mEntities.get(position);
        ImageLoaderFactory.getImageLoader().display(bannerLayout, url);
        container.addView(bannerLayout, 0);
        return bannerLayout;
    }

    private void setTransitionName(ImageView bannerLayout) {
        if (!TextUtils.isEmpty(mTransitionName)) {
            ViewCompat.setTransitionName(bannerLayout, mTransitionName);
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }


}
