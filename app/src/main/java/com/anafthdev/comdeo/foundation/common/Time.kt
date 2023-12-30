package com.anafthdev.comdeo.foundation.common

import androidx.annotation.IntRange

/**
 * @param num 0 - 23
 * @author kafri8889
 */
fun prependZero(num: Int): String {
	return if (num < 10) "0$num" else num.toString()
}

/**
 * @param num 0 - 23
 * @author kafri8889
 */
fun prependZero(num: Long): String {
	return if (num < 10) "0$num" else num.toString()
}

/**
 * @param hour 0 - 23
 * @return [[hour, hour format]] | [[06, "pm"]]
 * @author kafri8889
 */
fun convert24HourTo12Hour(hour: Int): Pair<String, String> {
	return prependZero(if (hour > 11) hour - 12 else hour) to (if (hour > 12) "pm" else "am")
}

/**
 * @param hour 0 - 11
 * @return hour in 24 hour format (0 - 23)
 * @author kafri8889
 */
fun convert12HourTo24Hour(
	@IntRange(from = 0L, to = 11L) hour: Int,
	hourClockType: String = "am"
): Int {
	return when {
		hourClockType.equals("am", true) -> {
			if (hour > 11) hour - 12
			else hour
		}
		hourClockType.equals("pm", true) -> hour + 12
		else -> hour
	}
}

/**
 * Check if hour is am or pm
 *
 * @param hour 0-23
 * @return true if the hour is am, pm if false
 * @author kafri8889
 */
fun isHourAm(hour: Int): Boolean {
	return hour < 12
}

/**
 * Format [timeInMillis] to duration like "02:23:59"
 */
fun formatDuration(timeInMillis: Long): String {
	return buildString {
		val seconds = (timeInMillis / 1000) % 60
		val minutes = (timeInMillis / (60 * 1000)) % 60
		val hours = (timeInMillis / (60 * 60 * 1000)) % 24
		val days = timeInMillis / (24 * 60 * 60 * 1000)

		var time = "${prependZero(minutes)}:${prependZero(seconds)}"

		if (hours > 0) time = "${prependZero(hours)}:$time"
		if (days > 0) time = "${prependZero(days)}:$time"

		append(time)
	}
}
