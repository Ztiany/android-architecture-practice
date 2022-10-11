package com.app.base.utils

/**
 *  - 表示一天的某个时刻：比如 03:30 = 3点30分
 *  - 表示时长：比如 03:30 = 3个小时30分钟
 */
data class Time(val hour: Int = 0, val minute: Int = 0, val second: Int = 0) {

    companion object {
        /** 秒转换为 [Time]*/
        fun fromSeconds(seconds: Int): Time {
            val hour = seconds / 3600
            val minute = (seconds % 3600) / 60
            val second = seconds % 60
            return Time(hour, minute, second)
        }

        /** 分钟转换为 [Time]*/
        fun fromMinutes(minutes: Int): Time {
            val hour = minutes / 60
            val minute = minutes % 60
            return Time(hour, minute, 0)
        }
    }

    fun isZero() = hour == 0 && minute == 0 && second == 0

    fun toSeconds() = hour * 60 * 60 + minute * 60 + second

    /**忽略[second]*/
    fun toMinutes() = hour * 60 + minute

}

/**转换为 24 小时格式的文本：比如 `02:30:00`*/
fun Time.formatTo24BSText(): String {
    return "${hour.to2BitText()}:${minute.to2BitText()}:${second.to2BitText()}"
}