package com.dev.rexhuang.r_lib.app.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.dev.rexhuang.r_lib.R
import com.dev.rexhuang.r_lib.app.demo.executor.DemoExecutorActivity
import com.dev.rexhuang.rlib.log.RLog
import com.dev.rexhuang.rlib.log.RLogFilePrinter
import com.dev.rexhuang.rlib.log.RLogManager
import com.dev.rexhuang.rlib.log.RLogViewPrinter

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.tv_log_console).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_log_view).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_log_file).setOnClickListener(this)
        findViewById<TextView>(R.id.tv_executor_demo).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_log_console -> {
                RLog.d("控制台输出Log")
            }
            R.id.tv_log_view -> {
                val viewPrinter = RLogViewPrinter(this)
                RLogManager.getInstance().addPrinter(viewPrinter)
                viewPrinter.viewProvider.showFloatingView()
                RLog.d("可视化输出Log")
                RLogManager.getInstance().removePrinter(viewPrinter)
            }
            R.id.tv_log_file -> {
                // SDCard/Android/data/com.dev.rexhuang.r_lib/files/log
                val filePrinter =
                    RLogFilePrinter(getExternalFilesDir("log")?.absolutePath, 3600)
                RLogManager.getInstance().addPrinter(filePrinter)
                RLog.d("文件输出Log")
                RLogManager.getInstance().removePrinter(filePrinter)
            }
            R.id.tv_executor_demo ->{
                startActivity(Intent(this,DemoExecutorActivity::class.java))
            }
        }
    }
}
