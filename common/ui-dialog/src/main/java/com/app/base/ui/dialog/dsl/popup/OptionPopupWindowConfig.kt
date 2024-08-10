package com.app.base.ui.dialog.dsl.popup

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Indicator
import com.app.base.ui.dialog.dsl.OnOptionSelectedListener
import com.app.base.ui.dialog.dsl.OptionList
import com.app.base.ui.dialog.dsl.PopupWindowConfig

interface OptionPopupWindowConfig : PopupWindowConfig<OptionPopupWindowDescription> {

    fun list(config: OptionList.() -> Unit)

    fun options(list: List<CharSequence>, onOptionSelectedListener: OnOptionSelectedListener)

    fun customizeList(config: (RecyclerView) -> Unit)

    fun indicator(config: Indicator.() -> Unit)

}

fun OptionPopupWindowConfig.options(
    vararg texts: CharSequence,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(texts.toList(), onOptionSelectedListener)
}

context(Context)
fun OptionPopupWindowConfig.resOptions(
    @StringRes vararg textResArr: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(textResArr.map { getText(it) }, onOptionSelectedListener)
}

context(Context)
fun OptionPopupWindowConfig.arrResOptions(
    @ArrayRes textArrRes: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(resources.getTextArray(textArrRes).toList(), onOptionSelectedListener)
}

context(Fragment)
fun OptionPopupWindowConfig.resOptions(
    @StringRes vararg textResArr: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(textResArr.map { getText(it) }, onOptionSelectedListener)
}

context(Fragment)
fun OptionPopupWindowConfig.arrResOptions(
    @ArrayRes textArrRes: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(resources.getTextArray(textArrRes).toList(), onOptionSelectedListener)
}