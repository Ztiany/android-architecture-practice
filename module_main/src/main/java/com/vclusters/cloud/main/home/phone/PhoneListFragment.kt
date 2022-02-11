package com.vclusters.cloud.main.home.phone

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.SimpleRecyclerAdapter
import com.android.base.architecture.fragment.list.BaseListFragment
import com.android.base.architecture.ui.collectFlowOnLifecycle
import com.android.base.architecture.ui.handleListResource
import com.app.base.services.devicemanager.CloudDevice
import com.vclusters.cloud.main.databinding.MainFragmentPhoneListBinding
import com.vclusters.cloud.main.databinding.MainItemPhoneBinding
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.widget.common.dip
import me.ztiany.widget.recyclerview.MarginDecoration

@AndroidEntryPoint
class PhoneListFragment : BaseListFragment<CloudDevice, MainFragmentPhoneListBinding>() {

    private val viewModel by activityViewModels<PhoneViewModel>()

    private val phoneListAdapter by lazy(LazyThreadSafetyMode.NONE) { PhoneListAdapter(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
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

    private fun subscribeViewModel() {
        collectFlowOnLifecycle(data = viewModel.devicesState) {
            handleListResource(it)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        viewModel.loadCloudDevices()
    }

    private class PhoneListAdapter(context: Context) : SimpleRecyclerAdapter<CloudDevice, MainItemPhoneBinding>(context) {

        override fun provideViewBinding(parent: ViewGroup, inflater: LayoutInflater): MainItemPhoneBinding {
            return MainItemPhoneBinding.inflate(inflater, parent, false)
        }

        override fun bindItem(viewHolder: BindingViewHolder<MainItemPhoneBinding>, item: CloudDevice) {
            viewHolder.vb.mainTvPhoneName.text = item.diskName
        }

    }

}