package com.app.base.component.compression

import android.net.Uri

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-23 12:21
 */
interface ImageCompressionService {

    /**
     * 压缩策略：
     *
     * 1. 长边不足 maxLongSide，或者图片大小低于 maxFileSize，则不进行压缩。
     * 2. 图片长边固定为：maxLongSide，短边按照原比例确定。
     */
    suspend fun compressImages(spec: CompressionSpec, list: List<Uri>): List<Uri>

    /**
     * 压缩策略：
     *
     * 1. 长边不足 maxLongSide，或者图片大小低于 maxFileSize，则不进行压缩。
     * 2. 图片长边固定为：maxLongSide，短边按照原比例确定。
     */
    suspend fun compressImage(spec: CompressionSpec, image: Uri): Uri

}

class CompressionSpec(
        val maxLongSide: Int,
        val maxFileSize: Long = 0,
)