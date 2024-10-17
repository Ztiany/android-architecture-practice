package com.app.sample.view.mvi.data


data class LoadedData<T>(
    val data: T,
    val abort: Boolean,
    val remote: Boolean,
)