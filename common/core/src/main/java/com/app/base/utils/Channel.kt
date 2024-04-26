package com.app.base.utils

import android.content.Context

/**
 *@author Ztiany
 */
fun getAppChannel(context: Context): String {
    //val channel = ChannelReaderUtil.getChannel(context)
    //return if (channel.isNullOrEmpty()) "DEV" else channel
    return "DEV"
}