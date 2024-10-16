package com.app.base.debug

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.view.View
import androidx.appcompat.app.AlertDialog.Builder
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.fragment.tool.doFragmentTransaction
import com.app.base.data.storage.StorageManager
import com.app.base.databinding.BaseFragmentDebugMainBinding
import com.app.common.api.usermanager.UserManager
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import org.joor.Reflect
import javax.inject.Inject

@AndroidEntryPoint
internal class DebugFragment : BaseUIFragment<BaseFragmentDebugMainBinding>() {

    @Inject lateinit var userManager: UserManager

    @Inject lateinit var storageManager: StorageManager

    override fun onSetupCreatedView(view: View, savedInstanceState: Bundle?) {
        initToolViews()
        requestNecessaryPermission()
    }

    private fun requestNecessaryPermission() {
        PermissionX.init(this)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request { allGranted, _, _ ->
                if (!allGranted) {
                    showMessage("需要权限")
                    requireActivity().supportFinishAfterTransition()
                }
            }
    }

    private fun doRestart() {
        activity?.startActivity(Intent(requireActivity(), DebugActivity::class.java))
        Process.killProcess(Process.myPid())
    }

    private fun initToolViews() {
        vb.debugSwitch.setOnClickListener { showSwitchTips() }
        vb.debugOpenUeTool.setOnClickListener { openUETool() }
        vb.debugRestart.setOnClickListener { confirmRestart() }
    }

    private fun confirmRestart() {
        Builder(requireContext())
            .setMessage("当前环境的登录状态、缓存数据、快捷登录将会清空，确定要重启吗？")
            .setNegativeButton(com.app.base.ui.theme.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .setPositiveButton(com.app.base.ui.theme.R.string.sure) { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                //清除所有数据
                userManager.logout()
                storageManager.stable().clearAll()
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
            .setNegativeButton(com.app.base.ui.theme.R.string.cancel) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .setPositiveButton("好的") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
                requireActivity().doFragmentTransaction {
                    replaceToStack(fragment = EnvironmentConfigFragment.newInstance(false))
                }
            }.show()
    }

}