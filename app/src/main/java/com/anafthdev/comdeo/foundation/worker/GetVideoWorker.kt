package com.anafthdev.comdeo.foundation.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.anafthdev.comdeo.data.repository.VideoRepository
import com.anafthdev.comdeo.util.VideoUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class GetVideoWorker @AssistedInject constructor(
	@Assisted private val context: Context,
	@Assisted params: WorkerParameters,
	private val videoRepository: VideoRepository
): CoroutineWorker(context, params) {

	override suspend fun doWork(): Result {
		return try {
			videoRepository.insert(VideoUtil.findAllVideo(context))
		    Result.success()
		} catch (e: Exception) {
			Timber.e(e, e.message)
			Result.failure()
		}
	}

}