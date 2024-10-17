package com.app.sample.view.mvi.presentation

sealed interface ArticleEvent {

    sealed interface Report : ArticleEvent {
        data class Result(val reportToast: String) : Report
    }

}

sealed class ArticleIntent {

    data object Init : ArticleIntent()

    data object More : ArticleIntent()

    data class Report(val id: Int) : ArticleIntent()

}