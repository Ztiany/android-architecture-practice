package com.app.base.widget.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.text.TextUtils;

import com.android.base.app.ui.LoadingView;
import com.app.base.R;

public class AppLoadingView implements LoadingView {

    private Context mContext;
    private LoadingDialog mLoadingDialog;

    public AppLoadingView(Context context) {
        mContext = context;
    }

    @Override
    public void showLoadingDialog(CharSequence message, boolean cancelable) {
        initDialog();
        if (TextUtils.isEmpty(message)) {
            mLoadingDialog.setMessage(R.string.dialog_loading);
        } else {
            mLoadingDialog.setMessage(message);
        }
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.show();
    }

    @Override
    public void showLoadingDialog(@StringRes int messageId, boolean cancelable) {
        showLoadingDialog(mContext.getText(messageId), cancelable);
    }

    @Override
    public void showLoadingDialog(boolean cancelable) {
        showLoadingDialog(null, cancelable);
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(CharSequence message) {
        TipsManager.showMessage(message);
    }

    @Override
    public void showMessage(@StringRes int messageId) {
        showMessage(mContext.getText(messageId));
    }

    private void initDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = (LoadingDialog) DialogManager.createLoadingDialog(mContext, false);
        }
    }

}