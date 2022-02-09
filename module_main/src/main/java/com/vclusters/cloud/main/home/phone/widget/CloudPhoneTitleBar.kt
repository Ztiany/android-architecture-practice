package com.vclusters.cloud.main.home.phone.widget

import android.content.Context
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.use
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.measureSelf
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.databinding.MainLayoutWidgetPhoneTitleBarBinding
import org.jetbrains.annotations.NotNull

typealias OnTabSelectedListener = (position: Int) -> Unit

class CloudPhoneTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val vb = MainLayoutWidgetPhoneTitleBarBinding.inflate(LayoutInflater.from(getContext()), this)

    private var selectedIndex = 0

    private val tabMargin = dip(20)
    private var tabWidth = 0

    private val tabs = mutableListOf<TextView>()

    var onTabSelectedListener: OnTabSelectedListener? = null
        set(value) {
            field = value
            field?.invoke(selectedIndex)
        }

    var onMessageClickListener: OnClickListener? = null

    init {
        context.obtainStyledAttributes(attrs, R.styleable.CloudPhoneTitleBar).use {
            val tabs = it.getTextArray(R.styleable.CloudPhoneTitleBar_tabs) ?: emptyArray()
            initTables(tabs)
        }

        tabs.forEachIndexed { index, textView ->
            textView.setOnClickListener {
                selectedIndex = index
                onSelectedTabChanged()
            }
        }

        initMessageIcon()
    }

    private fun initMessageIcon() {
        vb.mainIvMessage.setOnClickListener {
            onMessageClickListener?.onClick(it)
        }
    }

    private fun initTables(tabTexts: Array<CharSequence>) {
        val from = LayoutInflater.from(context)
        tabTexts.forEachIndexed { index, item ->
            addTab(from, item, index)
        }
    }

    private fun addTab(from: LayoutInflater, item: CharSequence, index: Int) {
        val textView = from.inflate(R.layout.main_layout_phone_tab, vb.mainFlTableContainer, false) as TextView
        tabs.add(textView)
        textView.text = item
        if (selectedIndex == index) selectTab(textView) else unselectTab(textView)
        if (tabWidth == 0 && textView.isSelected) {
            textView.measureSelf()
            tabWidth = textView.measuredWidth
        }
        vb.mainFlTableContainer.addView(textView, FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            this.leftMargin = index * (tabWidth + tabMargin)
            this.gravity = Gravity.CENTER_VERTICAL
        })
    }

    private fun onSelectedTabChanged() {
        tabs.forEachIndexed { index, textView ->
            if (selectedIndex == index) selectTab(textView) else unselectTab(textView)
        }
    }

    private fun selectTab(tab: TextView) {
        if (tab.isSelected) {
            return
        }
        tab.isSelected = true
        tab.textSize = 16F
        tab.typeface = Typeface.DEFAULT_BOLD
        onTabSelectedListener?.invoke(tabs.indexOf(tab))
    }

    private fun unselectTab(tab: TextView) {
        if (!tab.isSelected) {
            return
        }
        tab.isSelected = false
        tab.textSize = 14F
        tab.typeface = Typeface.DEFAULT
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.position = selectedIndex
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState);
        selectedIndex = savedState.position
        onSelectedTabChanged()
    }

    fun showMessageCount(count: Int) {
        vb.mainMcvMessageCount.setMessageCount(count)
    }

    private class SavedState : BaseSavedState {

        var position = 0

        constructor(superState: Parcelable?) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            position = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(position)
        }

        companion object {

            @JvmField
            @NotNull
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }

    }

}