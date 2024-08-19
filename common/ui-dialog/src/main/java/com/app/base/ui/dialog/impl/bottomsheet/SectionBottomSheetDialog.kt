package com.app.base.ui.dialog.impl.bottomsheet

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.ui.recyclerview.DividerItemDecoration
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.android.views.setHorizontalPadding
import com.android.base.utils.common.findWithIndex
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.dialog.databinding.DialogLayoutBottomsheetListBinding
import com.app.base.ui.dialog.dsl.Selection
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.bottomsheet.MultiSectionBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.MultiSelectionBottomSheetDialogInterface
import com.app.base.ui.dialog.dsl.bottomsheet.SectionBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.SingleSectionBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.SingleSelectionBottomSheetDialogInterface
import com.app.base.ui.dialog.dsl.bottomsheet.applyToDialog
import com.app.base.ui.dialog.dsl.bottomsheet.listCustomized
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper
import com.google.android.material.divider.MaterialDividerItemDecoration

internal class SectionBottomSheetDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val description: SectionBottomSheetDialogDescription,
) : AppBottomSheetDialog(context, description.size), SingleSelectionBottomSheetDialogInterface, MultiSelectionBottomSheetDialogInterface {

    private val vb = DialogLayoutBottomsheetListBinding.inflate(LayoutInflater.from(context))

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

    private inline fun SectionBottomSheetDialogDescription.discriminate(
        single: SingleSectionBottomSheetDialogDescription.() -> Unit,
        multi: MultiSectionBottomSheetDialogDescription.() -> Unit,
    ) {
        when (this) {
            is SingleSectionBottomSheetDialogDescription -> single()
            is MultiSectionBottomSheetDialogDescription -> multi()
        }
    }

    init {
        setContentView(vb.root)

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
            listTopAreaConfig?.invoke(this@SectionBottomSheetDialog, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
            listBottomAreaConfig?.invoke(this@SectionBottomSheetDialog, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
        },
        multi = {
            listTopAreaConfig?.invoke(this@SectionBottomSheetDialog, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
            listBottomAreaConfig?.invoke(this@SectionBottomSheetDialog, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
        }
    )

    private fun setBehavior() = with(description.behavior) {
        applyToDialog(this@SectionBottomSheetDialog)
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
                    it.invoke(this@SectionBottomSheetDialog, vb.dialogRvList)
                    return
                }
            },
            multi = {
                customizeList?.let {
                    it.invoke(this@SectionBottomSheetDialog, vb.dialogRvList)
                    return
                }
            }
        )

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

}