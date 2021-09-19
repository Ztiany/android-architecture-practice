package com.app.base.debug

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.appcompat.app.AlertDialog.Builder
import com.android.base.app.fragment.BaseUIFragment
import com.android.base.app.fragment.tools.inFragmentTransaction
import com.android.base.app.ui.viewBinding
import com.android.sdk.permission.AutoPermission
import com.android.sdk.permission.Permission
import com.app.base.R
import com.app.base.R.string
import com.app.base.services.usermanager.AppDataSource
import com.app.base.data.storage.StorageManager
import com.app.base.databinding.BaseFragmentDebugBinding
import com.blankj.utilcode.util.ActivityUtils
import dagger.hilt.android.AndroidEntryPoint
import org.joor.Reflect
import javax.inject.Inject


/**
 * 仅用于调试版本
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-07-26 18:49
 */
@AndroidEntryPoint
class DebugFragment : BaseUIFragment() {

    @Inject lateinit var appDataSource: AppDataSource

    @Inject lateinit var storageManager: StorageManager

    private val layout by viewBinding(BaseFragmentDebugBinding::bind)

    override fun provideLayout() = R.layout.base_fragment_debug

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        initToolViews()
        requestNecessaryPermission()
    }

    private fun requestNecessaryPermission() {
        AutoPermission.with(this)
            .runtime()
            .permission(Permission.WRITE_EXTERNAL_STORAGE)
            .onDenied {
                showMessage("需要权限")
                requireActivity().supportFinishAfterTransition()
            }
            .start()
    }

    private fun doRestart() {
        activity?.startActivity(Intent(requireActivity(), DebugActivity::class.java))
        ActivityUtils.finishAllActivities()
        Process.killProcess(Process.myPid())
    }

    private fun initToolViews() {
        layout.debugSwitch.setOnClickListener { showSwitchTips() }
        layout.debugOpenUeTool.setOnClickListener { openUETool() }
        layout.debugRestart.setOnClickListener { confirmRestart() }
    }

    private fun confirmRestart() {
        Builder(requireContext())
            .setMessage("当前环境的登录状态、缓存数据、快捷登录将会清空，确定要重启吗？")
            .setNegativeButton(string.cancel_) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .setPositiveButton(string.sure) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                //清除所有数据
                appDataSource.logout()
                storageManager.stableStorage().clearAll()
                //重启
                requireActivity().window.decorView.post { doRestart() }
            }.show()
    }

    private var isUEShowing = false

    private fun openUETool() {
        isUEShowing = if (isUEShowing) {
            Reflect.on("me.ele.uetool.UETool").call("dismissUETMenu")
            false
        } else {
            Reflect.on("me.ele.uetool.UETool").call("showUETMenu")
            true
        }
    }

    private fun showSwitchTips() {
        Builder(requireContext())
            .setMessage("切换接口环境后，需要手动重启应用方能生效哦。（H5环境不需要重启）")
            .setNegativeButton(string.cancel_) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .setPositiveButton("好的") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                requireActivity().inFragmentTransaction {
                    replaceToStack(fragment = EnvironmentConfigFragment.newInstance(false))
                }
            }.show()
    }

}