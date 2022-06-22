package com.dev.rexhuang.rlib.executor

import android.os.Handler
import android.os.Looper
import androidx.annotation.IntRange
import com.dev.rexhuang.rlib.log.RLog
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

/**
 * 支持按任务的优先级去执行,
 * 支持线程池暂停.恢复(批量文件下载，上传) ，
 * 异步结果主动回调主线程
 * todo 线程池能力监控,耗时任务检测,定时,延迟
 *
 **  created by RexHuang
 **  on 2020/8/3
 */
object RExecutor {
    private const val TAG: String = "RExecutor"
    private var isPaused: Boolean = false
    private var executor: ThreadPoolExecutor
    private var lock: ReentrantLock = ReentrantLock()
    private var pauseCondition: Condition
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        pauseCondition = lock.newCondition()

        val cpuCount = Runtime.getRuntime().availableProcessors()
        val corePoolSize = cpuCount + 1
        val maxPoolSize = cpuCount * 2 + 1
        val blockingQueue: PriorityBlockingQueue<out Runnable> = PriorityBlockingQueue()
        val keepAliveTime = 30L
        val unit = TimeUnit.SECONDS

        val seq = AtomicLong()
        val threadFactory = ThreadFactory {
            val thread = Thread(it)
            thread.name = "r-executor-${seq.getAndIncrement()}"
            return@ThreadFactory thread
        }

        executor = object :
            ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                unit,
                blockingQueue as BlockingQueue<Runnable>,
                threadFactory
            ) {
            override fun beforeExecute(t: Thread?, r: Runnable?) {
                if (isPaused) {
                    lock.lock()
                    try {
                        pauseCondition.await()
                    } finally {
                        lock.unlock()
                    }
                }
            }

            override fun afterExecute(r: Runnable?, t: Throwable?) {
                // 监控线程池耗时任务,线程创建数量,正在运行的数量
                RLog.et(TAG, "已执行完的任务优先级是: ${(r as PriorityRunnable).priority}")
            }
        }
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, runnable: Runnable) {
        executor.execute(PriorityRunnable(priority, runnable))
    }

    @JvmOverloads
    fun execute(@IntRange(from = 0, to = 10) priority: Int = 0, callable: Callable<*>) {
        executor.execute(PriorityRunnable(priority, callable))
    }

    abstract class Callable<T> : Runnable {
        override fun run() {
            val prepareRunnable = Runnable { onPrepare() }
            mainHandler.post(prepareRunnable)

            val t:T? = onBackground()

            // 移除所有消息.防止需要执行onCompleted了，onPrepare还没被执行，那就不需要执行了
            mainHandler.removeCallbacks(prepareRunnable)
            mainHandler.post { onCompleted(t) }
        }

        open fun onPrepare() {

        }

        abstract fun onBackground(): T?
        abstract fun onCompleted(t: T?)
    }

    class PriorityRunnable(val priority: Int, private val runnable: Runnable) : Runnable,
        Comparable<PriorityRunnable> {
        override fun compareTo(other: PriorityRunnable): Int {
            return if (this.priority < other.priority) 1 else if (this.priority > other.priority) -1 else 0
        }

        override fun run() {
            runnable.run()
        }

    }

    fun pause() {
        lock.lock()
        try {
            isPaused = true
            RLog.et(TAG, "RExecutor is paused")
        } finally {
            lock.unlock()
        }
    }

    fun resume() {
        isPaused = false
        lock.lock()
        try {
            pauseCondition.signalAll()
        } finally {
            lock.unlock()
        }
        RLog.et(TAG, "RExecutor is resumed")
    }
}