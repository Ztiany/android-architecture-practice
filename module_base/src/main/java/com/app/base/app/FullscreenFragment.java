package com.app.base.app;

import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.android.base.app.fragment.BaseFragment;

/**
 * 保证显示这个Fragment的时候是全屏的
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2018-11-02 14:28
 */
public class FullscreenFragment extends BaseFragment {

    private int mBackupFlags;

    @Override
    public void onResume() {
        super.onResume();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            mBackupFlags = window.getAttributes().flags;
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.flags = mBackupFlags;
            window.setAttributes(attributes);
        }
    }

}
