package com.dev.rexhuang.rlib.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dev.rexhuang.rlib.util.AppGlobals

/**
 **  created by RexHuang
 **  on 2020/9/8
 */
@Database(entities = [Cache::class], version = 1)
abstract class CacheDatabase : RoomDatabase() {
    companion object {
        private var database: CacheDatabase
        fun get(): CacheDatabase {
            return database
        }

        init {
            val context = AppGlobals.get()!!.applicationContext
            database =
                Room.databaseBuilder(context, CacheDatabase::class.java, "common_cache").build()
        }
    }

    abstract val cacheDao:CacheDao

}