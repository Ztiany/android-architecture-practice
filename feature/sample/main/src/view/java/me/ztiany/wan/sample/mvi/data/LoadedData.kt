package me.ztiany.wan.sample.mvi.data


data class LoadedData<T>(
    val data: T,
    val abort: Boolean,
    val remote: Boolean,
)