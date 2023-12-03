package com.anafthdev.comdeo.data.datasource

import android.content.Context
import com.anafthdev.comdeo.data.datasource.local.AppDatabase
import com.anafthdev.comdeo.data.datasource.local.dao.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatasourceModule {

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = AppDatabase.getInstance(context)

    @Provides
    fun provideVideoDao(
        appDatabase: AppDatabase
    ): VideoDao = appDatabase.videoDao()

}