package com.android.sdk.mediaselector

import android.content.Intent
import android.os.Bundle

/**
 *@author Ztiany
 */
interface ComponentStateHandler {

    fun onSaveInstanceState(outState: Bundle)

    fun onRestoreInstanceState(outState: Bundle?)

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

}