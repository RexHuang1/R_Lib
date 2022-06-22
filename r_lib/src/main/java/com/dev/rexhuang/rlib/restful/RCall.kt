package com.dev.rexhuang.rlib.restful

import java.io.IOException

/**
 *  网络请求接口
 *
 **  created by RexHuang
 **  on 2020/8/4
 */
interface RCall<T> {
    /**
     * 同步网络请求
     */
    @Throws(IOException::class)
    fun execute(): RResponse<T>

    /**
     * 异步网络请求
     */
    fun enqueue(callback: RCallback<T>)

    /**
     * 网络请求工厂接口
     */
    interface Factory {
        fun newCall(request: RRequest): RCall<*>
    }
}