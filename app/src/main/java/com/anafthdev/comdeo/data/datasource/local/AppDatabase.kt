package com.anafthdev.comdeo.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anafthdev.comdeo.data.datasource.local.dao.VideoDao
import com.anafthdev.comdeo.data.model.Video

@Database(
	entities = [
		Video::class
	],
	version = 1,
	exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

	abstract fun videoDao(): VideoDao

	companion object {
		private var INSTANCE: AppDatabase? = null

		fun getInstance(context: Context): AppDatabase {
			return INSTANCE ?: Room.databaseBuilder(
				context,
				AppDatabase::class.java,
				"app.db"
			).build().also { INSTANCE = it }
		}
	}

}