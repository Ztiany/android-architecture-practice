package com.app.base.widget.dialog.bottomsheet

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.android.base.utils.android.views.getString
import com.app.base.ui.R
import com.app.base.widget.dialog.common.BaseDialogBuilder
import com.google.android.material.color.MaterialColors

class BottomSheetDialogBuilder internal constructor(val context: Context) {

    /** default is [com.app.base.ui.R.string.cancel] */
    @StringRes
    var actionResId = BaseDialogBuilder.NO_ID
        set(value) {
            actionText = context.getText(value)
        }

    /** default is the text defined by [com.app.base.ui.R.string.cancel]，if set it empty than the action view will be hidden . */
    var actionText: CharSequence = getString(R.string.cancel)

    /**  default is 14SP */
    var actionSize: Float = 14F

    fun noAction() {
        actionText = ""
    }

    /** default is "", it means the title view is hidden by default. */
    var titleText: CharSequence = ""

    /**  default is 16SP */
    var titleSize: Float = 16F

    /** default is empty*/
    @StringRes
    var titleResId = BaseDialogBuilder.NO_ID
        set(value) {
            titleText = context.getText(value)
        }

    var itemGravity: Int = Gravity.CENTER
    var itemTextColor: Int = MaterialColors.getColor(context, R.attr.app_color_text_level1, "app_color_text_level1 not provided.")
    var itemSelectedListener: ((which: Int, item: CharSequence) -> Unit)? = null
    var itemSelectedListener2: ((dialog: Dialog, which: Int, item: CharSequence) -> Unit)? = null
    var actionListener: (() -> Unit)? = null
    var items: List<CharSequence>? = null

    var selectedPosition: Int = -1

    /** if you want to apply your own list style, you can provide a [customList] callback, then other properties(like [items]) will not be effective . */
    var customList: ((dialog: Dialog, list: RecyclerView) -> Unit)? = null

    /** 选择后，自动 dismiss */
    var autoDismiss: Boolean = true

    ///////////////////////////////////////////////////////////////////////////
    // Behavior
    ///////////////////////////////////////////////////////////////////////////
    var cancelable: Boolean = true
    var skipCollapsed: Boolean = false
    var expandedDirectly: Boolean = false
}

fun Fragment.showBottomSheetListDialog(
    builder: BottomSheetDialogBuilder.() -> Unit,
): com.google.android.material.bottomsheet.BottomSheetDialog {
    return showBottomSheetListDialog(requireContext(), builder)
}

fun Activity.showBottomSheetListDialog(
    builder: BottomSheetDialogBuilder.() -> Unit,
): com.google.android.material.bottomsheet.BottomSheetDialog {
    return showBottomSheetListDialog(this, builder)
}

fun showBottomSheetListDialog(
    context: Context,
    builder: BottomSheetDialogBuilder.() -> Unit,
): com.google.android.material.bottomsheet.BottomSheetDialog {
    val bottomSheetDialogBuilder = BottomSheetDialogBuilder(context)
    bottomSheetDialogBuilder.builder()
    val bottomSheetDialog = BottomSheetDialog(bottomSheetDialogBuilder)
    bottomSheetDialog.show()
    return bottomSheetDialog
}