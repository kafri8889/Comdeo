package com.anafthdev.comdeo.data.repository

import com.anafthdev.comdeo.data.datasource.local.dao.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideVideoRepository(
        videoDao: VideoDao
    ): VideoRepository = VideoRepository(videoDao)

}