package com.app.base.ui.dialog.dsl.popup

import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.dialog.dsl.Indicator
import com.app.base.ui.dialog.dsl.OnOptionSelectedListener
import com.app.base.ui.dialog.dsl.Option
import com.app.base.ui.dialog.dsl.OptionList
import com.app.base.ui.dialog.dsl.PopupWindowConfig

interface OptionPopupWindowConfig : PopupWindowConfig<OptionPopupWindowDescription> {

    fun list(config: OptionList.() -> Unit)

    fun options(list: List<Option>, onOptionSelectedListener: OnOptionSelectedListener)

    fun customizeList(config: (RecyclerView) -> Unit)

    fun indicator(config: Indicator.() -> Unit)

}

fun OptionPopupWindowConfig.options(
    vararg options: Option,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(options.toList(), onOptionSelectedListener)
}

fun OptionPopupWindowConfig.textOptions(
    list: List<CharSequence>,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(list.map { Option(it) }, onOptionSelectedListener)
}

fun OptionPopupWindowConfig.textOptions(
    vararg texts: CharSequence,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(texts.toList().map { Option(it) }, onOptionSelectedListener)
}

context(Context)
fun OptionPopupWindowConfig.resOptions(
    @StringRes vararg textResArr: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(textResArr.map { Option(getText(it)) }, onOptionSelectedListener)
}

context(Context)
fun OptionPopupWindowConfig.arrResOptions(
    @ArrayRes textArrRes: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(resources.getTextArray(textArrRes).map { Option(it) }, onOptionSelectedListener)
}

context(Fragment)
fun OptionPopupWindowConfig.resOptions(
    @StringRes vararg textResArr: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(textResArr.map { Option(getText(it)) }, onOptionSelectedListener)
}

context(Fragment)
fun OptionPopupWindowConfig.arrResOptions(
    @ArrayRes textArrRes: Int,
    onOptionSelectedListener: OnOptionSelectedListener,
) {
    options(resources.getTextArray(textArrRes).map { Option(it) }, onOptionSelectedListener)
}