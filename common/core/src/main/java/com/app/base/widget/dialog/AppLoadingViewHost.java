package com.app.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.android.base.fragment.ui.LoadingViewHost;
import com.app.base.R;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;

public class AppLoadingViewHost implements LoadingViewHost {

    private final Context mContext;
    private LoadingDialog mLoadingDialog;

    public AppLoadingViewHost(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Dialog showLoadingDialog(@NotNull CharSequence message, boolean cancelable) {
        initDialog();
        if (TextUtils.isEmpty(message)) {
            mLoadingDialog.setMessage(R.string.dialog_loading);
        } else {
            mLoadingDialog.setMessage(message);
        }
        mLoadingDialog.setCancelable(cancelable);
        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
        return mLoadingDialog;
    }

    @NonNull
    @Override
    public Dialog showLoadingDialog(@StringRes int messageId, boolean cancelable) {
        return showLoadingDialog(mContext.getText(messageId), cancelable);
    }

    @NonNull
    @Override
    public Dialog showLoadingDialog() {
        return showLoadingDialog(true);
    }

    @NonNull
    @Override
    public Dialog showLoadingDialog(boolean cancelable) {
        return showLoadingDialog("", cancelable);
    }

    @Override
    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Override
    public void showMessage(@NotNull CharSequence message) {
        ToastKit.showMessage(mContext, message);
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