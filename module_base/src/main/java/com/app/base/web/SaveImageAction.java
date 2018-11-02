package com.app.base.web;

import android.webkit.URLUtil;

import com.android.base.utils.common.Checker;
import com.app.base.config.DirectoryManager;

import java.io.File;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-01-24 14:48
 */
class SaveImageAction implements Runnable {

    private final BaseWebFragment mFragment;
    private final String[] mArgs;

    SaveImageAction(BaseWebFragment fragment, String[] args) {
        mFragment = fragment;
        mArgs = args;
    }

    @Override
    public void run() {

        if (Checker.isEmpty(mArgs)) {
            return;
        }

        String imageUrl = mArgs[0];

        if (!URLUtil.isNetworkUrl(imageUrl)) {
            return;
        }

        /*OKHttpDownloader.download(imageUrl, getSaveFile(imageUrl))
                .compose(mFragment.bindToLifecycle())
                .subscribe(
                        this::showImageSaveSuccess,
                        throwable -> mFragment.showMessage(R.string.image_save_fail_tips));*/
    }

    private void showImageSaveSuccess(@SuppressWarnings("unused") String path) {
        //mFragment.showMessage(mFragment.getString(R.string.image_save_success_mask_tips));
    }

    private File getSaveFile(String url) {
        int index = url.lastIndexOf("/");
        String fileName = url.substring(index + 1, url.length());
        String path = DirectoryManager.createDCIMPictureStorePath(fileName);
        return new File(path);
    }

}
