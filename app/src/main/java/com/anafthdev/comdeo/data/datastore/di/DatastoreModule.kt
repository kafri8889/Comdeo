package com.anafthdev.comdeo.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.anafthdev.comdeo.ProtoUserPreference
import com.anafthdev.comdeo.data.Constant
import com.anafthdev.comdeo.data.datastore.UserPreferenceSerializer
import com.anafthdev.comdeo.data.repository.UserPreferenceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatastoreModule {

	@Provides
	@Singleton
	fun provideUserPreferenceDatastore(
		@ApplicationContext context: Context
	): DataStore<ProtoUserPreference> = DataStoreFactory.create(
		serializer = UserPreferenceSerializer,
		corruptionHandler = UserPreferenceRepository.corruptionHandler,
		produceFile = { context.dataStoreFile(Constant.DATASTORE_USER_PREFERENCE) }
	)

}