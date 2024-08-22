package com.app.base.ui.dialog.impl.list

import android.content.Context
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.utils.android.views.beGone
import com.android.base.utils.android.views.onThrottledClick
import com.android.base.utils.common.ifNonNull
import com.android.base.utils.common.otherwise
import com.android.base.utils.common.unsafeLazy
import com.app.base.ui.dialog.AppBaseDialog
import com.app.base.ui.dialog.databinding.DialogLayoutListBinding
import com.app.base.ui.dialog.dsl.Condition
import com.app.base.ui.dialog.dsl.DisplayItem
import com.app.base.ui.dialog.dsl.applyTo
import com.app.base.ui.dialog.dsl.applyToDialog
import com.app.base.ui.dialog.dsl.list.ListDialogDescription
import com.app.base.ui.dialog.dsl.list.ListDialogInterface
import com.app.base.ui.dialog.impl.DialogInterfaceWrapper
import com.app.base.ui.dialog.impl.bottomsheet.ListDialogAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration

internal class AppListDialog(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    private val description: ListDialogDescription,
    style: Int = com.app.base.ui.theme.R.style.AppTheme_Dialog_Common_Transparent_Floating,
) : AppBaseDialog(context, description.size, style = style), ListDialogInterface {

    private val vb = DialogLayoutListBinding.inflate(LayoutInflater.from(context))

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
        if (description.positiveButton == null && description.negativeButton == null) {
            dialogBottom.beGone()
            return
        }

        with(description.positiveButton) {
            ifNonNull {
                textDescription.applyTo(dialogBottom.tvPositive)
                dialogBottom.tvPositive.onThrottledClick {
                    onClickListener?.invoke(dialogInterfaceWrapper, condition)
                    dismissChecked()
                }
            } otherwise {
                dialogBottom.hidePositiveButton()
            }
        }

        with(description.negativeButton) {
            ifNonNull {
                textDescription.applyTo(dialogBottom.tvNegative)
                dialogBottom.tvNegative.onThrottledClick {
                    onClickListener?.invoke(dialogInterfaceWrapper, condition)
                    dismissChecked()
                }
            } otherwise { dialogBottom.hideNegativeButton() }
        }

        dialogBottom.hideNeutralButton()
    }

    private fun setUpListDecor() = with(description) {
        listTopAreaConfig?.invoke(this@AppListDialog, (vb.dialogStubListTopDecorator.inflate() as ConstraintLayout))
        listBottomAreaConfig?.invoke(this@AppListDialog, (vb.dialogStubListBottomDecorator.inflate() as ConstraintLayout))
    }

    private fun setBehavior() = with(description.behavior) {
        applyToDialog(this@AppListDialog)
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
            customizeList.invoke(this@AppListDialog, vb.dialogRvList)
            return@with
        }
        val listDescription = listDescription ?: return

        with(vb.dialogRvList) {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(MaterialDividerItemDecoration(context, com.android.base.ui.recyclerview.DividerItemDecoration.VERTICAL).apply {
                dividerColor = listDescription.dividerColor
                dividerThickness = listDescription.dividerThickness
                dividerInsetStart = listDescription.dividerInsetStart
                dividerInsetEnd = listDescription.dividerInsetEnd
                isLastItemDecorated = false
            })
            adapter = ListDialogAdapter(
                context,
                listDescription
            ) { position: Int, item: DisplayItem ->
                listDescription.itemClickListener?.invoke(dialogInterfaceWrapper, position, item)
                dismissChecked()
            }
        }
    }

    private fun dismissChecked() {
        if (isShowing && description.behavior.shouldAutoDismiss) {
            dialogInterfaceWrapper.dismiss()
        }
    }

    override fun updateTitle(title: CharSequence) {
        vb.dialogTvTitle.text = title
    }

    override fun updateItems(list: List<DisplayItem>) {
        listAdapter.replaceAll(list)
    }

    override var isPositiveButtonEnable: Boolean
        get() = vb.dialogBottom.tvPositive.isEnabled
        set(value) {
            vb.dialogBottom.tvPositive.isEnabled = value
        }

}