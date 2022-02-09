package com.vclusters.cloud.main.home.phone

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.android.base.adapter.pager.ViewPagerFragmentAdapter
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.architecture.ui.handleFlowData
import com.app.base.services.devicemanager.CloudDevice
import com.qmuiteam.qmui.kotlin.dip
import com.vclusters.cloud.main.databinding.MainFragmentPhonePreviewsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhonePreviewsFragment : BaseUIFragment<MainFragmentPhonePreviewsBinding>() {

    private val viewModel by viewModels<PhonePreviewsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        vb.mainVpPreviews.offscreenPageLimit = 3
        vb.mainVpPreviews.pageMargin = dip(30)
    }

    private fun subscribeViewModel() {
        viewModel.announcement.observe(this) {
            vb.mainAnnouncementView.setAnnouncements(it)
        }

        handleFlowData(data = viewModel.devicesState) {
            onLoading = {
                vb.mainMsv.showLoadingLayout()
            }
            onError = {
                vb.mainMsv.showErrorLayout()
            }
            onSuccess = {
                if (it.isNullOrEmpty()) {
                    vb.mainMsv.showEmptyLayout()
                } else {
                    showDevices(it)
                    vb.mainMsv.showContentLayout()
                }
            }
        }
    }

    private fun showDevices(devices: List<CloudDevice>) {
        val pagerAdapter = ViewPagerFragmentAdapter(childFragmentManager, requireContext())
        pagerAdapter.setDataSource(devices.map {
            PhonePreviewFragment.newPageInfo(it.id)
        })
        vb.mainVpPreviews.adapter = pagerAdapter
    }

}