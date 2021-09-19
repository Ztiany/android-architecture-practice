package com.app.base.upgrade

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.android.base.app.ui.viewBinding
import com.app.base.R
import com.app.base.databinding.DialogUpgradeLoadingBinding

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-29 18:31
 */
class UpgradeLoadingDialog(context: Context) : AppCompatDialog(context, R.style.ThemeDialogCommon) {

    private val layout by viewBinding(DialogUpgradeLoadingBinding::bind)

    init {
        setContentView(R.layout.dialog_upgrade_loading)
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