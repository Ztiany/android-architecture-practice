package com.app.performance.launcher

import android.os.Looper
import android.os.MessageQueue.IdleHandler
import java.util.*

/**
 * 延迟初始化器。
 */
class DelayInitDispatcher {

    private val delayTasks: Queue<Runnable> = LinkedList()

    private val idleHandler = IdleHandler {
        if (delayTasks.size > 0) {
            val task = delayTasks.poll()
            task?.run()
        }
        !delayTasks.isEmpty()
    }

    fun addTask(task: Runnable): DelayInitDispatcher {
        delayTasks.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(idleHandler)
    }

}