package com.app.base.ui.dialog.dsl.bottomsheet

import android.content.DialogInterface
import com.app.base.ui.dialog.dsl.DisplayItem

interface ListBottomSheetDialogInterface : DialogInterface {

    var isBottomActionButtonEnable: Boolean

    fun updateTitle(title: CharSequence)

    /**
     * If you have customized your list, don't call this method.
     */
    fun updateItems(list: List<DisplayItem>)

    override fun dismiss()

    override fun cancel()

    fun show()

}