package com.app.base.ui.dialog.dsl.list

import android.content.DialogInterface
import com.app.base.ui.dialog.dsl.DisplayItem

interface ListDialogInterface : DialogInterface {

    var isPositiveButtonEnable: Boolean

    fun updateTitle(title: CharSequence)

    /**
     * If you have customized your list, don't call this method.
     */
    fun updateItems(list: List<DisplayItem>)

    override fun dismiss()

    override fun cancel()

    fun show()

}