package me.ztiany.wan.sample.presentation.state

internal data class QueryCondition(
    val refreshAction: Int,
    val pageStart: Int,
    val pageSize: Int,
)