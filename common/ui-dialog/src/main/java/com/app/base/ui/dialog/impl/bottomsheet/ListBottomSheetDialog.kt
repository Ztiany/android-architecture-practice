package com.app.base.ui.dialog.impl.bottomsheet

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.ui.recyclerview.DividerItemDecoration
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.android.views.setHorizontalPadding
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.dialog.databinding.DialogLayoutBottomsheetListBinding
import com.app.base.ui.dialog.dsl.Condition
import com.app.base.ui.dialog.dsl.DisplayItem
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.bottomsheet.ListBottomSheetDialogDescription
import com.app.base.ui.dialog.dsl.bottomsheet.ListBottomSheetDialogInterface
import com.app.base.ui.dialog.dsl.bottomsheet.applyToDialog
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper
import com.google.android.material.divider.MaterialDividerItemDecoration

internal class ListBottomSheetDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val description: ListBottomSheetDialogDescription,
) : AppBottomSheetDialog(context, description.size), ListBottomSheetDialogInterface {

    private val vb = DialogLayoutBottomsheetListBinding.inflate(LayoutInflater.from(context))

    private val condition by unsafeLazy { object : Condition {} }

    private val dialogInterfaceWrapper by unsafeLazy {
        DialogInterfaceWrapper(this)
    }

    private val listAdapter by unsafeLazy {
        val listDescription = description.listDescription ?: throw IllegalStateException("listDescription is null")
        ListDialogAdapter(
            context,
            listDescription
        ) { position: Int, item: DisplayItem ->
            listDescription.itemClickListener?.invoke(dialogInterfaceWrapper, position, item)
            dismissChecked()
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
        description.bottomButton.ifNonNull {
            dialogSpaceLeft.beGone()
            dialogBtnBottomLeft.beGone()
            textDescription.applyTo(dialogBtnBottomRight)
            dialogBtnBottomRight.onThrottledClick {
                onClickListener?.invoke(dialogInterfaceWrapper, condition)
                dismissChecked()
            }
        } otherwise { dialogLlBottomButtons.beGone() }
    }

    private fun setUpListDecor() = with(description) {
        listTopAreaConfig?.invoke(this@ListBottomSheetDialog, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
        listBottomAreaConfig?.invoke(this@ListBottomSheetDialog, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
    }

    private fun setBehavior() = with(description.behavior) {
        applyToDialog(this@ListBottomSheetDialog)
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

    private fun setupList() = with(description) {
        if (customizeList != null) {
            customizeList.invoke(this@ListBottomSheetDialog, vb.dialogRvList)
            return@with
        }
        val listDescription = listDescription ?: return

        with(vb.dialogRvList) {
            setHorizontalPadding(context.resources.getDimensionPixelSize(com.app.base.ui.theme.R.dimen.common_page_edge))
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MaterialDividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
                dividerColor = listDescription.dividerColor
                dividerThickness = listDescription.dividerThickness
                dividerInsetStart = listDescription.dividerInsetStart
                dividerInsetEnd = listDescription.dividerInsetEnd
            })
            adapter = listAdapter
        }
    }

    private fun dismissChecked() {
        if (isShowing && description.behavior.shouldAutoDismiss) {
            dialogInterfaceWrapper.dismiss()
        }
    }

    override var isBottomActionButtonEnable: Boolean
        get() = vb.dialogBtnBottomRight.isEnabled
        set(value) {
            vb.dialogBtnBottomRight.isEnabled = value
        }

    override fun updateTitle(title: CharSequence) {
        vb.dialogTvTitle.text = title
    }

    override fun updateItems(list: List<DisplayItem>) {
        listAdapter.replaceAll(list)
    }

}