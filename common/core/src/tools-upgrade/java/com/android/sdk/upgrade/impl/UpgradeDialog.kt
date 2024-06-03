package com.android.sdk.upgrade.impl

import android.content.Context
import android.view.LayoutInflater
import com.app.base.R
import com.app.base.databinding.DialogUpgradeContentBinding
import com.app.base.widget.dialog.base.AppBaseDialog

internal class UpgradeDialog(
    context: Context,
    forceUpgrade: Boolean,
    versionNumber: String,
    versionIntroduction: String
) : AppBaseDialog(context) {

    private var layout = DialogUpgradeContentBinding.inflate(LayoutInflater.from(context))

    var negativeListener: (() -> Unit)? = null

    var positiveListener: (() -> Unit)? = null

    override val maxDialogWidthPercent = 0.9F

    init {
        setContentView(layout.root)

        setCanceledOnTouchOutside(false)
        setCancelable(!forceUpgrade)

        layout.upgradeBottomLayout.onNegativeClick {
            negativeListener?.invoke()
            dismiss()
        }
        layout.upgradeBottomLayout.onPositiveClick {
            positiveListener?.invoke()
            dismiss()
        }
        if (forceUpgrade) {
            layout.upgradeBottomLayout.hideNegative()
        }
        layout.upgradeTvContent.text = versionIntroduction
        layout.upgradeTvVersion.text = context.getString(R.string.upgrade_version, versionNumber)
    }

}