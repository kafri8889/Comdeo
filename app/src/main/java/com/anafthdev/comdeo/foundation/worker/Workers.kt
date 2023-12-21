package com.anafthdev.comdeo.foundation.worker

import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder

object Workers {

	fun getVideoWorker(): OneTimeWorkRequest {
		return OneTimeWorkRequestBuilder<GetVideoWorker>()
			.build()
	}

}