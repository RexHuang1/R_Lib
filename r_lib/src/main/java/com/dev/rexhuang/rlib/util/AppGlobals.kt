package com.dev.rexhuang.rlib.util

import android.app.Application

/**
 * 全局获取application上下文
 * 对于组件化的项目,不可能将Application下沉到base中,而且各组件之间不需获取application真正的类名,
 * 这样可以通过一次反射就能获取全局application
 **  created by RexHuang
 **  on 2022/6/29
 */
object AppGlobals {
    var application: Application? = null
    fun get(): Application? {
        if (application == null) {
            try {
                application = Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null) as Application
            } catch (exception:ClassNotFoundException){
                exception.printStackTrace()
            }
        }
        return application
    }
}