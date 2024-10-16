package com.app.sample.compose.ui.request

sealed interface RequestIntent

data object Query : RequestIntent

data class Update(val open: Boolean) : RequestIntent