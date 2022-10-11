package com.android.base.ui.banner;

/**
 * @author Ztiany
 */
public interface IPagerNumberView {

    void setViewPager(BannerViewPager viewPager);

    void setPageSize(int i);

    void setPageScrolled(int position, float positionOffset);

}
