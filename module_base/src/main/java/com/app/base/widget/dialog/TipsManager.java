package com.app.base.widget.dialog;

import android.view.View;

import com.android.base.utils.android.ResourceUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.StringRes;


public class TipsManager {

    public static void showMessage(@StringRes int msgId) {
        showMessage(ResourceUtils.getText(msgId));
    }

    public static void showMessage(CharSequence message) {
        ToastUtils.showShort(message);
    }

    public static void showMessage(View anchor, CharSequence message) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showMessage(View anchor, CharSequence message, CharSequence actionText, View.OnClickListener onClickListener) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT)
                .setAction(actionText, onClickListener)
                .show();
    }

}
