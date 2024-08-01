package com.app.base.component.uitask

/**
 * UITask 的调度策略。
 */
enum class SchedulePolicy {

    /** 调度策略：只执行一次，不管成功还是失败。 */
    RunOnce,

    /** 调度策略：反复执行直到成功，失败后添加到队尾。 */
    RunTillSucceeded,

    /** 调度策略：反复执行，成功后添加到队尾后续继续执行。 */
    RunRepeatedly,

}