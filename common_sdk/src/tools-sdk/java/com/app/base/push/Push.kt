package com.app.base.push

import android.app.Activity

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-03-09 11:31
 */
interface Push {

    fun registerPush(pushCallBack: PushCallBack)

    fun addAlias(alias: String, type: String)
    fun setAlias(alias: String, type: String)
    fun deleteAlias(alias: String, type: String)

    fun addTag(tag: String)
    fun deleteTag(tag: String)
    fun clearTag()

    fun enablePush()
    fun disablePush()

    var messageHandler: MessageHandler?

    fun onActivityCreate(activity: Activity)

    fun setChannel(channel: String)

}