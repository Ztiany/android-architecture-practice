package com.app.base.ui.dialog.dsl

import android.view.View
import com.app.base.ui.dialog.R

fun View.setConditionId(id: Int) {
    setTag(R.id.condition_id, id)
}

fun View.getConditionId(): Int {
    return getTag(R.id.condition_id) as? Int ?: 0
}