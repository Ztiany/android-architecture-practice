package com.app.base.ui.dialog.dsl.bottomsheet

import android.content.Context
import com.app.base.ui.dialog.dsl.BottomSheetWindowSize
import com.app.base.ui.dialog.dsl.DialogBehavior
import com.app.base.ui.dialog.dsl.DialogConfig
import com.app.base.ui.dialog.dsl.DialogDescription

interface BottomSheetDialogConfig<Behavior : DialogBehavior, Description : DialogDescription> : DialogConfig<Behavior, Description> {

    fun size(config: BottomSheetWindowSize.() -> Unit)

}

fun BottomSheetDialogConfig<*, *>.maxWidth(block: (Context) -> Int) {
    size {
        maxWidth(block)
    }
}