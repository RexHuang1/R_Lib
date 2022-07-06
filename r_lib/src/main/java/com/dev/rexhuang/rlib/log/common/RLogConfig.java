package com.dev.rexhuang.rlib.log.common;

import com.dev.rexhuang.rlib.log.RLogStackTraceFormatter;
import com.dev.rexhuang.rlib.log.RLogThreadFormatter;

/**
 * *  created by RexHuang
 * *  on 2020/5/30
 */
public abstract class RLogConfig {

    public static int MAX_LEN = 512;
    public static RLogThreadFormatter sLogThreadFormatter = new RLogThreadFormatter();
    public static RLogStackTraceFormatter sLogStackTraceFormatter = new RLogStackTraceFormatter();

    public JsonParser injectJsonParser() {
        return null;
    }

    public String getGlobalTag() {
        return "RLog";
    }

    public boolean enable() {
        return true;
    }

    public boolean includeThread() {
        return true;
    }

    public int stackTraceDepth() {
        return 5;
    }

    public ILogPrinter[] printers() {
        return null;
    }

    public interface JsonParser {
        String toJson(Object src);
    }

}
