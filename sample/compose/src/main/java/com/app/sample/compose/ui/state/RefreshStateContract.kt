package com.app.sample.compose.ui.state

internal data class QueryCondition(
    val refreshAction: Int,
    val pageStart: Int,
    val pageSize: Int,
)