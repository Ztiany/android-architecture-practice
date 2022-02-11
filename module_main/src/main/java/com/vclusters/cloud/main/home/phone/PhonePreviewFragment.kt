package com.vclusters.cloud.main.home.phone

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.android.base.architecture.fragment.base.BaseUIFragment
import com.android.base.image.ImageLoader
import com.android.base.image.Source
import com.android.base.utils.android.views.dip
import com.app.base.utils.setRoundCornerSize
import com.vclusters.cloud.main.R
import com.vclusters.cloud.main.databinding.MainFragmentPhonePreviewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PhonePreviewFragment : BaseUIFragment<MainFragmentPhonePreviewBinding>() {

    @Inject lateinit var imageLoader: ImageLoader

    override fun onViewPrepared(view: View, savedInstanceState: Bundle?) {
        super.onViewPrepared(view, savedInstanceState)
        vb.mainIvPreview.setRoundCornerSize(dip(20F))
        imageLoader.display(vb.mainIvPreview, Source.create(R.drawable.main_img_rk_default))
    }

    companion object {
        private const val DEVICE_ID = "device_id"

        fun newInstance(id: Int): Fragment {
            return PhonePreviewFragment().also {
                it.arguments = bundleOf(DEVICE_ID to id)
            }
        }
    }

}