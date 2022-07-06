package com.dev.rexhuang.rlib.cache

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 **  created by RexHuang
 **  on 2020/9/8
 */
@Entity(tableName = "cache")
class Cache :Serializable{
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var key: String = ""

    var data: ByteArray? = null
}