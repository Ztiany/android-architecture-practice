package me.ztiany.wan.sample.presentation.epoxy

import com.android.base.fragment.list.paging3.IntIdentity
import me.ztiany.wan.sample.data.Article
import me.ztiany.wan.sample.data.Banner
import javax.inject.Inject

sealed interface FeedItem

data class BannerVO(
    val id: String = "main_banner_vo",
    val list: List<Banner>,
) : FeedItem

data class ArticleVO(
    override val id: Int,
    val isTop: Boolean = false,
    val isCollected: Boolean = false,
    val author: String,
    val title: String,
    val url: String,
    val category: String,
    val updateTime: String,
) : FeedItem, IntIdentity

class ArticleVOMapper @Inject constructor() {

    fun mapBanner(bannerList: List<Banner>): BannerVO {
        return BannerVO(list = bannerList)
    }

    fun mapArticles(articleList: List<Article>): List<ArticleVO> {
        return articleList.map { mapArticle(it) }
    }

    fun mapArticle(article: Article): ArticleVO {
        return ArticleVO(
            article.id,
            author = article.author,
            title = article.title,
            url = article.link,
            category = "${article.superChapterName}:${article.chapterName}",
            updateTime = article.niceDate
        )
    }

}