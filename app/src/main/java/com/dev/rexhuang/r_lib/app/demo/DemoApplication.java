package com.dev.rexhuang.r_lib.app.demo;

import android.app.Application;

import com.dev.rexhuang.rlib.log.RLogConsolePrinter;
import com.dev.rexhuang.rlib.log.RLogManager;
import com.dev.rexhuang.rlib.log.common.ILogPrinter;
import com.dev.rexhuang.rlib.log.common.RLogConfig;

/**
 * *  created by RexHuang
 * *  on 2020/6/13
 */
public class DemoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RLogManager.init(new RLogConfig() {
            @Override
            public JsonParser injectJsonParser() {
                return super.injectJsonParser();
            }

            @Override
            public String getGlobalTag() {
                return super.getGlobalTag();
            }

            @Override
            public boolean enable() {
                return super.enable();
            }

            @Override
            public boolean includeThread() {
                return super.includeThread();
            }

            @Override
            public int stackTraceDepth() {
                return super.stackTraceDepth();
            }

            @Override
            public ILogPrinter[] printers() {
                return super.printers();
            }
        }, new RLogConsolePrinter());
    }
}
