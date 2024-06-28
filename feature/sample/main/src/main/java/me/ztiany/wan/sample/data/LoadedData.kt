package me.ztiany.wan.sample.data


data class LoadedData<T>(
    val data: T,
    val fromRemote: Boolean
)