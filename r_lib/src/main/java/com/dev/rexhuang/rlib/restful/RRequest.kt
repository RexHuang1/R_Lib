package com.dev.rexhuang.rlib.restful

import android.text.TextUtils
import androidx.annotation.IntDef
import com.dev.rexhuang.rlib.restful.annotation.CacheStrategy
import java.lang.Exception
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.net.URLEncoder

/**
 * 网络请求实体类
 */
open class RRequest {
    private  var cacheStrategyKey: String = ""
    var cacheStrategy: Int = CacheStrategy.NET_ONLY

    @METHOD
    var httpMethod = 0

    var domainUrl: String? = null
    var relativeUrl: String? = null
    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, String>? = null
    var returnType: Type? = null
    var formPost: Boolean = true

    @IntDef(value = [METHOD.GET, METHOD.POST])
    annotation class METHOD {

        companion object {
            const val GET = 0
            const val POST = 1
        }
    }

    fun endPointUrl(): String {
        if (relativeUrl == null) {
            throw IllegalStateException("relative url must bot be null ")
        }
        if (!relativeUrl!!.startsWith("/")) {
            return domainUrl + relativeUrl
        }

        val indexOf = domainUrl!!.indexOf("/")
        return domainUrl!!.substring(0, indexOf) + relativeUrl
    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![name] = value
    }

    fun getCacheKey(): String {
        if (!TextUtils.isEmpty(cacheStrategyKey)){
            return cacheStrategyKey
        }
        val builder = StringBuilder()
        val endUrl = endPointUrl()
        builder.append(endUrl)
        if (endUrl.indexOf("?") > 0 || endUrl.indexOf("&") > 0){
            builder.append("&")
        } else{
            builder.append("?")
        }

        if (parameters != null){
            for ((key,value) in parameters!!){
                try {
                    val encodeValue =URLEncoder.encode(value,"UTF-8")
                    builder.append(key).append("=").append(encodeValue).append("&")
                }catch (e:Exception){
                    // ignore
                }
            }
            builder.deleteCharAt(builder.length -1)
            cacheStrategyKey = builder.toString()
        } else{
            cacheStrategyKey = endUrl
        }

        return cacheStrategyKey
    }
}
