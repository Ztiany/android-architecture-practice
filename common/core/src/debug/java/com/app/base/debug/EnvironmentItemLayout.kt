package com.app.base.debug

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.layoutInflater
import com.android.base.utils.android.views.setPaddings
import com.app.base.config.EnvironmentItem
import com.app.base.config.EnvironmentContext
import com.app.base.databinding.BaseDebugEnvironmentItemBinding

internal class EnvironmentItemLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr) {

    private val viewBinding = BaseDebugEnvironmentItemBinding.inflate(layoutInflater(), this)

    init {
        orientation = VERTICAL
        setPaddings(dip(10))
    }

    private lateinit var list: List<EnvironmentItem>
    private lateinit var categoryName: String

    fun bindEnvironmentList(categoryName: String, list: List<EnvironmentItem>) {
        this.categoryName = categoryName
        this.list = list
        viewBinding.baseTvDebugHostName.text = categoryName

        showSelectedValue(EnvironmentContext.selected(categoryName))

        viewBinding.baseBtnDebugSwitch.setOnClickListener {
            showSwitchDialog()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showSelectedValue(selected: EnvironmentItem) {
        viewBinding.baseTvDebugHostValue.text = "${selected.name}：${selected.value}"
    }

    @SuppressLint("SetTextI18n")
    fun refresh() {
        showSelectedValue(EnvironmentContext.selected(categoryName))
    }

    private fun showSwitchDialog() {
        val first = list.indexOf(EnvironmentContext.selected(categoryName))

        AlertDialog.Builder(context)
            .setSingleChoiceItems(list.map { it.name }.toTypedArray(), first) { dialog, which ->
                dialog.dismiss()
                val env = list[which]
                showSelectedValue(env)
                EnvironmentContext.select(categoryName, env)
            }.show()
    }

}