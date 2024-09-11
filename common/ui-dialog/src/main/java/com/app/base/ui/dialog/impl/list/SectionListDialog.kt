package com.app.base.ui.dialog.impl.list

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.ui.recyclerview.DividerItemDecoration
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.dip
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.android.views.setHorizontalPadding
import com.android.base.utils.common.findWithIndex
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.dialog.AppBaseDialog
import com.app.base.ui.dialog.databinding.DialogLayoutBottomsheetListBinding
import com.app.base.ui.dialog.dsl.Condition
import com.app.base.ui.dialog.dsl.OnClickListener
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.applyToDialog
import com.app.base.ui.dialog.dsl.list.MultiSelectionListDialogDescription
import com.app.base.ui.dialog.dsl.list.MultiSelectionListDialogInterface
import com.app.base.ui.dialog.dsl.list.SelectionListDialogDescription
import com.app.base.ui.dialog.dsl.list.SingleSelectionListDialogDescription
import com.app.base.ui.dialog.dsl.list.SingleSelectionListDialogInterface
import com.app.base.ui.dialog.dsl.list.listCustomized
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper
import com.app.base.ui.dialog.impl.bottomsheet.SectionDialogAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

internal class SectionListDialog(
    context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val description: SelectionListDialogDescription,
) : AppBaseDialog(context, description.size), SingleSelectionListDialogInterface, MultiSelectionListDialogInterface {

    private val vb by lazy {
        // It is necessary to use the context of the dialog, otherwise the theme will not be applied!
        DialogLayoutBottomsheetListBinding.inflate(LayoutInflater.from(getContext()))
    }

    private val condition by unsafeLazy { object : Condition {} }

    private val dialogInterfaceWrapper by unsafeLazy { DialogInterfaceWrapper(this) }

    private var selectedSelection: Pair<Int, Selection>? = null

    private val selectionAdapter by unsafeLazy {
        val listDescription = description.list ?: throw IllegalStateException("listDescription is null")
        SectionDialogAdapter(context, listDescription) { position, selection ->
            handleOnItemSelected(position, selection)
        }
    }

    private var onCheckAllSelectionsListener: (() -> Unit)? = null
    private var onPositiveButtonClickedListener: (DialogInterface.() -> Unit)? = null
    private var onNegativeButtonClickedListener: (DialogInterface.() -> Unit)? = null

    private inline fun SelectionListDialogDescription.discriminate(
        single: SingleSelectionListDialogDescription.() -> Unit,
        multi: MultiSelectionListDialogDescription.() -> Unit,
    ) {
        when (this) {
            is SingleSelectionListDialogDescription -> single()
            is MultiSelectionListDialogDescription -> multi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb.dialogLlBottomsheetRoot.getShapeDrawable().setCornerSize(dip(10F))
        setContentView(vb.root)

        description.discriminate(
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

        setupTitle()
        setupList()
        setUpListDecor()
        setUpBottomButton()
        setBehavior()

        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                dismiss()
            }
        })
    }

    private fun setUpBottomButton() = with(vb) {
        if (description.negativeButton == null && description.positiveButton == null) {
            dialogLlBottomButtons.beGone()
        }

        description.negativeButton.ifNonNull {
            textDescription.applyTo(dialogBtnBottomLeft)
            dialogBtnBottomLeft.onThrottledClick {
                handleOnNegativeButtonClicked(this.onClickListener)
            }
        } otherwise {
            dialogSpaceLeft.beGone()
            dialogBtnBottomLeft.beGone()
        }

        description.positiveButton.ifNonNull {
            textDescription.applyTo(dialogBtnBottomRight)
            dialogBtnBottomRight.onThrottledClick {
                handleOnPositiveButtonClicked()
            }
        } otherwise {
            dialogSpaceRight.beGone()
            dialogBtnBottomRight.beGone()
        }
    }

    private fun setUpListDecor() = description.discriminate(
        single = {
            listTopAreaConfig?.invoke(this@SectionListDialog, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
            listBottomAreaConfig?.invoke(this@SectionListDialog, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
        },
        multi = {
            listTopAreaConfig?.invoke(this@SectionListDialog, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
            listBottomAreaConfig?.invoke(this@SectionListDialog, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
        }
    )

    private fun setBehavior() = with(description.behavior) {
        applyToDialog(this@SectionListDialog)
        setOnDismissListener {
            onDismissListener?.invoke(dialogInterfaceWrapper.canceledByUser)
        }
    }

    private fun setupTitle() = with(description) {
        with(vb) {
            title.ifNonNull {
                applyTo(dialogTvTitle)
            } otherwise { dialogFlTitleContainer.beGone() }
        }
    }

    private fun dismissChecked() {
        if (isShowing && description.behavior.shouldAutoDismiss) {
            dialogInterfaceWrapper.dismiss()
        }
    }

    private fun setupList() = with(description) {
        discriminate(
            single = {
                customizeList?.let {
                    it.invoke(this@SectionListDialog, vb.dialogRvList)
                    return
                }
            },
            multi = {
                customizeList?.let {
                    it.invoke(this@SectionListDialog, vb.dialogRvList)
                    return
                }
            }
        )

        val listDescription = list ?: return
        with(vb.dialogRvList) {
            setHorizontalPadding(context.resources.getDimensionPixelSize(com.app.base.ui.theme.R.dimen.common_page_edge))
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MaterialDividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
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
            }
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
                onMultiItemSelectedListener?.invoke(
                    selectionAdapter.getList().filter { it.selected }
                )
            }
        )
        dismissChecked()
    }

    private fun handleOnNegativeButtonClicked(onClickListener: OnClickListener?) {
        onNegativeButtonClickedListener?.invoke(dialogInterfaceWrapper)
        if (description.listCustomized()) {
            return
        }

        onClickListener?.invoke(dialogInterfaceWrapper, condition)
        dismissChecked()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Interface Implementation
    ///////////////////////////////////////////////////////////////////////////

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

    override fun setOnNegativeButtonClickedListener(listener: DialogInterface.() -> Unit) {
        onNegativeButtonClickedListener = listener
    }

}