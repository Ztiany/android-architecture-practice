package com.vclusters.cloud.main.home.assistant

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.adapter.newCheckedChangeListener
import com.android.base.adapter.newOnItemClickListener
import com.android.base.architecture.fragment.state.BaseStateFragment
import com.android.base.architecture.ui.collectFlowOnViewLifecycleRepeat
import com.android.base.architecture.ui.handleSateResource
import com.android.base.utils.common.timing
import com.vclusters.cloud.main.databinding.MainFragmentAssistantBinding
import com.vclusters.cloud.main.home.MainNavigator
import com.vclusters.cloud.main.home.common.PhoneViewModel
import dagger.hilt.android.AndroidEntryPoint
import me.ztiany.widget.common.dip
import me.ztiany.widget.recyclerview.MarginDecoration
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class AssistantFragment : BaseStateFragment<MainFragmentAssistantBinding>() {

    private val featureAdapter by lazy(LazyThreadSafetyMode.NONE) {
        AssistantFeatureAdapter(requireContext(), newCallbacks())
    }

    private val assistantFeaturePresenter by lazy(LazyThreadSafetyMode.NONE) {
        AssistantFeaturePresenter(featureAdapter, vb) { phoneId, featureId ->
            false
        }
    }

    private val phoneViewModel by activityViewModels<PhoneViewModel>()
    private val viewModel by viewModels<AssistantViewModel>()

    private val refreshConfigTiming by timing(60.seconds.inWholeMilliseconds)

    @Inject lateinit var mainNavigator: MainNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        assistantFeaturePresenter.recoverStateIfNeed(savedInstanceState)
        subscribeViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        assistantFeaturePresenter.saveInstanceState(outState)
    }

    private fun setUpViews() {
        vb.mainRvAssistantFeatures.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(MarginDecoration(0, 0, 0, dip(10)))
            adapter = featureAdapter
        }
    }

    private fun newCallbacks() = AssistantFeatureCallbacks(
        onClickItemListener = newOnItemClickListener<AssistantFeature> {

        },
        onClickTitleIconListener = newOnItemClickListener<AssistantFeature> {

        },
        onClickSwitchListener = newCheckedChangeListener<AssistantFeature> { buttonView, isChecked, item ->

        }
    )

    private fun subscribeViewModel() {
        collectFlowOnViewLifecycleRepeat(data = phoneViewModel.devicesState) {
            handleSateResource(it, onResult = { devices ->
                assistantFeaturePresenter.showDevices(devices)
            })
        }

        collectFlowOnViewLifecycleRepeat(data = viewModel.assistantFeatureConfigs) {
            assistantFeaturePresenter.showFeatures(it)
        }
    }

    override fun onResume() {
        super.onResume()
        if (refreshConfigTiming) {
            viewModel.loadAssistantFeatureConfigs()
        }
    }

}