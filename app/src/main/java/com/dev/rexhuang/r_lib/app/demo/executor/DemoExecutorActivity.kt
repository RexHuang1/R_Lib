package com.dev.rexhuang.r_lib.app.demo.executor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.dev.rexhuang.r_lib.R
import com.dev.rexhuang.rlib.executor.RExecutor
import com.dev.rexhuang.rlib.log.RLog

class DemoExecutorActivity : AppCompatActivity() {
    private var paused = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_executor)
        val priority_button = findViewById<Button>(R.id.priority_button)
        val pause_button = findViewById<Button>(R.id.pause_button)
        val callable_button = findViewById<Button>(R.id.callable_button)

        priority_button.setOnClickListener {
            for (priority in 1..9) {
                RExecutor.execute(priority, Runnable {
                    try {
                        Thread.sleep((1000 - priority * 100).toLong())
                    } catch (e: InterruptedException) {
                        e.printStackTrace()

                    }
                })
            }
        }

        pause_button.setOnClickListener {
            if (paused){
                RExecutor.resume()
            } else {
                RExecutor.pause()
            }
            paused = !paused
        }

        callable_button.setOnClickListener {
            RExecutor.execute(callable = object : RExecutor.Callable<String>() {
                override fun onBackground(): String? {
                    RLog.et("DemoExecutorActivity","onBackground-当前线程是:" + Thread.currentThread().name)
                    return "异步任务结果"
                }

                override fun onCompleted(result: String?) {
                    RLog.et("DemoExecutorActivity","onCompleted-当前线程是:" + Thread.currentThread().name)
                    RLog.et("DemoExecutorActivity","onCompleted-任务结果是:" + result)

                }

            })
        }
    }
}