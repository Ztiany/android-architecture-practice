package com.app.base.ui.dialog.dsl

class Indicator {

    private var _offsetFromEnd: Int = -1

    private var _offsetFromStart: Int = -1

    @Direction private var _direction: Int = Direction.TOP

    /** -1 means indicator is at center. */
    fun offsetFromEnd(offset: Int) {
        _offsetFromStart = -1
        _offsetFromEnd = offset
    }

    /** -1 means indicator is at center. */
    fun offsetFromStart(offset: Int) {
        _offsetFromStart = offset
        _offsetFromEnd = -1
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
    val offsetFromEnd: Int,
    val offsetFromStart: Int,
    @Direction val direction: Int,
)