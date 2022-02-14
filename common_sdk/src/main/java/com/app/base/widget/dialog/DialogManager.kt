@file:JvmName("DialogManager")

package com.app.base.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.android.base.foundation.common.ActFragWrapper
import com.android.base.utils.android.views.getColorCompat
import com.android.base.utils.android.views.getString
import com.app.base.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

///////////////////////////////////////////////////////////////////////////
// Loading
///////////////////////////////////////////////////////////////////////////
@JvmOverloads
fun createLoadingDialog(context: Context, autoShow: Boolean = false): Dialog {
    val loadingDialog = LoadingDialog(context)
    loadingDialog.setCanceledOnTouchOutside(false)
    loadingDialog.setMessage(R.string.dialog_loading)
    if (autoShow) {
        loadingDialog.show()
    }
    return loadingDialog
}


///////////////////////////////////////////////////////////////////////////
// Tips
///////////////////////////////////////////////////////////////////////////
@IntDef(TipsDialogBuilder.TYPE_SUCCESS, TipsDialogBuilder.TYPE_FAILURE, TipsDialogBuilder.TYPE_WARNING)
annotation class TipsType

class TipsDialogBuilder(private val actFragWrapper: ActFragWrapper) {

    companion object {
        const val TYPE_SUCCESS = 1
        const val TYPE_FAILURE = 2
        const val TYPE_WARNING = 3
    }

    /*消息*/
    @StringRes
    var messageId = BaseDialogBuilder.NO_ID
        set(value) {
            message = actFragWrapper.context.getText(value)
        }
    var message: CharSequence = "debug：请设置message"

    @TipsType var type = TYPE_SUCCESS

    var onDismiss: (() -> Unit)? = null

    var autoDismissMillisecond: Long = 1500
}

fun showTipsDialog(actFragWrapper: ActFragWrapper, builder: TipsDialogBuilder.() -> Unit): Dialog {
    val tipsDialogBuilder = TipsDialogBuilder(actFragWrapper)
    builder(tipsDialogBuilder)
    return showTipsDialogImpl(actFragWrapper, tipsDialogBuilder)
}

fun Fragment.showTipsDialog(builder: TipsDialogBuilder.() -> Unit): Dialog {
    return showTipsDialog(ActFragWrapper.create(this), builder)
}


fun FragmentActivity.showTipsDialog(builder: TipsDialogBuilder.() -> Unit): Dialog {
    return showTipsDialog(ActFragWrapper.create(this), builder)
}

private fun showTipsDialogImpl(actFragWrapper: ActFragWrapper, tipsDialogBuilder: TipsDialogBuilder): Dialog {
    val tipsDialog = TipsDialog(actFragWrapper.context)

    tipsDialog.setMessage(tipsDialogBuilder.message)
    tipsDialog.setCancelable(false)
    tipsDialog.setTipsType(tipsDialogBuilder.type)

    val lifecycleOwner: LifecycleOwner = if (actFragWrapper.fragment != null) {
        actFragWrapper.fragment
    } else {
        actFragWrapper.context as FragmentActivity
    }

    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            tipsDialog.dismiss()
        }
    })

    tipsDialog.setOnDismissListener {
        tipsDialogBuilder.onDismiss?.invoke()
    }

    tipsDialog.show()

    lifecycleOwner.lifecycleScope.launch {
        delay(tipsDialogBuilder.autoDismissMillisecond)
        tipsDialog.dismiss()
    }

    return tipsDialog
}

///////////////////////////////////////////////////////////////////////////
// List
///////////////////////////////////////////////////////////////////////////
class ListDialogBuilder(context: Context) : BaseDialogBuilder(context) {

    /**标题id*/
    @StringRes
    var titleId: Int = NO_ID
        set(value) {
            title = context.getText(value)
        }
    var title: CharSequence? = null

    /**标题的字体大小，单位为 sp*/
    var titleSize = 16F

    /**标题的字体颜色*/
    var titleColor = Color.parseColor("#333333")

    @ArrayRes
    var itemsId: Int = NO_ID
        set(value) {
            items = context.resources.getTextArray(value)
        }
    var items: Array<CharSequence>? = null

    var adapter: RecyclerView.Adapter<*>? = null

    var selectedPosition: Int = 0

    var positiveListener: ((which: Int, item: CharSequence) -> Unit)? = null
    var negativeListener: (() -> Unit)? = null

    /**对话框最长高度（相对于屏幕高度）*/
    var maxWidthPercent = BaseDialog.DEFAULT_WIDTH_SIZE_PERCENT

    var onDialogPreparedListener: ((dialog: Dialog, dialogController: DialogController) -> Unit)? = null

}

fun showListDialog(context: Context, builder: ListDialogBuilder.() -> Unit): Dialog {
    val listDialogBuilder = ListDialogBuilder(context)
    builder.invoke(listDialogBuilder)
    val listDialog = ListDialog(listDialogBuilder)
    listDialog.show()
    return listDialog
}

fun Fragment.showListDialog(builder: ListDialogBuilder.() -> Unit): Dialog? {
    val context = this.context ?: return null
    return showListDialog(context, builder)
}

fun Activity.showListDialog(builder: ListDialogBuilder.() -> Unit): Dialog {
    return showListDialog(this, builder)
}

///////////////////////////////////////////////////////////////////////////
// Confirm Dialog
///////////////////////////////////////////////////////////////////////////
open class BaseDialogBuilder(val context: Context) {

    companion object {
        internal const val NO_ID = 0
    }

    /**样式*/
    var style: Int = R.style.ThemeDialogCommon_Transparent_Floating

    /**确认按钮*/
    @StringRes
    var positiveId: Int = NO_ID
        set(value) {
            positiveText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var positiveText: CharSequence? = context.getText(R.string.sure)
    @ColorInt var positiveColor: Int = context.getColorCompat(R.color.colorPrimary)

    /**否认按钮*/
    @StringRes
    var negativeId: Int = NO_ID
        set(value) {
            negativeText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var negativeText: CharSequence? = context.getText(R.string.cancel_)
    @ColorInt var negativeColor: Int = context.getColorCompat(R.color.text_level2)

    fun disableNegative() {
        negativeText = null
    }

    /**选择后，自动 dismiss*/
    var autoDismiss: Boolean = true

    var cancelable: Boolean = true
    var cancelableTouchOutside: Boolean = true
}

interface DialogController {
    var positiveEnable: Boolean
}

class ConfirmDialogBuilder(context: Context) : BaseDialogBuilder(context) {

    ///////////////////////////////////////////////////////////////////////////
    // title
    ///////////////////////////////////////////////////////////////////////////
    /**标题id*/
    @StringRes
    var titleId: Int = NO_ID
        set(value) {
            title = context.getText(value)
        }
    var title: CharSequence? = null

    /**标题的字体大小，单位为 sp*/
    var titleSize = -1F

    /**标题的字体颜色*/
    var titleColor = Color.BLACK

    ///////////////////////////////////////////////////////////////////////////
    // message
    ///////////////////////////////////////////////////////////////////////////
    @StringRes
    var messageId = NO_ID
        set(value) {
            message = context.getText(value)
        }
    var message: CharSequence = "debug：请设置message"

    /**消息的字体大小，单位为 sp*/
    var messageSize = -1F

    /**消息的字体颜色*/
    var messageColor = Color.BLACK

    /**消息对其方式*/
    var messageGravity: Int = Gravity.CENTER

    ///////////////////////////////////////////////////////////////////////////
    //中间的按钮
    ///////////////////////////////////////////////////////////////////////////
    /**中间的按钮*/
    @StringRes
    var neutralId: Int = NO_ID
        set(value) {
            neutralText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var neutralText: CharSequence? = null
    @ColorInt var neutralColor: Int = context.getColorCompat(R.color.text_level1)

    ///////////////////////////////////////////////////////////////////////////
    // checkbox
    ///////////////////////////////////////////////////////////////////////////
    @StringRes
    var checkBoxId = NO_ID
        set(value) {
            checkBoxText = context.getText(value)
        }
    var checkBoxText: CharSequence = ""
    var checkBoxChecked = false

    //确认与取消
    var positiveListener: ((dialog: Dialog) -> Unit)? = null
    var positiveListener2: ((dialog: Dialog, isChecked: Boolean) -> Unit)? = null
    var negativeListener: ((dialog: Dialog) -> Unit)? = null
    var neutralListener: ((dialog: Dialog) -> Unit)? = null

    //顶部 icon
    var iconId = NO_ID
}

fun showConfirmDialog(context: Context, builder: ConfirmDialogBuilder.() -> Unit): Dialog {
    val confirmDialogBuilder = ConfirmDialogBuilder(context)
    builder.invoke(confirmDialogBuilder)
    val confirmDialog = ConfirmDialog(confirmDialogBuilder)
    confirmDialog.show()
    return confirmDialog
}

fun Fragment.showConfirmDialog(builder: ConfirmDialogBuilder.() -> Unit): Dialog? {
    val context = this.context ?: return null
    return showConfirmDialog(context, builder)
}

fun Activity.showConfirmDialog(builder: ConfirmDialogBuilder.() -> Unit): Dialog {
    return showConfirmDialog(this, builder)
}

///////////////////////////////////////////////////////////////////////////
// AppBottomSheetDialog
///////////////////////////////////////////////////////////////////////////
class BottomSheetDialogBuilder(val context: Context) {

    /**default is [R.string.cancel_]*/
    var actionTextId = BaseDialogBuilder.NO_ID
        set(value) {
            actionText = context.getText(value)
        }

    /**default is the text defined by [R.string.cancel_]，if set it empty than the action view will be hidden.*/
    var actionText: CharSequence = getString(R.string.cancel_)
    fun noBottomAction() {
        actionText = ""
    }

    /**default is ""，it means the title view is hidden by default*/
    var titleText: CharSequence = ""

    /**default is empty*/
    var titleTextId = BaseDialogBuilder.NO_ID
        set(value) {
            titleText = context.getText(value)
        }

    var itemGravity: Int = Gravity.CENTER
    var itemTextColor: Int = Color.parseColor("#333333")
    var itemSelectedListener: ((which: Int, item: CharSequence) -> Unit)? = null
    var itemSelectedListener2: ((dialog: Dialog, which: Int, item: CharSequence) -> Unit)? = null
    var actionListener: (() -> Unit)? = null
    var items: List<CharSequence>? = null

    var selectedPosition: Int = -1

    /**if you want to apply your own list style, you can provide a [customList] callback, then other properties(like [items]) will not be effective.*/
    var customList: ((dialog: Dialog, list: RecyclerView) -> Unit)? = null

    /**选择后，自动 dismiss*/
    var autoDismiss: Boolean = true
}

fun showBottomSheetListDialog(context: Context, builder: BottomSheetDialogBuilder.() -> Unit): Dialog {
    val bottomSheetDialogBuilder = BottomSheetDialogBuilder(context)
    bottomSheetDialogBuilder.builder()
    val bottomSheetDialog = AppBottomSheetDialog(bottomSheetDialogBuilder)
    bottomSheetDialog.show()
    return bottomSheetDialog
}

fun Fragment.showBottomSheetListDialog(builder: BottomSheetDialogBuilder.() -> Unit): Dialog? {
    val context = this.context ?: return null
    return showBottomSheetListDialog(context, builder)
}

fun Activity.showBottomSheetListDialog(builder: BottomSheetDialogBuilder.() -> Unit): Dialog {
    return showBottomSheetListDialog(this, builder)
}


