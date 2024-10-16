package me.ztiany.wan.sample.common.data

data class WanAndroidPager<T>(
    val datas: List<T> = emptyList(),
    val pageCount: Int = 0,
    val total: Int = 0,
    val over: Boolean = false,
    val curPage: Int = 0,
    val offset: Int = 0,
    val size: Int = 0,
)