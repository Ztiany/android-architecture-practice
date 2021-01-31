package com.app.base.upgrade

import android.content.Context
import androidx.appcompat.app.AppCompatDialog
import com.app.base.R
import kotlinx.android.synthetic.main.dialog_upgrade_loading.*

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-29 18:31
 */
class UpgradeLoadingDialog(context: Context) : AppCompatDialog(context, R.style.ThemeDialogCommon) {

    init {
        setContentView(R.layout.dialog_upgrade_loading)
    }

    fun notifyProgress(total: Long, progress: Long) {
        if (total == -1L) {
            dialogUpgradePb.isIndeterminate = true
        } else {
            dialogUpgradePb.isIndeterminate = false
            dialogUpgradePb.max = total.toInt()
            dialogUpgradePb.progress = progress.toInt()
        }
    }

}