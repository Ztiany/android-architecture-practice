package com.app.base.ui.widget.shapeable

import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily


/**
 *@author Ztiany
 */
fun ShapeableImageView.setRoundCornerSize(size: Float) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder().setAllCorners(CornerFamily.ROUNDED, size).build()
}

fun ShapeableImageView.setTopRoundCornerSize(size: Float) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setTopLeftCorner(CornerFamily.ROUNDED, size)
        .setTopRightCorner(CornerFamily.ROUNDED, size)
        .setBottomLeftCorner(CornerFamily.ROUNDED, 0F)
        .setBottomRightCorner(CornerFamily.ROUNDED, 0F)
        .build()
}

fun ShapeableImageView.setBottomRoundCornerSize(size: Float) {
    shapeAppearanceModel = shapeAppearanceModel.toBuilder()
        .setTopLeftCorner(CornerFamily.ROUNDED, 0F)
        .setTopRightCorner(CornerFamily.ROUNDED, 0F)
        .setBottomLeftCorner(CornerFamily.ROUNDED, size)
        .setBottomRightCorner(CornerFamily.ROUNDED, size)
        .build()
}

