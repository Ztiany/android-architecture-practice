package com.app.base.widget.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.app.base.R;


/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-05-02 14:37
 */
class LoadingDialog extends AppCompatDialog {

    private TextView mMessageTv;

    LoadingDialog(Context context, boolean horizontal) {
        super(context);
        setView(horizontal);
    }

    private void setView(boolean horizontal) {
        if (horizontal) {
            setContentView(R.layout.dialog_loading_horizontal);
        } else {
            setContentView(R.layout.dialog_loading);
        }
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
}
