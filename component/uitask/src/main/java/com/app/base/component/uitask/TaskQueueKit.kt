package com.app.base.component.uitask

import timber.log.Timber
import java.util.PriorityQueue

internal class TaskQueueKit {

    private val comparator = Comparator<UITask> { o1, o2 ->
        if (o1.priority == o2.priority) {
            o1.id.compareTo(o2.id)
        } else {
            o2.priority - o1.priority
        }
    }

    private var shouldSwap = false

    private var readyTaskQueue = PriorityQueue(8, comparator)

    private var nextRoundQueue = PriorityQueue(8, comparator)

    fun takeTask(): UITask? {
        if (readyTaskQueue.isEmpty()) {
            return swapQueueAndTake()
        }

        while (!readyTaskQueue.isEmpty()) {
            val task = readyTaskQueue.poll()
            if (task != null && !task.isCanceled()) {
                return task
            }
        }

        return swapQueueAndTake()
    }

    private fun swapQueueAndTake(): UITask? {
        if (shouldSwap) {
            shouldSwap = false
            val temp = readyTaskQueue
            readyTaskQueue = nextRoundQueue
            nextRoundQueue = temp
            return takeTask()
        }
        return null
    }

    fun addTask(task: UITask) {
        addToQueue(task, readyTaskQueue)
    }

    private fun addToQueue(task: UITask, taskQueue: PriorityQueue<UITask>) {
        taskQueue.removeAll { it.id == task.id }
        taskQueue.add(task)
    }

    fun finishTask(task: UITask, succeeded: Boolean) {
        Timber.d("完成任务：$task, succeeded = $succeeded")
        if (!shouldReAddToQueue(task, succeeded)) {
            Timber.d("任务不需要重新添加到队列：$task")
            return
        }
        addToQueue(task, readyTaskQueue)
        shouldSwap = true
    }

    private fun shouldReAddToQueue(action: UITask, success: Boolean): Boolean {
        return !action.isCanceled() &&
                (SchedulePolicy.RunRepeatedly == action.policy
                        || (!success && action.policy == SchedulePolicy.RunTillSucceeded))
    }

    fun cancelTask(action: UITask) {
        readyTaskQueue.removeAll { it.id == action.id }
        nextRoundQueue.removeAll { it.id == action.id }
    }

    fun findTask(taskId: String): UITask? {
        readyTaskQueue.forEach {
            if (it.id == taskId) {
                return it
            }
        }

        nextRoundQueue.find { it.id == taskId }?.let {
            if (it.id == taskId) {
                return it
            }
        }
        return null
    }

}