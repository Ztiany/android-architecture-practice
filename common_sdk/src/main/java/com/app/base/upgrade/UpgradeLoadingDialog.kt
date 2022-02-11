package com.app.base.upgrade

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import com.app.base.R
import com.app.base.databinding.DialogUpgradeLoadingBinding

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-29 18:31
 */
class UpgradeLoadingDialog(context: Context) : AppCompatDialog(context, R.style.ThemeDialogCommon) {

    private var layout = DialogUpgradeLoadingBinding.inflate(LayoutInflater.from(context))

    init {
        setContentView(layout.root)
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