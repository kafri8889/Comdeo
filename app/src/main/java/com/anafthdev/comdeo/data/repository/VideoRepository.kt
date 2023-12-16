package com.anafthdev.comdeo.data.repository

import com.anafthdev.comdeo.data.datasource.local.dao.VideoDao
import com.anafthdev.comdeo.data.model.Video
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VideoRepository @Inject constructor(
	private val videoDao: VideoDao
) {

	fun getAll(): Flow<List<Video>> = videoDao.getAll()

	fun deleteAll() = videoDao.deleteAll()

	suspend fun insert(vararg video: Video) = videoDao.insert(*video)
	suspend fun update(vararg video: Video) = videoDao.update(*video)
	suspend fun delete(vararg video: Video) = videoDao.delete(*video)

	suspend fun insert(videos: Collection<Video>) = videoDao.insert(videos)
	suspend fun update(videos: Collection<Video>) = videoDao.update(videos)
	suspend fun delete(videos: Collection<Video>) = videoDao.delete(videos)

}