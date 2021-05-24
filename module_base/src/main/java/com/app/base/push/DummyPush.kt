package com.app.base.push

import android.app.Activity

class DummyPush(override var messageHandler: MessageHandler?=null) :Push {

    override fun registerPush(pushCallBack: PushCallBack) {
    }

    override fun addAlias(alias: String, type: String) {
    }

    override fun setAlias(alias: String, type: String) {
    }

    override fun deleteAlias(alias: String, type: String) {
    }

    override fun addTag(tag: String) {
    }

    override fun deleteTag(tag: String) {
    }

    override fun clearTag() {
    }

    override fun enablePush() {
    }

    override fun disablePush() {
    }

    override fun onActivityCreate(activity: Activity) {
    }

    override fun setChannel(channel: String) {
    }

}