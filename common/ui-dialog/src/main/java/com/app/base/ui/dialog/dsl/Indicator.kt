package com.app.base.ui.dialog.dsl

class Indicator {

    private var _offsetFromEnd: Float = -1F

    private var _offsetFromStart: Float = -1F

    @Direction private var _direction: Int = Direction.TOP

    /** -1 means indicator is at center. */
    fun offsetFromEnd(offset: Float) {
        _offsetFromStart = -1F
        _offsetFromEnd = offset
    }

    /** -1 means indicator is at center. */
    fun offsetFromStart(offset: Float) {
        _offsetFromStart = offset
        _offsetFromEnd = -1F
    }

    fun direction(@Direction direction: Int) {
        _direction = direction
    }

    fun toPopupIndicatorDescription(): IndicatorDescription {
        return IndicatorDescription(
            offsetFromEnd = _offsetFromEnd,
            offsetFromStart = _offsetFromStart,
            direction = _direction,
        )
    }

}

class IndicatorDescription(
    val offsetFromEnd: Float,
    val offsetFromStart: Float,
    @Direction val direction: Int,
)