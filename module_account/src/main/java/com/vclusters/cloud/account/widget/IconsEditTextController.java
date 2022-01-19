package com.vclusters.cloud.account.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;

import com.android.sdk.ui.R;

import org.jetbrains.annotations.NotNull;

import me.ztiany.widget.common.Sizes;
import timber.log.Timber;

/**
 * @author Ztiany
 */
public class IconsEditTextController {

    private IconsAttrs mIconsAttrs;
    private boolean mTailingState = false;
    private Paint mBitmapPaint;

    /**
     * the edge offset of first bitmap
     */
    private int mBitmapRightEdgeOffset;

    /**
     * the margin between clearing bitmap and password bitmap
     */
    private int mBitmapMargin;

    private int mInitPaddingRight;

    private static final int DOWN_POSITION_NONE = 1;
    private static final int DOWN_POSITION_CLEAR = 2;
    private static final int DOWN_POSITION_TAILING = 3;
    private int mDownPosition = DOWN_POSITION_NONE;

    private final IconsEditText mEditText;

    @SuppressWarnings("WeakerAccess")
    public IconsEditTextController(IconsEditText editText, IconsAttrs iconsAttrs) {
        mEditText = editText;
        getAttrs(iconsAttrs);
        init();
    }

    private void getAttrs(IconsAttrs iconsAttrs) {
        mIconsAttrs = iconsAttrs;
        if (iconsAttrs.getClearBitmap() == null) {
            iconsAttrs.setClearBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.base_ui_icon_clear));
        }
    }

    private void init() {
        mInitPaddingRight = mEditText.getPaddingRight();
        mBitmapRightEdgeOffset = Sizes.dpToPx(getContext(), 5);
        mBitmapMargin = Sizes.dpToPx(getContext(), 15);
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        adjustPadding();
        mEditText.addTextChangedListener(newWatcher());
    }

    private Context getContext() {
        return mEditText.getContext();
    }

    private Bitmap getTailingOnBitmap() {
        if (mIconsAttrs.getTailingOnBitmap() == null) {
            mIconsAttrs.setTailingOnBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.base_ui_icon_password_open));
        }
        return mIconsAttrs.getTailingOnBitmap();
    }

    private Bitmap getTailingOffBitmap() {
        if (mIconsAttrs.getTailingOffBitmap() == null) {
            mIconsAttrs.setTailingOffBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.base_ui_icon_password_close));
        }
        return mIconsAttrs.getTailingOffBitmap();
    }

    private Bitmap getPasswordBitmap() {
        if (mTailingState) {
            return getTailingOnBitmap();
        } else {
            return getTailingOffBitmap();
        }
    }

    private void adjustPadding() {
        boolean hasClearBitmap = mIconsAttrs.isContentClearableEnable() && !TextUtils.isEmpty(getTextValue());

        int rightPadding;
        if (mIconsAttrs.isTailingIconEnable()) {
            rightPadding = mInitPaddingRight + getPasswordBitmap().getWidth() + mBitmapRightEdgeOffset;
            if (hasClearBitmap) {
                rightPadding += (mBitmapMargin + mIconsAttrs.getClearBitmap().getWidth());
            }
        } else if (hasClearBitmap) {
            rightPadding = mInitPaddingRight + mIconsAttrs.getClearBitmap().getWidth() + mBitmapRightEdgeOffset;
        } else {
            rightPadding = mInitPaddingRight;
        }

        mEditText.invalidate();
        mEditText.setPadding(mEditText.getPaddingLeft(), mEditText.getPaddingTop(), rightPadding, mEditText.getPaddingBottom());
    }

    @NotNull
    private TextWatcher newWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adjustPadding();
            }
        };
    }


    public void onTouchEvent(MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mDownPosition = detectTouchPosition(event);
        } else if (action == MotionEvent.ACTION_UP) {
            int upPosition = detectTouchPosition(event);
            Timber.d("upPosition = %d", upPosition);
            if (upPosition == mDownPosition) {
                if ((upPosition == DOWN_POSITION_CLEAR)) {
                    mEditText.setText("");
                } else if (upPosition == DOWN_POSITION_TAILING) {
                    IconsEditText.IconsEditTextListener iconsEditTextListener = mEditText.getIconsEditTextListener();
                    if (iconsEditTextListener != null) {
                        iconsEditTextListener.onIconClicked(mEditText, !mTailingState);
                    }
                }
            }
        }
    }

    private int detectTouchPosition(MotionEvent event) {
        float eventX = event.getX();

        if (mIconsAttrs.isTailingIconEnable()) {

            int passwordRight = mEditText.getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
            int passwordLeft = passwordRight - getPasswordBitmap().getWidth();
            if (eventX >= passwordLeft && eventX <= passwordRight) {
                return DOWN_POSITION_TAILING;
            }

            if (mIconsAttrs.isContentClearableEnable() && !TextUtils.isEmpty(getTextValue())) {
                int clearRight = passwordLeft - mBitmapMargin;
                int clearLeft = clearRight - mIconsAttrs.getClearBitmap().getWidth();
                if (eventX >= clearLeft && eventX <= clearRight) {
                    return DOWN_POSITION_CLEAR;
                }
            }

        } else if (mIconsAttrs.isContentClearableEnable() && !TextUtils.isEmpty(getTextValue())) {

            int clearRight = mEditText.getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
            int clearLeft = clearRight - mIconsAttrs.getClearBitmap().getWidth();
            if (eventX >= clearLeft && eventX <= clearRight) {
                return DOWN_POSITION_CLEAR;
            }
        }

        return DOWN_POSITION_NONE;
    }

    private String getTextValue() {
        Editable text = mEditText.getText();
        return (text == null) ? "" : text.toString();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.translate(mEditText.getMeasuredWidth() - mInitPaddingRight, 0);

        if (mIconsAttrs.isTailingIconEnable()) {
            Bitmap passwordBitmap = getPasswordBitmap();
            canvas.translate(-(passwordBitmap.getWidth() + mBitmapRightEdgeOffset), 0);
            canvas.drawBitmap(passwordBitmap, 0, (int) ((mEditText.getMeasuredHeight() - passwordBitmap.getHeight()) / 2.0F), mBitmapPaint);
        }

        boolean hasClearBitmap = mIconsAttrs.isContentClearableEnable() && !TextUtils.isEmpty(getTextValue());

        if (hasClearBitmap) {
            if (mIconsAttrs.isTailingIconEnable()) {
                canvas.translate(-(mIconsAttrs.getClearBitmap().getWidth() + mBitmapMargin), 0);
            } else {
                canvas.translate(-(mIconsAttrs.getClearBitmap().getWidth() + mBitmapRightEdgeOffset), 0);
            }
            canvas.drawBitmap(mIconsAttrs.getClearBitmap(), 0, (int) ((mEditText.getMeasuredHeight() - mIconsAttrs.getClearBitmap().getHeight()) / 2.0F), mBitmapPaint);
        }

        canvas.restore();
    }

    @SuppressWarnings("WeakerAccess")
    public void setInitPaddingRight(int initPaddingRight) {
        mInitPaddingRight = initPaddingRight;
        adjustPadding();
    }

    public void setTailingIconState(boolean state) {
        mTailingState = state;
        mEditText.invalidate();
    }

}