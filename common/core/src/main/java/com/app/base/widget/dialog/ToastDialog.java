package com.app.base.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.content.ContextCompat;

import com.app.base.R;

import kotlin.Unit;


/**
 * @author Ztiany
 */
class ToastDialog extends AppCompatDialog {

    private TextView mMessageTv;

    ToastDialog(Context context) {
        super(context, R.style.AppTheme_Dialog_Tips);
        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.windowAnimations = R.style.AppAnimation_FadeIn;
        setView();
    }

    private void setView() {
        setContentView(R.layout.dialog_tips);
        mMessageTv = findViewById(R.id.dialogTipsTvMessage);
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

    public void setTipsType(@TipsType int type) {
        switch (type) {
            case ToastDialogBuilder.TYPE_SUCCESS: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tips_success, 0, 0);
                mMessageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            }
            break;
            case ToastDialogBuilder.TYPE_FAILURE:
            case ToastDialogBuilder.TYPE_WARNING: {
                mMessageTv.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_tips_failed, 0, 0);
                mMessageTv.setTextColor(ContextCompat.getColor(getContext(), R.color.text_stress));
            }
            break;
        }
    }

    @Override
    public void show() {
        DialogEx.showCompat(this, () -> {
            ToastDialog.super.show();
            return Unit.INSTANCE;
        });
    }

}