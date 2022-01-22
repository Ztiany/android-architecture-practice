package com.app.base.widget.dialog;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.android.base.architecture.ui.LoadingView;
import com.app.base.R;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class AppLoadingView implements LoadingView {

    private final Context mContext;
    private LoadingDialog mLoadingDialog;

    public AppLoadingView(Context context) {
        mContext = context;
    }

    @Override
    public void showLoadingDialog(@NotNull CharSequence message, boolean cancelable) {
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
    public void showLoadingDialog() {
        showLoadingDialog(true);
    }

    @Override
    public void showLoadingDialog(boolean cancelable) {
        showLoadingDialog("", cancelable);
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(@NotNull CharSequence message) {
        TipsTool.showMessage(mContext, message);
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

    @Override
    public void dismissLoadingDialog(long minimumMills, @Nullable Function0<Unit> onDismiss) {
        throw new UnsupportedOperationException("the method should be implemented by implementer of LoadingView");
    }

    @Override
    public boolean isLoadingDialogShowing() {
        return mLoadingDialog != null && mLoadingDialog.isShowing();
    }

}