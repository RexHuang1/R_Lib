package com.dev.rexhuang.rlib.restful

import java.lang.reflect.Type

/**
 *  网络请求响应转换
 *
 **  created by RexHuang
 **  on 2020/8/7
 */
interface RConvert {
    fun <T> convert(string: String, type: Type): RResponse<T>
}