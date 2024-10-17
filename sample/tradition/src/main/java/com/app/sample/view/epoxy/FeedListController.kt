package com.app.sample.view.epoxy

import com.android.base.fragment.list.epoxy.ListEpoxyController

class FeedListController : ListEpoxyController<FeedItem>() {

    override fun buildListModels(data: List<FeedItem>) {
        data.forEach {
            when (it) {
                is BannerVO -> {
                    bindBanner(it)
                }

                is ArticleVO -> {
                    bindArticle(it)
                }
            }
        }
    }

    private fun bindBanner(bannerVO: BannerVO) {
        bannerItemView {
            id(bannerVO.id)
            bannerList(bannerVO.list.map { it.imagePath })
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