package me.ztiany.wan.sample.presentation.mvi

import com.android.base.fragment.list.epoxy.ListEpoxyController
import me.ztiany.wan.sample.presentation.epoxy.ArticleVO
import me.ztiany.wan.sample.presentation.epoxy.articleItemView

class MVIListController : ListEpoxyController<ArticleVO>() {

    override fun buildListModels(data: List<ArticleVO>) {
      data.forEach {
          bindArticle(it)
      }
    }

    private fun bindArticle(articleVO: ArticleVO) {
        articleItemView {
            id(articleVO.id)
            author(articleVO.author)
            updateTime(articleVO.updateTime)
            title(articleVO.title)
            category(articleVO.category)
            collected(articleVO.isCollected)
        }
    }

}