package com.vclusters.cloud.main.home.assistant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.android.base.adapter.recycler.BindingViewHolder
import com.android.base.adapter.recycler.SimpleRecyclerAdapter
import com.android.base.utils.android.views.setRightDrawable
import com.android.base.utils.android.views.visibleOrGone
import com.vclusters.cloud.main.databinding.MainWidgetAssistantItemBinding


class AssistantFeatureCallbacks(
    val onClickItemListener: View.OnClickListener,
    val onClickTitleIconListener: View.OnClickListener,
    val onClickSwitchListener: CompoundButton.OnCheckedChangeListener,
)

class AssistantFeatureAdapter(
    context: Context,
    private val callbacks: AssistantFeatureCallbacks
) : SimpleRecyclerAdapter<AssistantFeature, MainWidgetAssistantItemBinding>(context) {

    override fun provideViewBinding(parent: ViewGroup, inflater: LayoutInflater) = MainWidgetAssistantItemBinding.inflate(inflater, parent, false)

    override fun bindItem(viewHolder: BindingViewHolder<MainWidgetAssistantItemBinding>, item: AssistantFeature) {
        viewHolder.withVB {
            root.tag = item
            mainTvFeatureName.tag = item
            mainSwitchFeature.tag = item
            root.setOnClickListener(callbacks.onClickItemListener)
            mainTvFeatureName.setOnClickListener(callbacks.onClickTitleIconListener)
            mainSwitchFeature.setOnCheckedChangeListener(callbacks.onClickSwitchListener)

            mainIvFeatureIcon.setImageResource(item.icon)
            mainTvFeatureName.text = item.name
            mainTvFeatureName.setRightDrawable(item.nameIcon)
            mainTvFeatureDesc.text = item.desc
            mainTvFeatureDesc.visibleOrGone(item.desc.isNotEmpty())
            mainIvArrow.visibleOrGone(item.hasArrow)
            mainSwitchFeature.visibleOrGone(item.hasSwitch)
        }
    }

}