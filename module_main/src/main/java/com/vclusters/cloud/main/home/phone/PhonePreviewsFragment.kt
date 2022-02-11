package com.vclusters.cloud.main.home.phone

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.android.base.adapter.pager2.SimpleFragmentStateAdapter
import com.android.base.architecture.fragment.state.BaseStateFragment
import com.android.base.architecture.ui.collectFlowOnLifecycle
import com.android.base.architecture.ui.handleSateResource
import com.android.base.utils.android.views.gone
import com.android.base.utils.android.views.recyclerView
import com.android.base.utils.android.views.visible
import com.android.base.utils.common.timing
import com.app.base.services.devicemanager.CloudDevice
import com.qmuiteam.qmui.kotlin.dip
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.databinding.MainFragmentPhonePreviewsBinding
import com.vclusters.cloud.main.home.phone.widget.FadeInTransformer
import dagger.hilt.android.AndroidEntryPoint
import kotlin.time.Duration.Companion.minutes

@AndroidEntryPoint
class PhonePreviewsFragment : BaseStateFragment<MainFragmentPhonePreviewsBinding>() {

    private val phoneViewModel by activityViewModels<PhoneViewModel>()

    private val viewModel by viewModels<PhonePreviewsViewModel>()

    private val refreshAnnouncementTiming by timing(5.minutes.inWholeMilliseconds)

    private lateinit var pagerAdapter: SimpleFragmentStateAdapter<CloudDevice>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeViewModel()
    }

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        setUpViews()

        stateLayoutConfig
            .setStateIcon(EMPTY, R.drawable.main_img_no_device)
            .setStateMessage(EMPTY, "还没有云手机哦~")
    }

    private fun setUpViews() {
        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(FadeInTransformer())
            addTransformer(MarginPageTransformer(dip(30)))
        }

        pagerAdapter = SimpleFragmentStateAdapter(this,
            itemIdProvider = {
                it.id.toLong()
            },
            fragmentFactory = { _, cloudDevice ->
                PhonePreviewFragment.newInstance(cloudDevice.id)
            })

        with(vb.mainVpPreviews) {
            setPageTransformer(compositePageTransformer)
            adapter = pagerAdapter
            recyclerView()?.overScrollMode = View.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    setPagerIndicatorVisibility()
                }
            })
        }
    }


    private fun subscribeViewModel() {
        viewModel.announcement.observe(this) {
            vb.mainAnnouncementView.setAnnouncements(it)
        }

        collectFlowOnLifecycle(data = phoneViewModel.devicesState) {
            handleSateResource(it, onResult = { devices ->
                showDevices(devices)
            })
        }
    }

    private fun showDevices(devices: List<CloudDevice>) {
        pagerAdapter.replaceAll(devices)
        setPagerIndicatorVisibility()
    }

    private fun setPagerIndicatorVisibility() {
        if (pagerAdapter.getDataSize() == 1) {
            gone(vb.mainIvPhoneLeftIndicator, vb.mainIvPhoneRightIndicator)
        } else if (pagerAdapter.getDataSize() == 2) {
            if (vb.mainVpPreviews.currentItem == 0) {
                vb.mainIvPhoneRightIndicator.visible()
                vb.mainIvPhoneLeftIndicator.gone()
            } else {
                vb.mainIvPhoneLeftIndicator.visible()
                vb.mainIvPhoneRightIndicator.gone()
            }
        } else {
            when (vb.mainVpPreviews.currentItem) {
                0 -> {
                    vb.mainIvPhoneRightIndicator.visible()
                    vb.mainIvPhoneLeftIndicator.visible()
                }
                pagerAdapter.getDataSize() - 1 -> {
                    vb.mainIvPhoneLeftIndicator.visible()
                    vb.mainIvPhoneRightIndicator.gone()
                }
                else -> {
                    visible(vb.mainIvPhoneLeftIndicator, vb.mainIvPhoneRightIndicator)
                }
            }
        }
    }

    override fun onRetry(state: Int) {
        phoneViewModel.loadCloudDevices()
    }

    override fun onResume() {
        super.onResume()
        if (refreshAnnouncementTiming) {
            viewModel.loadHomeAnnouncements()
        }
    }

}