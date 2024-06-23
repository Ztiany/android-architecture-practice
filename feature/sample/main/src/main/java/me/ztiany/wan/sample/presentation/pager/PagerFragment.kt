package me.ztiany.wan.sample.presentation.pager

import android.os.Bundle
import android.view.View
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onThrottledClickClick
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.sample.SampleInternalNavigator
import me.ztiany.wan.sample.databinding.SampleFragmentPagerBinding
import javax.inject.Inject

@AndroidEntryPoint
class PagerFragment : BaseUIFragment<SampleFragmentPagerBinding>() {

    @Inject lateinit var internalNavigator: SampleInternalNavigator

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        sampleTvListSegment1.onThrottledClickClick {
            internalNavigator.showSegment1List()
        }

        sampleTvListSegment2.onThrottledClickClick {
            showMessage("没有时间写！")
        }

        sampleTvListEpoxy.onThrottledClickClick {
            internalNavigator.showEpoxyList()
        }

        sampleTvListPaging3.onThrottledClickClick {
            internalNavigator.showPaging3List()
        }
    }

}