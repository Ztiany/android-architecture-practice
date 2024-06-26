package me.ztiany.wan.sample.data

// 新闻流包装类
data class ArticleFlowWrapper(
    val news: List<Article>,
    val abort: Boolean
)