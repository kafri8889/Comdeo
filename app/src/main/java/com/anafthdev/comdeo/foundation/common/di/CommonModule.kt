package com.anafthdev.comdeo.foundation.common.di

import android.content.Context
import androidx.work.WorkManager
import com.anafthdev.comdeo.foundation.common.VideoManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CommonModule {

	@Provides
	@Singleton
	fun provideVideoManager(
		@ApplicationContext context: Context,
		workManager: WorkManager
	): VideoManager = VideoManager(context, workManager)

}