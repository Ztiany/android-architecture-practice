package com.android.sdk.mediaselector.utils

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

///////////////////////////////////////////////////////////////////////////
// internal
///////////////////////////////////////////////////////////////////////////

internal fun File?.makeFilePath(): Boolean {
    if (this == null) {
        return false
    }
    val parent = parentFile ?: return false
    return parent.exists() || parent.mkdirs()
}

internal fun Context.createInternalPath(postfix: String): String {
    var fixedPostfix = postfix
    if (!fixedPostfix.startsWith(".")) {
        fixedPostfix = ".$fixedPostfix"
    }
    val file = File(
        getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        MEDIA_SELECTOR_FOLDER + "/" + tempFileName() + fixedPostfix
    )
    file.makeFilePath()
    return file.absolutePath
}

///////////////////////////////////////////////////////////////////////////
// private
///////////////////////////////////////////////////////////////////////////

private const val MEDIA_SELECTOR_FOLDER = "media-selector"

private val safeSDF = object : ThreadLocal<SimpleDateFormat>() {
    override fun initialValue(): SimpleDateFormat {
        return SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
    }
}

private val simpleDateFormat: SimpleDateFormat
    get() = safeSDF.get() ?: throw IllegalStateException("can't happen!")

/**
 * 统一生成图片的名称格式
 */
private fun tempFileName(): String {
    return simpleDateFormat.format(Date()) + "_" + UUID.randomUUID().toString()
}