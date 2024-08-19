package com.app.base.ui.dialog.impl.popup

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow.OnDismissListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.utils.android.activityContext
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.android.views.setHorizontalPadding
import com.android.base.utils.common.findWithIndex
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.dialog.AppPopupWindow
import com.app.base.ui.dialog.databinding.PopupLayoutListBinding
import com.app.base.ui.dialog.dsl.PopupDimDescription
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.popup.MultiSelectionPopupWindowDescription
import com.app.base.ui.dialog.dsl.popup.MultiSelectionPopupWindowInterface
import com.app.base.ui.dialog.dsl.popup.SelectionPopupWindowDescription
import com.app.base.ui.dialog.dsl.popup.SelectionPopupWindowInterface
import com.app.base.ui.dialog.dsl.popup.SingleSelectionPopupWindowDescription
import com.app.base.ui.dialog.dsl.popup.SingleSelectionPopupWindowInterface
import com.app.base.ui.dialog.dsl.popup.listCustomized
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper
import com.app.base.ui.dialog.impl.bottomsheet.SectionDialogAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

internal class SelectionPopupWindow(
    private val context: Context,
    lifecycleOwner: LifecycleOwner,
    private val description: SelectionPopupWindowDescription,
) : AppPopupWindow<SelectionPopupWindowInterface>(context), SingleSelectionPopupWindowInterface, MultiSelectionPopupWindowInterface {

    private val vb = PopupLayoutListBinding.inflate(LayoutInflater.from(context))

    private val onDismissListeners: MutableList<OnDismissListener> = mutableListOf()

    private val dialogInterfaceWrapper by unsafeLazy {
        DialogInterfaceWrapper(this)
    }

    private val internalOnDismissListener = OnDismissListener {
        onDismissListeners.forEach { listener ->
            listener.onDismiss()
        }
        recoverTargetView()
    }

    private var onCheckAllSelectionsListener: (() -> Unit)? = null
    private var onPositiveButtonClickedListener: (DialogInterface.() -> Unit)? = null

    private var originalBackground: Drawable? = null
    private var originalNavigationColor: Int? = null

    private var selectedSelection: Pair<Int, Selection>? = null

    private val selectionAdapter by unsafeLazy {
        val listDescription = description.list ?: throw IllegalStateException("listDescription is null")
        SectionDialogAdapter(context, listDescription) { position, selection ->
            handleOnItemSelected(position, selection)
        }
    }

    override val controller: SelectionPopupWindowInterface
        get() = this

    private inline fun SelectionPopupWindowDescription.discriminate(
        single: SingleSelectionPopupWindowDescription.() -> Unit,
        multi: MultiSelectionPopupWindowDescription.() -> Unit,
    ) {
        when (this) {
            is SingleSelectionPopupWindowDescription -> single()
            is MultiSelectionPopupWindowDescription -> multi()
        }
    }

    init {
        contentView = vb.root
        description.size.applyTo(this)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setAttributes()
        rememberWidgetState()

        setupTitle()
        setupList()
        setUpListDecor()
        setUpBottomButton()

        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                dismiss()
            }
        })
    }

    private fun setUpBottomButton() = with(vb) {
        description.positiveButton.ifNonNull {
            dialogSpaceLeft.beGone()
            dialogBtnBottomLeft.beGone()
            textDescription.applyTo(dialogBtnBottomRight)
            dialogBtnBottomRight.onThrottledClick {
                handleOnPositiveButtonClicked()
            }
        } otherwise { dialogLlBottomButtons.beGone() }
    }

    private fun setUpListDecor() = description.discriminate(
        single = {
            listTopAreaConfig?.invoke(this@SelectionPopupWindow, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
            listBottomAreaConfig?.invoke(this@SelectionPopupWindow, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
        },
        multi = {
            listTopAreaConfig?.invoke(this@SelectionPopupWindow, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
            listBottomAreaConfig?.invoke(this@SelectionPopupWindow, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
        }
    )

    private fun setAttributes() {
        description.behavior.applyTo(this)
        setOnDismissListener(internalOnDismissListener)
        description.behavior.onDismissListener?.let {
            setOnDismissListener {
                it.invoke(dialogInterfaceWrapper.canceledByUser)
            }
        }
    }

    private fun setupTitle() = with(description) {
        with(vb) {
            title.ifNonNull {
                applyTo(dialogTvTitle)
            } otherwise { dialogFlTitleContainer.beGone() }
        }
    }

    private fun setupList() = with(description) {
        discriminate(
            single = {
                customizeList?.let {
                    it.invoke(this@SelectionPopupWindow, vb.dialogRvList)
                    return
                }
            },
            multi = {
                customizeList?.let {
                    it.invoke(this@SelectionPopupWindow, vb.dialogRvList)
                    return
                }
            }
        )

        discriminate(
            single = {
                list?.items?.findWithIndex { it.selected }?.let {
                    if (it.second != null) {
                        selectedSelection = it.first to it.second!!
                    }
                }
            },
            multi = {
                vb.dialogBtnBottomRight.setText(com.app.base.ui.theme.R.string.check_all)
                rightTitleActionTextStyle.applyTo(vb.dialogBtnBottomRight)
                vb.dialogBtnBottomRight.onThrottledClick {
                    checkAllSelections()
                }
            }
        )

        val listDescription = list ?: return
        with(vb.dialogRvList) {
            setHorizontalPadding(context.resources.getDimensionPixelSize(com.app.base.ui.theme.R.dimen.common_page_edge))
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MaterialDividerItemDecoration(context, com.android.base.ui.recyclerview.DividerItemDecoration.VERTICAL).apply {
                dividerColor = listDescription.dividerColor
                dividerThickness = listDescription.dividerThickness
                dividerInsetStart = listDescription.dividerInsetStart
                dividerInsetEnd = listDescription.dividerInsetEnd
            })
            adapter = selectionAdapter
        }

        updateConfirmBtnState()
    }

    private fun handleOnItemSelected(position: Int, selection: Selection) {
        description.list?.onSelectionClickListener?.invoke(dialogInterfaceWrapper, position, selection)

        description.discriminate(
            multi = {
                selectionAdapter.replace(selection, selection.copy(selected = !selection.selected))
            },
            single = {
                val oldItem = selectionAdapter.getList().find { item -> item.selected }
                if (selection == oldItem) {
                    return
                }
                oldItem?.let {
                    selectionAdapter.replace(it, it.copy(selected = false))
                }
                val selected = selection.copy(selected = true)
                selectionAdapter.replace(selection, selected)
                selectedSelection = Pair(position, selected)
            },
        )
        updateConfirmBtnState()
    }

    private fun updateConfirmBtnState() = description.discriminate(
        single = {
            vb.dialogBtnBottomRight.isEnabled = selectedSelection != null
        },
        multi = {
            vb.dialogBtnBottomRight.isEnabled = selectionAdapter.getList().any { it.selected }
        }
    )

    private fun handleOnPositiveButtonClicked() {
        onPositiveButtonClickedListener?.invoke(dialogInterfaceWrapper)

        if (description.listCustomized()) {
            return
        }

        description.discriminate(
            single = {
                selectedSelection?.let {
                    onSingleItemSelectedListener?.invoke(it.first, it.second)
                }
            },
            multi = {
                onMultiItemSelectedListener?.invoke(selectionAdapter.getList().filter { it.selected })
            }
        )
        dismissChecked()
    }

    private fun dismissChecked() {
        if (isShowing && description.behavior.shouldAutoDismiss) {
            dialogInterfaceWrapper.dismiss()
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // dim
    ///////////////////////////////////////////////////////////////////////////

    override fun setOnDismissListener(onDismissListener: OnDismissListener?) {
        if (onDismissListener == internalOnDismissListener) {
            super.setOnDismissListener(onDismissListener)
        } else if (onDismissListener != null) {
            onDismissListeners.add(onDismissListener)
        }
    }

    override fun showAsDropDown(anchor: View) {
        super.showAsDropDown(anchor)
        dimTargetView()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        super.showAsDropDown(anchor, xoff, yoff)
        dimTargetView()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int, gravity: Int) {
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        dimTargetView()
    }

    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        super.showAtLocation(parent, gravity, x, y)
        dimTargetView()
    }

    private fun rememberWidgetState() {
        description.popupDim?.let {
            originalBackground = it.dimView?.background
            context.activityContext?.window?.navigationBarColor?.let { color ->
                originalNavigationColor = color
            }
        }
    }

    private fun dimTargetView() {
        val dimDescription = description.popupDim ?: return

        dimDescription.dimView?.let {
            it.background = ColorDrawable(dimDescription.dimColor)
            if (dimDescription.includeNavigationBar) {
                context.activityContext?.window?.navigationBarColor = dimDescription.dimColor
            }
            return
        }

        updateDimAmount(dimDescription.dimPercent)
    }

    private fun recoverTargetView() {
        val dimDescription = description.popupDim ?: return
        dimDescription.dimView?.let {
            it.background = originalBackground
            if (dimDescription.includeNavigationBar) {
                originalNavigationColor?.let { color ->
                    context.activityContext?.window?.navigationBarColor = color
                }
            }
        }
    }

    private fun updateDimAmount(percent: Float) {
        if (percent == PopupDimDescription.NO_DIM) return
        getDecorView()?.let {
            val layoutParams = it.layoutParams as WindowManager.LayoutParams
            layoutParams.flags = layoutParams.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
            layoutParams.dimAmount = percent
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).updateViewLayout(it, layoutParams)
        }
    }

    private fun getDecorView(): View? {
        var decorView: View? = null
        try {
            decorView = if (background == null) {
                contentView.parent as View
            } else {
                contentView.parent.parent as View
            }
        } catch (ignore: Exception) {
        }
        return decorView
    }

    ///////////////////////////////////////////////////////////////////////////
    // Interface Implementation
    ///////////////////////////////////////////////////////////////////////////

    override fun cancel() {
        dismiss()
    }

    override fun checkAllSelections() {
        onCheckAllSelectionsListener?.invoke()
        if (!description.listCustomized()) {
            selectionAdapter.checkAll()
            updateConfirmBtnState()
        }
    }

    override var isPositiveButtonEnable: Boolean
        get() = vb.dialogBtnBottomRight.isEnabled
        set(value) {
            vb.dialogBtnBottomRight.isEnabled = value
        }

    override fun updateTitle(title: CharSequence) {
        vb.dialogTvTitle.text = title
    }

    override fun updateSelections(list: List<Selection>) {
        selectionAdapter.replaceAll(list)
    }

    override fun setOnCheckAllSelectionsListener(listener: () -> Unit) {
        onCheckAllSelectionsListener = listener
    }

    override fun setOnPositiveButtonClickedListener(listener: DialogInterface.() -> Unit) {
        onPositiveButtonClickedListener = listener
    }

}