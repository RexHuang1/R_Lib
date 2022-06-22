package com.dev.rexhuang.rlib.cache

import androidx.room.*

/**
 **  created by RexHuang
 **  on 2020/9/8
 */
@Dao
interface CacheDao {
    @Insert(entity = Cache::class, onConflict = OnConflictStrategy.REPLACE)
    fun saveCache(cache: Cache): Long

    @Query("select * from cache where `key`=:key")
    fun getCache(key: String): Cache?

    @Delete(entity = Cache::class)
    fun deleteCache(cache: Cache)
}