package com.dev.rexhuang.rlib.restful

/**
 *  网络请求Callback回调
 *
 **  created by RexHuang
 **  on 2020/8/4
 */
interface RCallback<T> {
    fun onSuccess(response: RResponse<T>)
    fun onFailed(throwable: Throwable)
}