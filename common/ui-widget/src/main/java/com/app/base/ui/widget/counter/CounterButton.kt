package com.app.base.ui.widget.counter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.AttrRes
import com.android.base.ui.drawable.DTextView
import com.android.base.utils.BaseUtils
import com.android.base.utils.android.storage.SpCache
import com.app.base.ui.widget.R

private val counterStorage = SpCache(BaseUtils.getAppContext(), "counter-button")

/**
 * Counter Button.
 *
 * @author Ztiany
 */
class CounterButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.textViewStyle,
) : DTextView(context, attrs, defStyleAttr) {

    private var originText: CharSequence?
    private val formatText: String?

    private val totalCountDown: Int

    private var isCounting = false
    private var remainingCount = 0
    private var stopCountingTime: Long = 0

    private val counterHandler = Handler(Looper.getMainLooper())

    private val counterTag: String
    private val stopCountingTag: String
        get() = "${counterTag}-stopCounting"

    private var clearWhenDetach = false

    init {
        isClickable = true

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CounterButton)
        originText = typedArray.getString(R.styleable.CounterButton_ccb_text)
        if (TextUtils.isEmpty(originText)) {
            originText = text
        } else {
            text = originText
        }
        totalCountDown = typedArray.getInt(R.styleable.CounterButton_ccb_count, DEF_COUNTER)
        formatText = typedArray.getString(R.styleable.CounterButton_ccb_text_hint_format)
        counterTag = typedArray.getString(R.styleable.CounterButton_ccb_counter_tag) ?: ""
        typedArray.recycle()

        if (counterTag.isNotEmpty()) {
            remainingCount = counterStorage.getInt(stopCountingTag, 0)
            isCounting = remainingCount > 0
        }

        originText = text
    }

    fun startCounter() {
        startCounter(totalCountDown)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isCounting) {
            val timeGone = ((System.currentTimeMillis() - getStopCountingTime()) / 1000f).toInt()
            val realRemainingCount = remainingCount - timeGone
            if (realRemainingCount > RECOVER_COUNTER_COUNT) {
                startCounter(realRemainingCount)
            } else {
                reset()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        counterHandler.removeCallbacksAndMessages(null)
        saveStopCountingTime()
    }

    private fun saveStopCountingTime() {
        if (clearWhenDetach) {
            return
        }
        if (!isCounting) {
            return
        }
        stopCountingTime = System.currentTimeMillis()
        if (counterTag.isNotEmpty()) {
            counterStorage.putLong(counterTag, stopCountingTime)
            counterStorage.putInt(stopCountingTag, remainingCount)
        }
    }

    private fun getStopCountingTime(): Long {
        if (counterTag.isNotEmpty()) {
            stopCountingTime = counterStorage.getLong(counterTag, 0)
        }
        return stopCountingTime
    }

    private fun startCounter(count: Int) {
        isCounting = true
        createCounter(count, onCounting = {
            setCounterText(it.toString())
            remainingCount = it
        }, onFinished = {
            reset()
        })
    }

    private fun createCounter(count: Int, onCounting: (Int) -> Unit, onFinished: () -> Unit) {
        var counting = count
        onCounting(counting)

        counterHandler.postDelayed(object : Runnable {
            override fun run() {
                counting--
                if (counting <= 0) {
                    counterHandler.post { onFinished() }
                } else {
                    onCounting(counting)
                    counterHandler.postDelayed(this, 1000)
                }
            }
        }, 1000)
    }

    private fun reset() {
        counterHandler.removeCallbacksAndMessages(null)
        isCounting = false
        text = originText
        stopCountingTime = 0

        if (counterTag.isNotEmpty()) {
            counterStorage.putLong(counterTag, 0)
            counterStorage.putInt(stopCountingTag, 0)
        }
    }

    private fun setCounterText(text: String) {
        var ret = text
        if (formatText != null) {
            ret = if (formatText.contains("%s")) {
                String.format(formatText, text)
            } else {
                formatText + text
            }
        }
        setText(ret)
    }

    fun clearCounter() {
        reset()
    }

    fun clearCounterStateWhenDetach() {
        clearWhenDetach = true
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isCounting) {
            return false
        }
        return super.onTouchEvent(event)
    }

    companion object {
        private const val DEF_COUNTER = 60
        private const val RECOVER_COUNTER_COUNT = 5
    }

}