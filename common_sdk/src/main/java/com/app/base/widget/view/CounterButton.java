package com.app.base.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.android.base.rx.RxKit;
import com.app.base.R;

import java.util.concurrent.TimeUnit;

import androidx.appcompat.widget.AppCompatTextView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Counter Button
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-04-27 11:33
 */
public class CounterButton extends AppCompatTextView {

    private CharSequence mOriginText;
    private int mCount;
    private String mFormatText;
    private boolean mIsCounting;

    private static final int DEF_COUNTER = 60;
    private static final int RECOVER_COUNTER_COUNT = 5;
    private int mStopCounting;
    private long mStopCountingTime;

    private boolean mDelayEnable;

    private int mNormalTextColor;
    private int mCountingTextColor;
    private int mDisableTextColor;

    private CompositeDisposable mCompositeSubscription;

    public CounterButton(Context context) {
        this(context, null);
    }

    public CounterButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CounterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CounterButton);
        mOriginText = typedArray.getString(R.styleable.CounterButton_ccb_text);

        if (TextUtils.isEmpty(mOriginText)) {
            mOriginText = getText();
        } else {
            setText(mOriginText);
        }

        mCount = typedArray.getInt(R.styleable.CounterButton_ccb_count, DEF_COUNTER);
        mFormatText = typedArray.getString(R.styleable.CounterButton_ccb_text_hint_format);

        mNormalTextColor = typedArray.getColor(R.styleable.CounterButton_ccb_text_normal_color, Color.BLACK);
        mCountingTextColor = typedArray.getColor(R.styleable.CounterButton_ccb_text_counting_color, Color.GRAY);
        mDisableTextColor = typedArray.getColor(R.styleable.CounterButton_ccb_text_disable_color, Color.GRAY);

        typedArray.recycle();

        setTextColor(mNormalTextColor);
        mOriginText = getText();
        mDelayEnable = isEnabled();
    }

    public void startCounter() {
        startCounter(mCount);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIsCounting) {
            int timeGone = (int) ((System.currentTimeMillis() - mStopCountingTime) / 1000F);
            int currentCount = mCount - mStopCounting + timeGone;
            int remainCount = mCount - currentCount;
            if (remainCount > RECOVER_COUNTER_COUNT) {
                startCounter(remainCount);
            } else {
                reset();
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        RxKit.disposeChecked(mCompositeSubscription);
        mStopCountingTime = System.currentTimeMillis();
    }

    private void startCounter(int count) {
        mIsCounting = true;
        super.setEnabled(false);
        setTextColor(mCountingTextColor);

        Disposable disposable = createCounter(count)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> postDelayed(this::reset, 1000))
                .subscribe(aLong -> {
                    setCounterText(String.valueOf(aLong));
                    mStopCounting = aLong.intValue();
                }, RxKit.logErrorHandler());

        mCompositeSubscription = RxKit.newCompositeIfUnsubscribed(mCompositeSubscription);
        mCompositeSubscription.add(disposable);
    }

    private void reset() {
        RxKit.disposeChecked(mCompositeSubscription);
        mIsCounting = false;
        setEnabled(mDelayEnable);
        setText(mOriginText);
        setTextColor(mNormalTextColor);
    }

    private void setCounterText(String text) {
        String ret = text;
        if (mFormatText != null) {
            if (mFormatText.contains("%s")) {
                ret = String.format(mFormatText, text);
            } else {
                ret = mFormatText + text;
            }
        }
        setText(ret);
    }

    public static Observable<Long> createCounter(final int count) {
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count)
                .map(aLong -> count - aLong);
    }

    public void clearCounter() {
        reset();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (!mIsCounting) {
            super.setEnabled(enabled);
            if (enabled) {
                setTextColor(mNormalTextColor);
            } else {
                setTextColor(mDisableTextColor);
            }
        }
        mDelayEnable = enabled;
    }

}