package me.ztiany.wan.sample.mvi.presentation

sealed class ArticleIntent {

    data object Init : ArticleIntent()

    data object More : ArticleIntent()

    data class Report(val id: Int) : ArticleIntent()

}