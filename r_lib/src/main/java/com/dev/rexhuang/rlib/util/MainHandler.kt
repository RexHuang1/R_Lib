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

    fun post(runnable: Runnable) {
        handler.post(runnable)
    }

    fun postDelay(delayMillis: Long, runnable: Runnable) {
        handler.postDelayed(runnable, delayMillis)
    }

    fun sendAtFrontOfQueue(runnable: Runnable) {
        val msg = Message.obtain(handler, runnable)
        handler.sendMessageAtFrontOfQueue(msg)
    }
}