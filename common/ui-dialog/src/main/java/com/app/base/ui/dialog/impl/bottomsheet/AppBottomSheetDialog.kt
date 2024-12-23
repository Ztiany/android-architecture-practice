package com.app.base.ui.dialog.impl.bottomsheet

import android.content.Context
import android.graphics.Color
import android.view.View
import com.app.base.ui.dialog.dsl.BottomSheetWindowSizeDescription
import com.app.base.ui.dialog.showCompat
import com.google.android.material.bottomsheet.BottomSheetDialog

open class AppBottomSheetDialog(
    context: Context,
    private val sizeDescription: BottomSheetWindowSizeDescription,
) : BottomSheetDialog(context) {

    init {
        with(behavior) {
            maxWidth = sizeDescription.maxWidth(context)
        }
    }

    override fun show() {
        showCompat {
            super.show()
        }
        // A way to set transparent background for BottomSheetDialog.
        // refers to <https://stackoverflow.com/questions/37104960/bottomsheetdialog-with-transparent-background> for more details.
        findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
            post {
                background = android.graphics.drawable.ColorDrawable(Color.TRANSPARENT)
            }
        }
        // or we can use theme to set transparent background for BottomSheetDialog. check the following links for more details.
        // <https://github.com/material-components/material-components-android/issues/267>
        // <https://medium.com/halcyon-mobile/implementing-googles-refreshed-modal-bottom-sheet-4e76cb5de65b>
    }

}