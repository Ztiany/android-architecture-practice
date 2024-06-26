package me.ztiany.wan.sample.presentation.mvi

sealed interface ArticleEvent {

    sealed interface Report : ArticleEvent {
        data class Result(val reportToast: String) : Report
    }

}