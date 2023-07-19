package com.app.base.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;

import com.app.base.R;

import kotlin.Unit;


/**
 * @author Ztiany
 */
public class LoadingDialog extends AppCompatDialog implements LoadingDialogInterface {

    private TextView mMessageTv;

    public LoadingDialog(Context context) {
        super(context);
        setView();
    }

    private void setView() {
        setContentView(R.layout.dialog_loading);
        mMessageTv = findViewById(R.id.dialog_loading_tv_title);
    }

    public void setMessage(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            mMessageTv.setText(message);
        }
    }

    public void setMessage(@StringRes int messageId) {
        if (messageId != 0) {
            mMessageTv.setText(messageId);
        }
    }

    @Override
    public void show() {
        DialogEx.showCompat(this, () -> {
            LoadingDialog.super.show();
            return Unit.INSTANCE;
        });
    }

    @NonNull
    @Override
    public Dialog getDialog() {
        return this;
    }

    @Override
    public void updateMessage(@NonNull CharSequence message) {
        setMessage(message);
    }

}
