package com.vclusters.cloud.account.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.vclusters.cloud.account.R;

/**
 * 带两个 Icon 的 Edit。【该 EditText 仅适用于 Account 的登录界面，不具备通用性】
 */
public class IconsEditText extends AppCompatEditText {

    private IconsEditTextController mIconsEditTextController;
    private IconsEditTextListener mIconsEditTextListener;

    public IconsEditText(Context context) {
        super(context);
        init(context, null);
    }

    public IconsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconsEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        IconsAttrs iconsAttrs = new IconsAttrs();
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconsEditText);

            BitmapDrawable clearDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.IconsEditText_iet_clear_drawable);
            if (clearDrawable != null) {
                iconsAttrs.setClearBitmap(clearDrawable.getBitmap());
            }

            BitmapDrawable passwordVisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.IconsEditText_iet_tailing_on_drawable);
            if (passwordVisibleDrawable != null) {
                iconsAttrs.setTailingOnBitmap(passwordVisibleDrawable.getBitmap());
            }

            BitmapDrawable passwordInvisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.IconsEditText_iet_tailing_off_drawable);
            if (passwordInvisibleDrawable != null) {
                iconsAttrs.setTailingOffBitmap(passwordInvisibleDrawable.getBitmap());
            }

            iconsAttrs.setTailingIconEnable(typedArray.getBoolean(R.styleable.IconsEditText_iet_enable_tailing_icon, false));
            iconsAttrs.setContentClearableEnable(typedArray.getBoolean(R.styleable.IconsEditText_iet_enable_content_clearable, true));

        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        mIconsEditTextController = new IconsEditTextController(this, iconsAttrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mIconsEditTextController.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mIconsEditTextController.draw(canvas);
    }

    public void setInitPaddingRight(int initPaddingRight) {
        mIconsEditTextController.setInitPaddingRight(initPaddingRight);
    }

    public void setTailingIconState(boolean state) {
        mIconsEditTextController.setTailingIconState(state);
    }

    public interface IconsEditTextListener {
        void onIconClicked(IconsEditText iconsEditText, boolean pendingState);
    }

    IconsEditTextListener getIconsEditTextListener() {
        return mIconsEditTextListener;
    }

    public void setIconsEditTextListener(IconsEditTextListener iconsEditTextListener) {
        mIconsEditTextListener = iconsEditTextListener;
    }

}