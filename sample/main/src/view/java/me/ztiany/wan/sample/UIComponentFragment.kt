package me.ztiany.wan.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onThrottledClick
import com.app.base.ui.dialog.alertDialog
import com.app.base.ui.dialog.dsl.input.autoShowKeyboard
import com.app.base.ui.dialog.dsl.noCancelable
import com.app.base.ui.dialog.inputDialog
import me.ztiany.wan.sample.databinding.SampleFragmentComponentBinding
import me.ztiany.wan.sample.selector.MediaSelectorActivity

/**
 * @author Ztiany
 */
class UIComponentFragment : BaseUIFragment<SampleFragmentComponentBinding>() {

    override fun onSetupCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        sampleTvDialogAlert.onThrottledClick {
            sampleAlertDialog()
        }

        sampleTvDialogInput.onThrottledClick {
            sampleInputDialog()
        }

        sampleTvOpenMediaSelector.onThrottledClick {
            startActivity(Intent(requireContext(), MediaSelectorActivity::class.java))
        }
    }

    private fun sampleInputDialog() {
        inputDialog {
            title("我是标题")
            field {
                hint("请输入内容！")
                lines(2)
                maxLines(4)
                maxLength(20)
            }
            autoShowKeyboard()
            positiveButton("确认") {
                showMessage("确认")
            }
            negativeButton("取消") {
                showMessage("取消")
            }
        }.show()
    }

    private fun sampleAlertDialog() {
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