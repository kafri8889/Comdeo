package com.anafthdev.comdeo.foundation.common

import android.content.Context
import androidx.work.WorkManager
import com.anafthdev.comdeo.foundation.worker.Workers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID
import javax.inject.Inject

class VideoManager @Inject constructor(
	private val context: Context,
	private val workManager: WorkManager
) {

	private val _currentScanWorkId = MutableStateFlow<UUID?>(null)
	val currentScanWorkId: StateFlow<UUID?> = _currentScanWorkId

	suspend fun scan() {
		workManager.enqueue(
			Workers.getVideoWorker().also {
				_currentScanWorkId.emit(it.id)
			}
		)
	}

}