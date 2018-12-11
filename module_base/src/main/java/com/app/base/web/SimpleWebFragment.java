package com.app.base.web;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.base.app.fragment.tools.FragmentUtils;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-03-05 17:20
 */
public class SimpleWebFragment extends BaseWebFragment {

    private static final String FIRST_URL_KEY = "first_url_key";

    private boolean mAutoHandleTitle = true;
    private boolean mOnNavigationClickDismiss;

    public static SimpleWebFragment newInstance(String firstUrl) {
        SimpleWebFragment baseWebFragment = new SimpleWebFragment();
        Bundle args = new Bundle();
        args.putString(FIRST_URL_KEY, firstUrl);
        baseWebFragment.setArguments(args);
        return baseWebFragment;
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        mToolbar.setNavigationOnClickListener(v -> {
            if (mOnNavigationClickDismiss) {
                dismiss();
            } else {
                FragmentUtils.fragmentBack(getActivity(), false);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            String url = getArguments().getString(FIRST_URL_KEY);
            if (!TextUtils.isEmpty(url)) {
                startLoad(url);
            }
        }
    }

    public void setAutoHandleTitle(boolean autoHandleTitle) {
        mAutoHandleTitle = autoHandleTitle;
    }

    @Override
    protected boolean autoHandleTitle() {
        return mAutoHandleTitle;
    }

    public void setOnNavigationClickDismiss() {
        mOnNavigationClickDismiss = true;
    }

}
