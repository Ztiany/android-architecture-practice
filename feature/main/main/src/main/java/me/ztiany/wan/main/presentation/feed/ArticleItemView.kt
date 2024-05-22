package me.ztiany.wan.main.presentation.feed

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.AfterPropsSet
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.google.android.material.card.MaterialCardView
import me.ztiany.wan.main.databinding.MainItemArticleBinding

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class ArticleItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null,
) : MaterialCardView(context, attrs) {

    private val vb = MainItemArticleBinding.inflate(LayoutInflater.from(context), this, true)

    @ModelProp lateinit var author: String
    @ModelProp lateinit var title: String
    @ModelProp lateinit var category: String
    @ModelProp lateinit var updateTime: String

    var collected: Boolean = false
        @ModelProp set

    @AfterPropsSet
    fun bindProperties() = with(vb) {
        mainTvAuthor.text = author
        mainTvTitle.text = title
        mainTvCategory.text = category
        mainTvTime.text = updateTime
    }

}