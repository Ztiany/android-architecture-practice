package com.android.sdk.mediaselector.processor.crop

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 裁剪配置类。
 */
@Parcelize
data class CropOptions(
    /**裁剪宽度比例  aspectY 组合，如 16:9 */
    val aspectX: Int = 0,
    /** 高度比例与 aspectX 组合，如 16:9 */
    val aspectY: Int = 0,
    /** 输出图片的宽度 */
    val outputX: Int = 0,
    /** 输入图片的高度 */
    val outputY: Int = 0,
) : Parcelable