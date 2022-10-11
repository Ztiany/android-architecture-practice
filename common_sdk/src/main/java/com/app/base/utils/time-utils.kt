package com.app.base.utils


import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.getOrSet

private const val MICROSECOND = 1
private const val MICROSECOND_IN_SEC = 1000
private const val MICROSECOND_IN_MIN = 60000
private const val MICROSECOND_IN_HOUR = 3600000
private const val MICROSECOND_IN_DAY = 86400000

/**组合日期，默认格式为 `yyyyMMdd` */
fun composeDate(year: Int, month: Int, day: Int, separator: String = ""): String {
    return arrayOf(year.toString(), month.to2BitText(), day.to2BitText()).joinToString(separator)
}

private val sdfThreadLocal = ThreadLocal<MutableMap<String, SimpleDateFormat>>()

fun formatMilliseconds(milliseconds: Long, pattern: String = "yyyy-MM-dd"): String {
    val sdfMap = sdfThreadLocal.getOrSet { mutableMapOf() }
    val sdp = sdfMap.getOrPut(pattern) { SimpleDateFormat(pattern, Locale.getDefault()) }
    val date = Date()
    date.time = milliseconds
    return sdp.format(date)
}

fun formatSeconds(seconds: Int, pattern: String = "yyyy-MM-dd"): String {
    return formatMilliseconds(seconds * 1000L, pattern)
}

fun Int.secondToMillisecond(): Long = this * 1000L

fun getMillisecondYear(mill: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = mill
    return calendar.get(Calendar.YEAR);
}

fun isSameDay(day1: Long, day2: Long): Boolean {
    val cal = Calendar.getInstance()
    cal.timeInMillis = day1
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MINUTE, 0)
    val wee1 = cal.timeInMillis

    cal.timeInMillis = day2
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MINUTE, 0)

    Timber.d("wee1 = $wee1, wee2 = ${cal.timeInMillis}")

    return wee1 == cal.timeInMillis
}

/**转换为至少 2 位的文本，比如 `1` 应该转换为 `01`*/
fun Int.to2BitText(): String {
    if (this < 10 && this > -10) {
        return "0$this"
    }
    return toString()
}

/**获取今天凌晨的时间（生成的时间戳精确到天），用于[isToday], [isYesterday], [isDayBeforeYesterday] 等方法的传值*/
fun getWeeOfToday(): Long {
    return getWeeOfTodayCalendar().timeInMillis
}

/**获取今天凌晨的时间（生成的时间戳精确到天），用于[isToday], [isYesterday], [isDayBeforeYesterday] 等方法的传值*/
fun getWeeOfTodayCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal
}

/**当前时间戳*/
fun timestampMillis() = System.currentTimeMillis()

/**判断是不是今天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于同时进行多个时间判断时减少创建 [Calendar] 对象*/
fun isToday(milliseconds: Long, wee: Long = getWeeOfToday()): Boolean {
    return milliseconds >= wee && milliseconds < wee + MICROSECOND_IN_DAY
}

/**判断是不是后天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于当前时间需要对比多个时间时减少创建 [Calendar] 对象*/
fun isTheDayAfterTomorrow(milliseconds: Long, wee: Long = getWeeOfToday()) = isToday(milliseconds, wee + 2 * MICROSECOND_IN_DAY)

/**判断是不是明天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于当前时间需要对比多个时间时减少创建 [Calendar] 对象*/
fun isTomorrow(milliseconds: Long, wee: Long = getWeeOfToday()) = isToday(milliseconds, wee + MICROSECOND_IN_DAY)

/**判断是不是昨天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于当前时间需要对比多个时间时减少创建 [Calendar] 对象*/
fun isYesterday(milliseconds: Long, wee: Long = getWeeOfToday()) = isToday(milliseconds + MICROSECOND_IN_DAY, wee)

/**判断是不是前天， [wee] 用作对比，应使用 [getWeeOfToday] 来创建，这个参数用于当前时间需要对比多个时间时减少创建 [Calendar] 对象*/
fun isDayBeforeYesterday(milliseconds: Long, wee: Long = getWeeOfToday()) = isToday(milliseconds + MICROSECOND_IN_DAY * 2, wee)

/**判断是不是一分钟以内，[cur] 是用于对比的时间，默认是当前时间戳*/
fun isWithinOneMinute(milliseconds: Long, cur: Long = timestampMillis()) = (cur - milliseconds) / 1000 < 60

/**判断是不是一小时以内，[cur] 是用于对比的时间，默认是当前时间戳*/
fun isWithinOneHour(milliseconds: Long, cur: Long = timestampMillis()) = (cur - milliseconds) / 1000 / 60 < 60

/**判断是不是[hours]小时以内，[cur] 是用于对比的时间，默认是当前时间戳*/
fun isWithinHours(milliseconds: Long, cur: Long = timestampMillis(), hours: Int) = (cur - milliseconds) / 1000 / 60 / 60 < hours

/**返回时间上的分钟差，[cur] 是用于对比的时间，默认是当前时间戳*/
fun minutesDifference(milliseconds: Long, cur: Long? = null) = ((cur ?: timestampMillis()) - milliseconds) / 1000 / 60

/**返回时间上的小时差，[cur] 是用于对比的时间，默认是当前时间戳*/
fun hoursDifference(milliseconds: Long, cur: Long? = null) = ((cur ?: timestampMillis()) - milliseconds) / 1000 / 60 / 60
