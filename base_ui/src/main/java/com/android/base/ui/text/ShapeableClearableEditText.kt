package com.android.base.ui.text

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import com.android.base.ui.common.TextColorView
import com.android.base.ui.shape.EnhancedShapeable
import com.android.base.ui.shape.MaterialShapeDrawableHelper
import com.android.base.ui.shape.ShapeTextColorHelper
import com.google.android.material.shape.ShapeAppearanceModel

class ShapeableClearableEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = android.R.attr.editTextStyle
) : ClearableEditText(context, attrs, defStyleAttr), EnhancedShapeable, TextColorView {

    private val mdHelper = MaterialShapeDrawableHelper(context, attrs, defStyleAttr)

    private val colorHelper = ShapeTextColorHelper(context, attrs, defStyleAttr)

    init {
        mdHelper.update(this)
        colorHelper.setTextColor(this)
    }

    override fun updateShapeDrawable() {
        mdHelper.update(this)
    }

    override fun updateTextColor() {
        colorHelper.setTextColor(this)
    }

    override fun setShapeAppearanceModel(shapeAppearanceModel: ShapeAppearanceModel) {
        mdHelper.updateShapeAppearanceModel(shapeAppearanceModel)
    }

    override fun getShapeAppearanceModel(): ShapeAppearanceModel {
        return mdHelper.obtainShapeAppearanceModel()
    }

}