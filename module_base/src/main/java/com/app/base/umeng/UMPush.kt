package com.app.base.umeng

import android.app.Activity
import android.app.Application
import android.content.Context
import com.app.base.push.MessageHandler
import com.app.base.push.Push
import com.app.base.push.PushCallBack
import com.app.base.push.PushMessage
import com.umeng.message.IUmengCallback
import com.umeng.message.PushAgent
import com.umeng.message.UmengMessageHandler
import com.umeng.message.UmengNotificationClickHandler
import com.umeng.message.entity.UMessage
import com.umeng.message.tag.TagManager
import timber.log.Timber


/**
 * 友盟推送
 */
class UMPush(private val application: Application) : Push {

    override var messageHandler: MessageHandler? = null

    override fun registerPush(pushCallBack: PushCallBack) {
        if (UmengPushRegister.isRegisteringSuccessfully()) {
            pushCallBack.onRegisterPushSuccess(UmengPushRegister.getToken())
            return
        }

        UmengPushRegister.registerUmengPush(application, object : PushCallBack {
            override fun onRegisterPushSuccess(token: String) {
                pushCallBack.onRegisterPushSuccess(token)
                setMessageHandler()
            }

            override fun onRegisterPushFail() {
                pushCallBack.onRegisterPushFail()
            }
        })
    }

    private fun setMessageHandler() {
        val notificationClickHandler: UmengNotificationClickHandler = object : UmengNotificationClickHandler() {

            override fun dealWithCustomAction(context: Context?, msg: UMessage) {
                messageHandler?.handleOnNotificationMessageClicked(convertToMessage(msg))
            }

            override fun launchApp(context: Context?, msg: UMessage) {
                super.launchApp(context, msg)
                messageHandler?.handleOnLaunchApp(convertToMessage(msg))
            }

        }

        val messageHandler = object : UmengMessageHandler() {
            override fun dealWithCustomMessage(p0: Context?, p1: UMessage) {
                messageHandler?.onDirectMessageArrived(convertToMessage(p1))
            }

            override fun dealWithNotificationMessage(p0: Context?, p1: UMessage) {
                messageHandler?.onNotificationMessageArrived(convertToMessage(p1))
                super.dealWithNotificationMessage(p0, p1)
            }

        }

        PushAgent.getInstance(application).notificationClickHandler = notificationClickHandler
        PushAgent.getInstance(application).messageHandler = messageHandler
    }

    override fun addAlias(alias: String, type: String) {
        PushAgent.getInstance(application).addAlias(alias, type) { success, message ->
            Timber.d("addAlias %s, success = %b, message = %s", alias, success, message)
        }
    }

    override fun setAlias(alias: String, type: String) {
        PushAgent.getInstance(application).setAlias(alias, type) { success, message ->
            Timber.d("setAlias %s, success = %b, message = %s", alias, success, message)
        }
    }

    override fun deleteAlias(alias: String, type: String) {
        PushAgent.getInstance(application).deleteAlias(alias, type) { success, message ->
            Timber.d("deleteAlias %s, success = %b, message = %s", alias, success, message)
        }
    }

    override fun addTag(tag: String) {
        PushAgent.getInstance(application).tagManager.addTags(TagManager.TCallBack { b, result ->
            Timber.d("addTag %s, success = %b, message = %s", tag, b, result.msg)
        }, tag)
    }

    override fun deleteTag(tag: String) {
        PushAgent.getInstance(application).tagManager.deleteTags(TagManager.TCallBack { b, result ->
            Timber.d("deleteTag %s, success = %b, message = %s", tag, b, result.msg)
        }, tag)
    }

    override fun clearTag() {
        Timber.d("not support")
    }

    override fun enablePush() {
        PushAgent.getInstance(application).enable(object : IUmengCallback {
            override fun onSuccess() {
                Timber.d("enablePush onSuccess")
            }

            override fun onFailure(p0: String?, p1: String?) {
                Timber.d("enablePush onFailure")
            }
        })
    }

    override fun disablePush() {
        PushAgent.getInstance(application).disable(object : IUmengCallback {
            override fun onSuccess() {
                Timber.d("disable onSuccess")
            }

            override fun onFailure(p0: String?, p1: String?) {
                Timber.d("disable onSuccess")
            }
        })
    }

    override fun onActivityCreate(activity: Activity) {
        PushAgent.getInstance(application).onAppStart()
    }

    override fun setChannel(channel: String) {
        Timber.d("not support")
    }

    private fun convertToMessage(uMessage: UMessage): PushMessage {
        return UmengUtils.convertToMessage(uMessage)
    }

}