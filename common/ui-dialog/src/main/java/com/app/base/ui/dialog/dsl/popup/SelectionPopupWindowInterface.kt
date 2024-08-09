package com.app.base.ui.dialog.dsl.popup

import android.content.DialogInterface
import com.app.base.ui.dialog.dsl.Selection

interface SelectionPopupWindowInterface : PopupWindowInterface {

    var isPositiveButtonEnable: Boolean

    fun updateTitle(title: CharSequence)

    /**
     * If you have customized your list, don't call this method.
     */
    fun updateSelections(list: List<Selection>)

    /** Only useful when you have customized the list. */
    fun setOnPositiveButtonClickedListener(listener: DialogInterface. () -> Unit)

}

interface SingleSelectionPopupWindowInterface : SelectionPopupWindowInterface

interface MultiSelectionPopupWindowInterface : SelectionPopupWindowInterface {

    fun checkAllSelections()

    /** Only useful when you have customized the list. */
    fun setOnCheckAllSelectionsListener(listener: () -> Unit)

}