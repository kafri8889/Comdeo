package com.anafthdev.comdeo.util

import android.icu.text.DateFormat
import java.util.Locale

object DateUtil {

	fun formatShort(timeInMillis: Long, locale: Locale = Locale.getDefault()): String {
		return DateFormat.getDateInstance(DateFormat.SHORT, locale).format(timeInMillis)
	}

	fun formatMedium(timeInMillis: Long, locale: Locale = Locale.getDefault()): String {
		return DateFormat.getDateInstance(DateFormat.MEDIUM, locale).format(timeInMillis)
	}

}