package com.app.base.utils

import android.content.Context
import com.leon.channel.helper.ChannelReaderUtil

/**
 *@author Ztiany
 */
fun getAppChannel(context: Context): String {
    val channel = ChannelReaderUtil.getChannel(context)
    return if (channel.isNullOrEmpty()) "DEV" else channel
}