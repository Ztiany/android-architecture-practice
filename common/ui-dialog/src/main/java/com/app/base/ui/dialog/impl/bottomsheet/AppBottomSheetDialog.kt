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
        //https://stackoverflow.com/questions/37104960/bottomsheetdialog-with-transparent-background
        findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.apply {
            post {
                background = android.graphics.drawable.ColorDrawable(Color.TRANSPARENT)
            }
        }
    }

}