package com.dev.rexhuang.rlib.cache

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

/**
 **  created by RexHuang
 **  on 2020/9/8
 */
object RStorage {
    fun <T> saveCache(key: String, body: T) {
        val cache = Cache()
        cache.key = key
        cache.data = toByteArray(body)
        CacheDatabase.get().cacheDao.saveCache(cache)
    }

    fun <T> getCache(key: String): T? {
        val cache = CacheDatabase.get().cacheDao.getCache(key)
        return (if (cache?.data != null) {
            toObject(cache.data)
        } else null) as? T
    }

    fun deleteCache(key: String){
        val cache = Cache()
        cache.key = key
        CacheDatabase.get().cacheDao.deleteCache(cache)
    }

    /**
     * 将任意可序列化对象转换为二进制数组的形式
     */
    private fun <T> toByteArray(body: T): ByteArray? {
        var baos: ByteArrayOutputStream? = null
        var oos: ObjectOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            oos = ObjectOutputStream(baos)
            oos.writeObject(body)
            oos.flush()
            return baos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            baos?.close()
            oos?.close()
        }
        return ByteArray(0)
    }


    /**
     * 将二进制数组形式的对象还原为原来的对象
     */
    private fun toObject(data: ByteArray?): Any? {
        var bais: ByteArrayInputStream? = null
        var ois: ObjectInputStream? = null
        try {
            bais = ByteArrayInputStream(data)
            ois = ObjectInputStream(bais)
            return ois.readObject()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            bais?.close()
            ois?.close()
        }
        return null
    }
}