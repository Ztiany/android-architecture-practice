package me.ztiany.wan.sample.presentation.mvi

sealed class ArticleIntent {

    data object Init : ArticleIntent()

    data object More : ArticleIntent()

    data class Report(val id: Int) : ArticleIntent()

}