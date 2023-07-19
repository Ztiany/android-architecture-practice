package me.ztiany.wan.main.home.feed

import me.ztiany.wan.main.home.data.Article
import me.ztiany.wan.main.home.data.Banner
import javax.inject.Inject

sealed interface FeedItem

data class BannerVO(
    val id: String = "main_banner_vo",
    val list: List<Banner>,
) : FeedItem

data class ArticleVO(
    val id: Int,
    val isTop: Boolean = false,
) : FeedItem

class ArticleVOMapper @Inject constructor() {

    fun mapBanner(bannerList: List<Banner>): FeedItem {
        return BannerVO(list = bannerList)
    }

    fun mapArticle(articleList: List<Article>): List<FeedItem> {
        return articleList.map { ArticleVO(it.id) }
    }

}