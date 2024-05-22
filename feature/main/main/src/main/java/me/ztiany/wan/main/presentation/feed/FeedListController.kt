package me.ztiany.wan.main.presentation.feed

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
            author("ztiany")
            updateTime("2021-10-10")
            title("每日一问：Android 12 中的新特性")
            category("Android")
            collected(false)
        }

    }

}