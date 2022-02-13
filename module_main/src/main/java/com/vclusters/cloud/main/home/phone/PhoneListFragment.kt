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
import com.android.base.architecture.ui.collectFlowOnViewLifecycleRepeat
import com.android.base.architecture.ui.handleFlowDataWithViewLifecycle
import com.android.base.architecture.ui.handleListResource
import com.android.base.utils.android.views.invisible
import com.android.base.utils.android.views.visible
import com.app.base.services.devicemanager.CloudDevice
import com.vclusters.cloud.main.databinding.MainFragmentPhoneListBinding
import com.vclusters.cloud.main.databinding.MainItemPhoneBinding
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.widget.common.dip
import me.ztiany.widget.recyclerview.MarginDecoration

@AndroidEntryPoint
class PhoneListFragment : BaseListFragment<CloudDevice, MainFragmentPhoneListBinding>() {

    private val viewModel by activityViewModels<PhoneViewModel>()

    private val phoneListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PhoneListAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        setUpListener()
        setUpViews()
    }

    private fun setUpViews() {
        dataManager = phoneListAdapter
        with(vb.baseListLayout) {
            addItemDecoration(MarginDecoration(0, 0, 0, dip(5)))
            layoutManager = LinearLayoutManager(requireContext())
            adapter = phoneListAdapter
        }
    }

    private fun setUpListener() {
        phoneListAdapter.onEnterPhoneListener = newOnItemClickListener<CloudDevice> {
            showMessage("进入云手机")
        }
        phoneListAdapter.onRebootPhoneListener = newOnItemClickListener<CloudDevice> {
            doRebootPhone(it.cardId)
        }
        phoneListAdapter.onResetPhoneListener = newOnItemClickListener<CloudDevice> {
            doResetPhone(it.cardId)
        }
    }

    private fun doRebootPhone(cardId: Int) {
        viewModel.rebootCloudDevice(cardId)
    }

    private fun doResetPhone(cardId: Int) {
        viewModel.resetCloudDevice(cardId)
    }

    private fun subscribeViewModel() {
        collectFlowOnViewLifecycleRepeat(data = viewModel.devicesState) {
            handleListResource(it)
        }

        collectFlowOnViewLifecycleRepeat(data = viewModel.rebootCountDown) {
            notifyItem(it)
        }

        collectFlowOnViewLifecycleRepeat(data = viewModel.resetCountDown) {
            notifyItem(it)
        }

        handleFlowDataWithViewLifecycle(data = viewModel.rebootDeviceState) {
            onData = {
                notifyItem(it)
            }
        }

        handleFlowDataWithViewLifecycle(data = viewModel.resetDeviceState) {
            onData = {
                notifyItem(it)
            }
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        viewModel.loadCloudDevices()
    }

    private fun notifyItem(cardId: Int) {
        phoneListAdapter.getList().indexOfFirst {
            it.cardId == cardId
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

            if (viewModel.isDeviceRebooting(item.cardId)) {
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