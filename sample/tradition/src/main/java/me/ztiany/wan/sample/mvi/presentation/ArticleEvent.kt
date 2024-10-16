package me.ztiany.wan.sample.mvi.presentation

sealed interface ArticleEvent {

    sealed interface Report : ArticleEvent {
        data class Result(val reportToast: String) : Report
    }

}