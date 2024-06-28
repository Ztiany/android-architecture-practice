package me.ztiany.wan.sample.presentation.mvi

sealed class ArticleIntent {

    data class Init(val pageStart: Int, val pageSize: Int) : ArticleIntent()

    data class More(val pageNo: Int, val pageSize: Int) : ArticleIntent()

    data class Report(val id: Int) : ArticleIntent()

}