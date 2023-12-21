package com.anafthdev.comdeo.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anafthdev.comdeo.data.model.Video
import kotlinx.coroutines.flow.Flow

@Dao
abstract class VideoDao {

	@Query("select * from video")
	abstract fun getAll(): Flow<List<Video>>

	@Query("delete from video")
	abstract fun deleteAll()

	@Query("select * from video where id_video like :id")
	abstract fun getById(id: Long): Flow<Video?>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insert(vararg video: Video)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	abstract suspend fun insert(videos: Collection<Video>)

	@Update
	abstract suspend fun update(vararg video: Video)

	@Update
	abstract suspend fun update(videos: Collection<Video>)

	@Delete
	abstract suspend fun delete(vararg video: Video)

	@Delete
	abstract suspend fun delete(videos: Collection<Video>)

}