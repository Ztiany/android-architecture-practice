package com.app.base.component.uitask

interface TaskListener {

    fun onTaskFinished(task: UITask, success: Boolean)

    fun onTaskCanceled(task: UITask)

}