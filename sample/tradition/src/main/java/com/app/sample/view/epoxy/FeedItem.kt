package com.app.sample.view.epoxy

import com.android.base.fragment.list.paging3.IntIdentity
import com.app.sample.view.common.data.Banner

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
