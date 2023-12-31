package com.anafthdev.comdeo.data.repository.di

import androidx.datastore.core.DataStore
import com.anafthdev.comdeo.ProtoUserPreference
import com.anafthdev.comdeo.data.datasource.local.dao.VideoDao
import com.anafthdev.comdeo.data.repository.UserPreferenceRepository
import com.anafthdev.comdeo.data.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

	@Provides
	@Singleton
	fun provideVideoRepository(
		videoDao: VideoDao
	): VideoRepository = VideoRepository(videoDao)

	@Provides
	@Singleton
	fun provideUserPreferenceRepository(
		userPreferenceDatastore: DataStore<ProtoUserPreference>
	): UserPreferenceRepository = UserPreferenceRepository(userPreferenceDatastore)

}