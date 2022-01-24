package com.vclusters.cloud.account.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

import com.vclusters.cloud.account.R;

import org.jetbrains.annotations.NotNull;

import me.ztiany.widget.common.Sizes;
import timber.log.Timber;

/**
 * 带两个 Icon 的 EditText，一个用于清理内容，一个用于自定义。【该 EditText 仅适用于 Account 的登录界面，不具备通用性】
 */
public class IconsEditText extends AppCompatEditText {

    public interface OnTailingIconClickListener {
        void onIconClicked(IconsEditText iconsEditText, boolean pendingState);
    }

    private Bitmap clearBitmap;
    private Bitmap tailingOnBitmap;
    private Bitmap tailingOffBitmap;
    private boolean isTailingIconEnable;
    private boolean isClearContentEnable;
    private boolean mIsTailingIconOn;

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

    private int extendDrawableTouchingSize = 0;

    private static final int DOWN_POSITION_NONE = 1;
    private static final int DOWN_POSITION_CLEAR = 2;
    private static final int DOWN_POSITION_TAILING = 3;
    private int mDownPosition = DOWN_POSITION_NONE;

    private OnTailingIconClickListener mOnTailingIconClickListener;

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
        parseAttributes(context, attrs);
        //calculate padding/margin
        mInitPaddingRight = getPaddingRight();
        mBitmapRightEdgeOffset = Sizes.dpToPx(getContext(), 5);
        mBitmapMargin = Sizes.dpToPx(getContext(), 15);
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        adjustPadding();
        addTextChangedListener(newWatcher());
        extendDrawableTouchingSize = Sizes.dpToPx(getContext(), 10);
    }

    private void parseAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.IconsEditText);
            BitmapDrawable clearDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.IconsEditText_iet_clear_drawable);
            if (clearDrawable != null) {
                clearBitmap = clearDrawable.getBitmap();
            }
            if (clearBitmap == null) {
                clearBitmap = BitmapFactory.decodeResource(getContext().getResources(), com.android.sdk.ui.R.drawable.base_ui_icon_clear);
            }

            BitmapDrawable passwordVisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.IconsEditText_iet_tailing_on_drawable);
            if (passwordVisibleDrawable != null) {
                tailingOnBitmap = passwordVisibleDrawable.getBitmap();
            }

            BitmapDrawable passwordInvisibleDrawable = (BitmapDrawable) typedArray.getDrawable(R.styleable.IconsEditText_iet_tailing_off_drawable);
            if (passwordInvisibleDrawable != null) {
                tailingOffBitmap = passwordInvisibleDrawable.getBitmap();
            }

            isTailingIconEnable = typedArray.getBoolean(R.styleable.IconsEditText_iet_enable_tailing_icon, false);
            isClearContentEnable = typedArray.getBoolean(R.styleable.IconsEditText_iet_enable_content_clearable, true);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    public void setOnTailingIconClickListener(OnTailingIconClickListener onTailingIconClickListener) {
        mOnTailingIconClickListener = onTailingIconClickListener;
    }

    private Bitmap getTailingOnBitmap() {
        if (tailingOnBitmap == null) {
            tailingOnBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_arrow_up);
        }
        return tailingOnBitmap;
    }

    private Bitmap getTailingOffBitmap() {
        if (tailingOffBitmap == null) {
            tailingOffBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon_arrow_down);
        }
        return tailingOffBitmap;
    }

    private Bitmap getPasswordBitmap() {
        if (mIsTailingIconOn) {
            return getTailingOnBitmap();
        } else {
            return getTailingOffBitmap();
        }
    }

    private void adjustPadding() {
        boolean hasClearBitmap = isClearContentEnable && !TextUtils.isEmpty(getTextValue());
        int rightPadding;

        if (isTailingIconEnable) {
            rightPadding = mInitPaddingRight + getPasswordBitmap().getWidth() + mBitmapRightEdgeOffset;
            if (hasClearBitmap) {
                rightPadding += (mBitmapMargin + clearBitmap.getWidth());
            }
        } else if (hasClearBitmap) {
            rightPadding = mInitPaddingRight + clearBitmap.getWidth() + mBitmapRightEdgeOffset;
        } else {
            rightPadding = mInitPaddingRight;
        }

        invalidate();
        setPadding(getPaddingLeft(), getPaddingTop(), rightPadding, getPaddingBottom());
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (handleTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        doDraw(canvas);
    }

    public boolean handleTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            mDownPosition = detectTouchPosition(event);
        } else if (action == MotionEvent.ACTION_UP) {
            int upPosition = detectTouchPosition(event);
            Timber.d("upPosition = %d", upPosition);
            if (upPosition == mDownPosition) {
                if ((upPosition == DOWN_POSITION_CLEAR)) {
                    setText("");
                } else if (upPosition == DOWN_POSITION_TAILING) {
                    if (mOnTailingIconClickListener != null) {
                        mOnTailingIconClickListener.onIconClicked(this, !mIsTailingIconOn/*the next status to be.*/);
                    }
                }
            }
        }

        return mDownPosition != DOWN_POSITION_NONE;
    }

    private int detectTouchPosition(MotionEvent event) {
        float eventX = event.getX();

        if (isTailingIconEnable) {

            int passwordRight = getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
            int passwordLeft = passwordRight - getPasswordBitmap().getWidth();
            if (eventX >= passwordLeft - extendDrawableTouchingSize && eventX <= passwordRight + extendDrawableTouchingSize) {
                return DOWN_POSITION_TAILING;
            }

            if (isClearContentEnable && !TextUtils.isEmpty(getTextValue())) {
                int clearRight = passwordLeft - mBitmapMargin;
                int clearLeft = clearRight - clearBitmap.getWidth();
                if (eventX >= clearLeft - extendDrawableTouchingSize && eventX <= clearRight + extendDrawableTouchingSize) {
                    return DOWN_POSITION_CLEAR;
                }
            }

        } else if (isClearContentEnable && !TextUtils.isEmpty(getTextValue())) {
            int clearRight = getMeasuredWidth() - mInitPaddingRight - mBitmapRightEdgeOffset;
            int clearLeft = clearRight - clearBitmap.getWidth();
            if (eventX >= clearLeft - extendDrawableTouchingSize && eventX <= clearRight + extendDrawableTouchingSize) {
                return DOWN_POSITION_CLEAR;
            }
        }

        return DOWN_POSITION_NONE;
    }

    private String getTextValue() {
        Editable text = getText();
        return (text == null) ? "" : text.toString();
    }

    private void doDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getMeasuredWidth() - mInitPaddingRight, 0);

        if (isTailingIconEnable) {
            Bitmap passwordBitmap = getPasswordBitmap();
            canvas.translate(-(passwordBitmap.getWidth() + mBitmapRightEdgeOffset), 0);
            canvas.drawBitmap(passwordBitmap, 0, (int) ((getMeasuredHeight() - passwordBitmap.getHeight()) / 2.0F), mBitmapPaint);
        }

        boolean hasClearBitmap = isClearContentEnable && !TextUtils.isEmpty(getTextValue());

        if (hasClearBitmap) {
            if (isTailingIconEnable) {
                canvas.translate(-(clearBitmap.getWidth() + mBitmapMargin), 0);
            } else {
                canvas.translate(-(clearBitmap.getWidth() + mBitmapRightEdgeOffset), 0);
            }
            canvas.drawBitmap(clearBitmap, 0, (int) ((getMeasuredHeight() - clearBitmap.getHeight()) / 2.0F), mBitmapPaint);
        }

        canvas.restore();
    }

    public void setInitPaddingRight(int initPaddingRight) {
        mInitPaddingRight = initPaddingRight;
        adjustPadding();
    }

    public void setTailingIconStateOn(boolean state) {
        mIsTailingIconOn = state;
        invalidate();
    }

    public void setTailingIconEnable(boolean enable) {
        isTailingIconEnable = enable;
        adjustPadding();
    }

    public boolean isTailingIconStateOn() {
        return mIsTailingIconOn;
    }
}