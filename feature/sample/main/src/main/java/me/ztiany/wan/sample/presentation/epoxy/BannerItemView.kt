package me.ztiany.wan.sample.presentation.epoxy

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import me.ztiany.wan.sample.databinding.SampleItemBannerBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class BannerItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : FrameLayout(context, attrs) {

    private val vb = SampleItemBannerBinding.inflate(LayoutInflater.from(context), this)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //banner size: 900w * 500h
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = (widthSize * 500F / 900F).toInt()
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY))
    }

    @ModelProp
    fun setBannerList(banners: List<String>) {
        vb.mainBanner.setImages(
            banners.map { banner ->
                Uri.parse(banner)
            }, FeedBannerAdapter()
        )
    }

}
