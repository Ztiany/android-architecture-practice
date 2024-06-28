package me.ztiany.wan.sample

import android.os.Bundle
import android.view.View
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onThrottledClick
import com.app.base.widget.dialog.confirm.showConfirmDialog
import me.ztiany.wan.sample.databinding.SampleFragmentComponentBinding

/**
 * @author Ztiany
 */
class UIComponentFragment : BaseUIFragment<SampleFragmentComponentBinding>() {

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        sampleTvDialogConfirm.onThrottledClick {
            sampleConfirmDialog()
        }
    }

    private fun sampleConfirmDialog() {
        showConfirmDialog {
            message = "我是一个确认框"
            negativeText = "取消"
            positiveText = "确认"
            positiveListener = {

            }
            cancelableTouchOutside = false
        }
    }

}