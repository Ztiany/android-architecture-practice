package com.app.base.component.uitask

import com.blankj.utilcode.util.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Ztiany
 */
@Singleton
class UITaskExecutor @Inject constructor() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val taskQueueKit = TaskQueueKit()

    private val taskListener = object : TaskListener {

        override fun onTaskFinished(task: UITask, success: Boolean) {
            scope.launch { finished(task, success) }
        }

        override fun onTaskCanceled(task: UITask) {
            scope.launch { cancelTask(task) }
        }

    }

    init {
        startTaskRunner()
    }

    private fun startTaskRunner() = scope.launch {
        while (isActive) {
            delay(1000)
            if (AppUtils.isAppForeground()) {
                promoteAndExecute()
            }
        }
    }

    private var runningTask: UITask? = null

    /** 执行一个任务 */
    fun cancel(taskId: String) = scope.launch {
        taskQueueKit.findTask(taskId)?.let {
            cancelTask(it)
        }
    }

    /**
     * 如果相同的任务已经在队列中，则取消之前的任务，重新执行新的任务。
     */
    fun enqueue(uiTask: UITask) = scope.launch {
        Timber.d("加入任务：$uiTask")

        val current = runningTask
        if ((current != null && current.id == uiTask.id)) {
            Timber.d("任务已经在执行 runningTask = $runningTask, currentTask = $uiTask")
            current.cancel()
        }

        taskQueueKit.addTask(uiTask)
    }

    private fun finished(uiTask: UITask, success: Boolean) = scope.launch {
        clearTask(uiTask)
        if (uiTask == runningTask) {
            Timber.d("完成任务：$uiTask")
            runningTask = null
            taskQueueKit.finishTask(uiTask, success)
        } else {
            Timber.d("完成任务，匹配错误：uiTask = $uiTask, runningTask = $runningTask")
        }
    }

    private fun clearTask(uiTask: UITask) {
        uiTask.taskListener = null
    }

    private fun cancelTask(uiTask: UITask) {
        Timber.d("取消任务：$uiTask")
        clearTask(uiTask)
        taskQueueKit.cancelTask(uiTask)

        val current = runningTask
        if ((current != null && current.id == uiTask.id)) {
            runningTask = null
        }
    }

    private fun promoteAndExecute() {
        if (runningTask != null) {
            return
        }

        runningTask = taskQueueKit.takeTask()
        runningTask?.taskListener = taskListener

        try {
            runningTask?.onRun()
            if (runningTask != null) {
                Timber.d("执行任务：$runningTask")
            }
        } catch (e: Exception) {
            Timber.e(e, "promoteAndExecute")
            runningTask?.let {
                finished(it, false)
            }
        }
    }

}