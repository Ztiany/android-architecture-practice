package com.app.base.ui.dialog.dsl

import android.content.Context

class BottomSheetWindowSize(
    private var _maxWidth: ((Context) -> Int),
) {

    fun maxWidth(block: (Context) -> Int) {
        _maxWidth = block
    }

    fun toBottomSheetWindowSizeDescription(): BottomSheetWindowSizeDescription {
        return BottomSheetWindowSizeDescription(_maxWidth)
    }

}

class BottomSheetWindowSizeDescription(val maxWidth: ((Context) -> Int))