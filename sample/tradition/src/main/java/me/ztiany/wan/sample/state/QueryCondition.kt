package me.ztiany.wan.sample.state

internal data class QueryCondition(
    val refreshAction: Int,
    val pageStart: Int,
    val pageSize: Int,
)