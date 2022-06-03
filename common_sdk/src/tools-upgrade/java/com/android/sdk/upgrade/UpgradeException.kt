package com.android.sdk.upgrade

/**
 *@author Ztiany
 */
class UpgradeException(val type: Int) : Exception() {

    companion object {
        const val NETWORK_ERROR = 1
        const val DIGITS_COMPARING_ERROR = 2
    }

}
