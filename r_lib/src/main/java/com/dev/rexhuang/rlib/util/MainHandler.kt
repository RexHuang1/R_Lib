package com.dev.rexhuang.rlib.util

import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 **  created by RexHuang
 **  on 2020/8/10
 */
object MainHandler {
    private val handler = Handler(Looper.getMainLooper())

    /**
     * 提交runnable任务,当主线程MessageQueue取出该任务时执行
     */
    fun post(runnable: Runnable) {
        handler.post(runnable)
    }

    /**
     * 提交runnable延时任务,当主线程MessageQueue取出该任务时执行
     */
    fun postDelay(delayMillis: Long, runnable: Runnable) {
        handler.postDelayed(runnable, delayMillis)
    }

    /**
     * 将任务提交到主线程MessageQueue的头部,让其尽快执行
     */
    fun sendAtFrontOfQueue(runnable: Runnable) {
        val msg = Message.obtain(handler, runnable)
        handler.sendMessageAtFrontOfQueue(msg)
    }
}