package com.app.base.services.devicemanager

import android.text.TextUtils
import com.app.base.R

fun CloudDevice.isRK(): Boolean {
    return TextUtils.isEmpty(diskName) || diskName.startsWith("R-云")
}

fun CloudDevice.isRK10(): Boolean {
    return !TextUtils.isEmpty(diskName) && diskName.startsWith("R-10-云")
}

fun CloudDevice.isMTK(): Boolean {
    return !TextUtils.isEmpty(diskName) && diskName.startsWith("M")
}

fun CloudDevice.isQualcomm(): Boolean {
    return !TextUtils.isEmpty(diskName) && diskName.startsWith("Q-")
}

fun CloudDevice.defaultScreenBg(): Int {
    return when {
        isRK() -> {
            R.drawable.img_rk_default;
        }
        isRK10() -> {
            R.drawable.img_rk10_default;
        }
        isQualcomm() -> {
            R.drawable.img_qualcomm_default;
        }
        else -> {
            R.drawable.img_rk_default;
        }
    }
}