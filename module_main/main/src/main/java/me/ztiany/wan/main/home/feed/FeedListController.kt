package me.ztiany.wan.main.home.feed

import com.android.base.fragment.epoxy.ListEpoxyController

class FeedListController : ListEpoxyController<FeedItem>() {

    override fun buildListModels(data: List<FeedItem>) {
        data.forEach {
            when (it) {
                is BannerVO -> {
                    bindBanner(it)
                }

                is ArticleVO -> {
                    //TODO
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

}