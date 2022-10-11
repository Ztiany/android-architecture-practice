package com.android.common.api.services

interface AppServiceFactory {
    fun create(): AppService
}
