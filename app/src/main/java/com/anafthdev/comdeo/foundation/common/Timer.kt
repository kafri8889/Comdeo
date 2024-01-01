package com.anafthdev.comdeo.foundation.common

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

class Timer {

	private val scope = CoroutineScope(Dispatchers.Default + Job())
	private var currentJob: Job? = null

	val hasRunningJob: Boolean
		get() = currentJob != null

	fun postDelayed(
		timeInMillis: Long,
		context: CoroutineContext = Dispatchers.Default,
		block: suspend () -> Unit
	) {
		currentJob = scope.launch(context) {
			delay(timeInMillis)
			block()
		}
	}

	fun postDelayed(
		duration: Duration,
		context: CoroutineContext = Dispatchers.Default,
		block: suspend () -> Unit
	) = postDelayed(duration.inWholeMilliseconds, context, block)

	fun cancel() {
		currentJob?.cancel(CancellationException("User cancelled"))
	}

}