package com.app.base.component.uitask

/** 一个任务抽象，在任务执行完毕后，必须调用 [complete] 方法，不同任务的 [id] 必须保证唯一。*/
abstract class UITask(
    val id: String,
    val priority: Int,
    val policy: SchedulePolicy,
) {

    internal var taskListener: TaskListener? = null

    private var cancel = false

    private var finished = false

    protected fun complete(success: Boolean = true) {
        finished = true
        taskListener?.onTaskFinished(this, success)
    }

    fun cancel() {
        cancel = true
        taskListener?.onTaskCanceled(this)
        onCanceled()
    }

    fun isCanceled() = cancel

    fun isFinished() = finished || isCanceled()

    protected open fun onCanceled() {}

    abstract fun onRun()

    override fun toString(): String {
        return "UITask(id='$id', schedulePolicy=${policy}, canceled=$cancel, finished=$finished)"
    }

    companion object {
        const val PRIORITY_HIGHEST = 100000
        const val PRIORITY_HIGH = 10000
        const val PRIORITY_NORMAL = 1000
        const val PRIORITY_LOW = 100
        const val PRIORITY_LOWEST = 1
    }

}