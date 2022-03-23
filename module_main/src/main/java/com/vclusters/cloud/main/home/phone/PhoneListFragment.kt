package com.vclusters.cloud.main.home.phone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.adapter.newOnItemClickListener
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.SimpleRecyclerAdapter
import com.android.base.architecture.fragment.list.BaseListFragment
import com.android.base.architecture.ui.collectFlowOnViewLifecycle
import com.android.base.architecture.ui.handleFlowDataWithViewLifecycle
import com.android.base.architecture.ui.handleListResource
import com.android.base.utils.android.text.SpanUtils
import com.android.base.utils.android.views.getColorCompat
import com.android.base.utils.android.views.invisible
import com.android.base.utils.android.views.visible
import com.app.base.services.devicemanager.CloudDevice
import com.app.base.widget.dialog.showConfirmDialog
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.databinding.MainFragmentPhoneListBinding
import com.vclusters.cloud.main.databinding.MainItemPhoneBinding
import com.vclusters.cloud.main.home.common.PhoneViewModel
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.widget.common.dip
import me.ztiany.widget.recyclerview.MarginDecoration

@AndroidEntryPoint
class PhoneListFragment : BaseListFragment<CloudDevice, MainFragmentPhoneListBinding>() {

    private val viewModel by activityViewModels<PhoneViewModel>()

    private val phoneListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PhoneListAdapter(requireContext())
    }

    override fun provideListImplementation(view: View, savedInstanceState: Bundle?) = setUpList(phoneListAdapter, vb.baseListLayout)

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        setUpListener()
        setUpViews()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeViewModel()
    }

    private fun setUpViews() {
        with(vb.baseListLayout) {
            addItemDecoration(MarginDecoration(0, 0, 0, dip(5)))
            layoutManager = LinearLayoutManager(requireContext())
        }

        stateLayoutConfig
            .setStateIcon(EMPTY, R.drawable.main_img_no_device)
            .setStateMessage(EMPTY, "还没有云手机哦~")
            .setStateAction(EMPTY, getString(R.string.refresh))
    }

    private fun setUpListener() {
        phoneListAdapter.onEnterPhoneListener = newOnItemClickListener<CloudDevice> {
            showMessage("进入云手机")
        }
        phoneListAdapter.onRebootPhoneListener = newOnItemClickListener<CloudDevice> {
            doRebootPhone(it)
        }
        phoneListAdapter.onResetPhoneListener = newOnItemClickListener<CloudDevice> {
            doResetPhone(it)
        }
    }

    private fun doRebootPhone(cloudDevice: CloudDevice) {
        showConfirmDialog {
            message = SpanUtils().append(getString(R.string.main_reboot_phone_tips_part1))
                .append(cloudDevice.diskName)
                .setForegroundColor(getColorCompat(R.color.text_link2))
                .append(getString(R.string.main_reboot_phone_tips_part2))
                .create()
            positiveListener = {
                viewModel.rebootCloudDevice(cloudDevice.id)
            }
        }
    }

    private fun doResetPhone(cloudDevice: CloudDevice) {
        showConfirmDialog {
            message = getString(R.string.main_reset_phone_tips)
            positiveListener = {
                viewModel.resetCloudDevice(cloudDevice.id)
            }
        }
    }

    private fun subscribeViewModel() {
        collectFlowOnViewLifecycle(data = viewModel.devicesState) {
            handleListResource(it)
        }

        collectFlowOnViewLifecycle(data = viewModel.rebootCountDown) {
            notifyItem(it)
        }

        collectFlowOnViewLifecycle(data = viewModel.resetCountDown) {
            notifyItem(it)
        }

        handleFlowDataWithViewLifecycle(data = viewModel.rebootDeviceState) {
            loadingMessage = getString(R.string.main_rebooting_tips)
            onData = {
                notifyItem(it)
                showMessage(R.string.main_rebooted_ips)
            }
        }

        handleFlowDataWithViewLifecycle(data = viewModel.resetDeviceState) {
            loadingMessage = getString(R.string.main_resetting_tips)
            onData = {
                notifyItem(it)
                showMessage(R.string.main_reset_success_tips)
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        viewModel.loadCloudDevices()
    }

    private fun notifyItem(phoneId: Int) {
        phoneListAdapter.getList().indexOfFirst {
            it.id == phoneId
        }.let {
            phoneListAdapter.notifyItemChanged(it)
        }
    }

    private inner class PhoneListAdapter(context: Context) : SimpleRecyclerAdapter<CloudDevice, MainItemPhoneBinding>(context) {

        var onRebootPhoneListener: View.OnClickListener? = null
        var onResetPhoneListener: View.OnClickListener? = null
        var onEnterPhoneListener: View.OnClickListener? = null

        override fun provideViewBinding(parent: ViewGroup, inflater: LayoutInflater): MainItemPhoneBinding {
            return MainItemPhoneBinding.inflate(inflater, parent, false)
        }

        override fun bindItem(viewHolder: BindingViewHolder<MainItemPhoneBinding>, item: CloudDevice) = with(viewHolder.vb) {
            root.tag = item
            mainBtnReboot.tag = item
            mainBtnReset.tag = item

            root.setOnClickListener(onEnterPhoneListener)
            mainBtnReboot.setOnClickListener(onRebootPhoneListener)
            mainBtnReset.setOnClickListener(onResetPhoneListener)

            if (viewModel.isDeviceRebooting(item.id)) {
                mainPhoneOperationGroup.invisible()
                mainPhoneOperatingView.visible()
                mainPhoneOperatingView.startLoad()
            } else {
                mainPhoneOperationGroup.visible()
                mainPhoneOperatingView.invisible()
                mainPhoneOperatingView.stopLoad()
            }

            mainTvPhoneName.text = item.diskName
        }

    }

}