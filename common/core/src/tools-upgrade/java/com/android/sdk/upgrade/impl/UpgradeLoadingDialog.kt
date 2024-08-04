package com.android.sdk.upgrade.impl

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.android.base.utils.android.views.beVisibleOrInvisible
import com.app.base.databinding.DialogUpgradeLoadingBinding

/**
 *@author Ztiany
 */
internal class UpgradeLoadingDialog(
    context: Context,
    forceUpgrade: Boolean,
) : Dialog(context, com.app.base.ui.theme.R.style.AppTheme_Dialog_FullScreen) {

    private var layout = DialogUpgradeLoadingBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(layout.root)
        layout.dialogUpgradeBackground.beVisibleOrInvisible(!forceUpgrade)
        layout.dialogUpgradeBackground.setOnClickListener {
            dismiss()
        }
    }

    fun notifyProgress(total: Long, progress: Long) {
        if (total == -1L) {
            layout.dialogUpgradePb.isIndeterminate = true
        } else {
            layout.dialogUpgradePb.isIndeterminate = false
            layout.dialogUpgradePb.max = total.toInt()
            layout.dialogUpgradePb.progress = progress.toInt()
        }
    }

}