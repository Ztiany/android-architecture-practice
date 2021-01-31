package com.app.base.utils

import android.content.Context
import com.leon.channel.helper.ChannelReaderUtil

/**
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2020-09-21 11:46
 */
fun getAppChannel(context: Context): String {
    val channel = ChannelReaderUtil.getChannel(context)
    return if (channel.isNullOrEmpty()) "DEV" else channel
}