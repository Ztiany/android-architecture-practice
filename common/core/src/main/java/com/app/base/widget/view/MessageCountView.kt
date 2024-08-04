package com.app.base.widget.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.beVisible

class MessageCountView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    init {
        gravity = Gravity.CENTER
        textSize = 8F
        setTextColor(Color.WHITE)
    }

    fun setMessageCount(count: Int) {
        when {
            count > 99 -> {
                text = ""
                beVisible()
                setBackgroundResource(com.app.base.ui.theme.R.drawable.icon_message_overflow)
            }
            count > 9 -> {
                text = count.toString()
                beVisible()
                setBackgroundResource(com.app.base.ui.theme.R.drawable.icon_message_more)
            }
            count > 0 -> {
                beVisible()
                text = count.toString()
                setBackgroundResource(com.app.base.ui.theme.R.drawable.icon_message_count)
            }
            else -> {
                beGone()
            }
        }
    }

}