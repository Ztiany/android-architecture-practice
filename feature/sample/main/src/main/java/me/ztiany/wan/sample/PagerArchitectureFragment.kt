package me.ztiany.wan.sample

import android.os.Bundle
import android.view.View
import com.android.base.fragment.base.BaseUIFragment
import com.android.base.utils.android.views.onThrottledClick
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.wan.sample.databinding.SampleFragmentPagerBinding
import javax.inject.Inject

@AndroidEntryPoint
class PagerArchitectureFragment : BaseUIFragment<SampleFragmentPagerBinding>() {

    @Inject lateinit var internalNavigator: SampleInternalNavigator

    override fun onSetUpCreatedView(view: View, savedInstanceState: Bundle?) = withVB {
        sampleTvListSegment1.onThrottledClick {
            internalNavigator.showSegment1List()
        }

        sampleTvListSegment2.onThrottledClick {
            showMessage("TODO")
        }

        sampleTvListEpoxy.onThrottledClick {
            internalNavigator.showEpoxyList()
        }

        sampleTvListEpoxyMvi.onThrottledClick {
            internalNavigator.showEpoxyMVIList()
        }

        sampleTvListPaging3.onThrottledClick {
            internalNavigator.showPaging3List()
        }
    }

}