package me.ztiany.wan.sample.presentation.mvi

sealed class ArticleIntent {

    data class Init(val count: Int = 20) : ArticleIntent()

    data class More(val count: Int = 20) : ArticleIntent()

    data class Report(val id: Int) : ArticleIntent()

}