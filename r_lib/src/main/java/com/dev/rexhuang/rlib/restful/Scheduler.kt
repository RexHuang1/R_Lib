package com.dev.rexhuang.rlib.restful

import com.dev.rexhuang.rlib.cache.HiStorage
import com.dev.rexhuang.rlib.executor.RExecutor
import com.dev.rexhuang.rlib.log.RLog
import com.dev.rexhuang.rlib.restful.annotation.CacheStrategy
import com.dev.rexhuang.rlib.util.MainHandler

/**
 **  代理CallFactory创建出来的call对象,从而实现拦截器的派发动作
 **  created by RexHuang
 **  on 2020/8/5
 */
class Scheduler(
    private val callFactory: RCall.Factory,
    private val interceptors: MutableList<RInterceptor>
) {

    fun newCall(newRequest: RRequest): RCall<*> {
        val newCall = callFactory.newCall(newRequest)
        return ProxyCall(newCall, newRequest)
    }

    internal inner class ProxyCall<T>(
        private val delegate: RCall<T>,
        private val request: RRequest
    ) : RCall<T> {

        override fun execute(): RResponse<T> {
            dispatchInterceptor(request, null)

            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                val cacheResponse = readCache<T>()
                if (cacheResponse.data != null) {
                    return cacheResponse
                }
                RLog.d("enqueue, cache : " + request.getCacheKey())
            }

            val response = delegate.execute()

            saveCacheIfNeeded(response)

            dispatchInterceptor(request, response)

            return response
        }

        override fun enqueue(callback: RCallback<T>) {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                RExecutor.execute(runnable = Runnable {
                    val cacheResponse = readCache<T>()
                    if (cacheResponse.data != null) {
                        MainHandler.sendAtFrontOfQueue(Runnable { callback.onSuccess(cacheResponse) })
                    }
                    RLog.d("enqueue, cache : " + request.getCacheKey())
                })
            }
            delegate.enqueue(object : RCallback<T> {
                override fun onSuccess(response: RResponse<T>) {
                    dispatchInterceptor(request, response)

                    saveCacheIfNeeded(response)

                    callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callback.onFailed(throwable)
                }

            })
        }

        private fun saveCacheIfNeeded(response: RResponse<T>) {
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST ||
                request.cacheStrategy == CacheStrategy.NET_CACHE
            ) {
                if (response.data != null) {
                    RExecutor.execute(runnable = Runnable {
                        HiStorage.saveCache(request.getCacheKey(), response.data)
                    })
                }
            }
        }

        private fun <T> readCache(): RResponse<T> {
            // HiStorage 查询缓存需要提供一个cache key
            // request url+ 参数
            val cacheKey = request.getCacheKey()
            val cache = HiStorage.getCache<T>(cacheKey)
            val cacheResponse = RResponse<T>()
            cacheResponse.data = cache
            cacheResponse.code = RResponse.CACHE_SUCCESS
            cacheResponse.msg = "缓存获取成功"
            return cacheResponse
        }

        private fun dispatchInterceptor(request: RRequest, response: RResponse<T>?) {
            if (interceptors.size <= 0) {
                return
            }
            InterceptorChain(request, response).dispatch()
        }


        internal inner class InterceptorChain(
            private val request: RRequest,
            private val response: RResponse<T>?
        ) : RInterceptor.Chain {

            // 拦截器下标
            var callIndex: Int = 0
            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): RRequest {
                return request
            }

            override fun response(): RResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }


        }
    }

}