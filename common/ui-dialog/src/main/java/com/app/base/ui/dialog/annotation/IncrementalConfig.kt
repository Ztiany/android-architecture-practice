package com.app.base.ui.dialog.annotation

/**
 * This annotation is used to mark the configuration method that are incremental.
 * Incremental configuration methods are those that can be extended with new properties
 * without breaking the existing API.
 */
@Retention(AnnotationRetention.SOURCE)
annotation class IncrementalConfig
