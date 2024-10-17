package com.app.sample.view.state

internal data class QueryCondition(
    val refreshAction: Int,
    val pageStart: Int,
    val pageSize: Int,
)