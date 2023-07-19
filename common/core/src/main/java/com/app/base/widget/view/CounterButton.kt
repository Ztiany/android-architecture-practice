package com.app.base.widget.view

import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.android.base.utils.android.storage.SpCache
import com.app.base.R

/** TODO：preload the sp. */
private val counterStorage = SpCache("AppCounterStorage", true)

/**
 * Counter Button.
 *
 * @author Ztiany
 */
class CounterButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var originText: CharSequence?
    private val formatText: String?

    private val totalCountDown: Int

    private var isCounting = false
    private var remainingCount = 0
    private var stopCountingTime: Long = 0
    private var delayEnable: Boolean

    private val normalTextColor: Int
    private val countingTextColor: Int
    private val disableTextColor: Int

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
        normalTextColor = typedArray.getColor(R.styleable.CounterButton_ccb_text_normal_color, Color.BLACK)
        countingTextColor = typedArray.getColor(R.styleable.CounterButton_ccb_text_counting_color, normalTextColor)
        disableTextColor = typedArray.getColor(R.styleable.CounterButton_ccb_text_disable_color, normalTextColor)
        typedArray.recycle()

        if (counterTag.isNotEmpty()) {
            remainingCount = counterStorage.getInt(stopCountingTag, 0)
            isCounting = remainingCount > 0
        }

        setTextColor(normalTextColor)
        originText = text
        delayEnable = isEnabled
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
        super.setEnabled(false)
        setTextColor(countingTextColor)

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
        isEnabled = delayEnable
        text = originText
        stopCountingTime = 0

        if (counterTag.isNotEmpty()) {
            counterStorage.putLong(counterTag, 0)
            counterStorage.putInt(stopCountingTag, 0)
        }
        setTextColor(normalTextColor)
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

    fun clearWhenDetach() {
        clearWhenDetach = true
    }

    override fun setEnabled(enabled: Boolean) {
        if (!isCounting) {
            super.setEnabled(enabled)
            if (enabled) {
                setTextColor(normalTextColor)
            } else {
                setTextColor(disableTextColor)
            }
        }
        delayEnable = enabled
    }

    companion object {
        private const val DEF_COUNTER = 60
        private const val RECOVER_COUNTER_COUNT = 5
    }

}