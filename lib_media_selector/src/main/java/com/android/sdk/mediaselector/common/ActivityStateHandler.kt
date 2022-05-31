package com.android.sdk.mediaselector.common

import android.content.Intent
import android.os.Bundle

/**
 *@author Ztiany
 */
interface ActivityStateHandler {

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun onSaveInstanceState(outState: Bundle)

    fun onRestoreInstanceState(outState: Bundle?)

}