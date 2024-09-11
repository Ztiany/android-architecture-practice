package com.app.base.ui.dialog.dsl.list

import android.content.DialogInterface
import com.app.base.ui.dialog.dsl.Selection

interface SelectionListDialogInterface : DialogInterface {

    var isPositiveButtonEnable: Boolean

    fun updateTitle(title: CharSequence)

    /**
     * If you have customized your list, don't call this method.
     */
    fun updateSelections(list: List<Selection>)

    override fun dismiss()

    override fun cancel()

    fun show()

    /** Only useful when you have customized the list. */
    fun setOnPositiveButtonClickedListener(listener: DialogInterface. () -> Unit)

    /** Only useful when you have customized the list. */
    fun setOnNegativeButtonClickedListener(listener: DialogInterface. () -> Unit)

}

interface SingleSelectionListDialogInterface : SelectionListDialogInterface

interface MultiSelectionListDialogInterface : SelectionListDialogInterface {

    fun checkAllSelections()

    /** Only useful when you have customized the list. */
    fun setOnCheckAllSelectionsListener(listener: () -> Unit)

}