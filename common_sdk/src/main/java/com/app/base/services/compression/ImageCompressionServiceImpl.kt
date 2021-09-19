package com.app.base.services.compression

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.net.toFile
import com.android.base.utils.common.sizeOf
import com.app.base.app.DispatcherProvider
import com.app.base.config.AppDirectory
import com.app.base.config.AppDirectory.PICTURE_FORMAT_JPEG
import com.app.base.debug.ifOpenDebug
import com.app.base.debug.ifOpenLog
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.Utils
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import kotlin.math.max
import kotlin.math.roundToInt

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-23 15:40
 */
class ImageCompressionServiceImpl(
        private val dispatcherProvider: DispatcherProvider
) : ImageCompressionService {

    override suspend fun compressImages(spec: CompressionSpec, list: List<Uri>): List<Uri> {
        return list.map { doCompressChecked(it, spec) }
    }

    override suspend fun compressImage(spec: CompressionSpec, image: Uri): Uri {
        return doCompressChecked(image, spec)
    }

    private suspend fun doCompressChecked(uri: Uri, spec: CompressionSpec): Uri {
        return withContext(dispatcherProvider.io()) {
            val originFile = uri.toFile()

            if (originFile.sizeOf() <= spec.maxFileSize) {
                uri
            } else {
                val bitmapInfo = BitmapFactory.Options().apply {
                    inJustDecodeBounds = true
                    BitmapFactory.decodeFile(originFile.absolutePath, this)
                }
                if (max(bitmapInfo.outHeight, bitmapInfo.outWidth) <= spec.maxLongSide) {
                    uri
                } else {
                    compress(originFile, spec, bitmapInfo)
                }
            }
        }
    }

    private suspend fun compress(originFile: File, spec: CompressionSpec, bitmapInfo: BitmapFactory.Options): Uri {
        val newFile = Compressor.compress(Utils.getApp(), originFile, Dispatchers.Unconfined) {
            if (spec.maxLongSide > 0) {
                val calcResolution = calcResolution(spec, bitmapInfo)
                ifOpenLog {
                    Timber.d("${bitmapInfo.outWidth},${bitmapInfo.outHeight} setTargetResolution: ${calcResolution.contentToString()}")
                }
                resolution(calcResolution[0], calcResolution[1])
            }
        }

        Timber.d("compress oldFile: $originFile to new File: $newFile")

        ExifUtils.copyExif(originFile.absolutePath, newFile.absolutePath)

        ifOpenDebug {
            FileUtils.copy(newFile.absolutePath, AppDirectory.createTempPicturePath(PICTURE_FORMAT_JPEG))
        }

        return Uri.fromFile(newFile)
    }

    private fun calcResolution(spec: CompressionSpec, bitmapInfo: BitmapFactory.Options): Array<Int> {
        return if (bitmapInfo.outHeight > bitmapInfo.outWidth) {
            val shortSide = (spec.maxLongSide / (bitmapInfo.outHeight * 1.0F / bitmapInfo.outWidth)).roundToInt()
            arrayOf(shortSide, spec.maxLongSide)
        } else {
            val shortSide = (spec.maxLongSide / (bitmapInfo.outWidth * 1.0F / bitmapInfo.outHeight)).roundToInt()
            arrayOf(spec.maxLongSide, shortSide)
        }
    }

}