package com.app.base.widget.dialog.list

import android.app.Activity
import android.content.Context
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.app.base.ui.R
import com.app.base.widget.dialog.BaseDialogBuilder
import com.google.android.material.color.MaterialColors

class ListDialogBuilder(context: Context) : BaseDialogBuilder(context) {

    /** 标题id */
    @StringRes
    var titleResId: Int = NO_ID
        set(value) {
            title = context.getText(value)
        }
    var title: CharSequence? = null

    /** 标题的字体大小，单位为 sp */
    var titleSize = 16F

    /** 标题的字体颜色 */
    var titleColor = MaterialColors.getColor(context, R.attr.app_color_text_level1, "app_color_text_level1 not provided.")

    @ArrayRes
    var itemArrayResId: Int = NO_ID
        set(value) {
            items = context.resources.getTextArray(value)
        }
    var items: Array<CharSequence>? = null

    var adapter: RecyclerView.Adapter<*>? = null

    var selectedPosition: Int = 0

    var positiveListener: ((which: Int, item: CharSequence) -> Unit)? = null
    var negativeListener: (() -> Unit)? = null

    /** 对话框最长高度（相对于屏幕高度）*/
    var maxWidthPercent = 0.75F
}

fun Fragment.showListDialog(builder: ListDialogBuilder.() -> Unit): ListDialogInterface? {
    val context = this.context ?: return null
    return showListDialog(context, builder)
}

fun Activity.showListDialog(builder: ListDialogBuilder.() -> Unit): ListDialogInterface {
    return showListDialog(this, builder)
}

fun showListDialog(context: Context, builder: ListDialogBuilder.() -> Unit): ListDialogInterface {
    val listDialogBuilder = ListDialogBuilder(context)
    builder.invoke(listDialogBuilder)
    val appListDialog = ListDialog(listDialogBuilder)
    appListDialog.show()
    return appListDialog
}