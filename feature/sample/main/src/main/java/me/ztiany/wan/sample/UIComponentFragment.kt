package me.ztiany.wan.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onThrottledClick
import com.app.base.ui.dialog.alertDialog
import com.app.base.ui.dialog.dsl.noCancelable
import me.ztiany.wan.sample.databinding.SampleFragmentComponentBinding
import me.ztiany.wan.sample.selector.MediaSelectorActivity

/**
 * @author Ztiany
 */
class UIComponentFragment : BaseUIFragment<SampleFragmentComponentBinding>() {

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        sampleTvDialogConfirm.onThrottledClick {
            sampleConfirmDialog()
        }

        sampleTvOpenMediaSelector.onThrottledClick {
            startActivity(Intent(requireContext(), MediaSelectorActivity::class.java))
        }
    }

    private fun sampleConfirmDialog() {
        alertDialog {
            message("我是一个确认框")
            positiveButton("确认") {
                showMessage("确认")
            }
            negativeButton("取消") {
                showMessage("取消")
            }
            noCancelable()
        }.show()
    }

}