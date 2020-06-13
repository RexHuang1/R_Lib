package com.dev.rexhuang.rlib.log;

import androidx.annotation.NonNull;

import com.dev.rexhuang.rlib.log.common.ILogPrinter;
import com.dev.rexhuang.rlib.log.common.RLogConfig;
import com.dev.rexhuang.rlib.log.common.RLogType;

/**
 * Log输出:
 * 1. 控制台打印
 * 2. View打印
 * 3. 文件打印
 * *  created by RexHuang
 * *  on 2020/5/30
 */
public class RLog {

    private static final String I_LOG_PACKAGE;

    static {
        String className = RLog.class.getName();
        I_LOG_PACKAGE = className.substring(0, className.lastIndexOf('.') + 1);
    }

    public static void v(Object... contents) {
        log(RLogType.V, contents);
    }

    public static void d(Object... contents) {
        log(RLogType.D, contents);
    }

    public static void i(Object... contents) {
        log(RLogType.I, contents);
    }

    public static void w(Object... contents) {
        log(RLogType.W, contents);
    }

    public static void e(Object... contents) {
        log(RLogType.E, contents);
    }

    public static void a(Object... contents) {
        log(RLogType.A, contents);
    }

    public static void vt(String tag, Object... contents) {
        log(RLogType.V, tag, contents);
    }

    public static void dt(String tag, Object... contents) {
        log(RLogType.D, tag, contents);
    }

    public static void it(String tag, Object... contents) {
        log(RLogType.I, tag, contents);
    }

    public static void wt(String tag, Object... contents) {
        log(RLogType.W, tag, contents);
    }

    public static void et(String tag, Object... contents) {
        log(RLogType.E, tag, contents);
    }

    public static void at(String tag, Object... contents) {
        log(RLogType.A, tag, contents);
    }

    private static void log(@RLogType.level int level, Object... contents) {
        log(level, RLogManager.getInstance().getConfig().getGlobalTag(), contents);
    }

    private static void log(@RLogType.level int level, String tag, Object... contents) {
        log(RLogManager.getInstance().getConfig(), level, tag, contents);
    }

    private static void log(@NonNull RLogConfig config, @RLogType.level int level, String tag, Object... contents) {
        if (!config.enable()) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        if (config.includeThread()) {
            String threadInfo = RLogConfig.sLogThreadFormatter.format(Thread.currentThread());
            sb.append(threadInfo);
            sb.append("\n");
        }
        if (config.stackTraceDepth() > 0) {
            String stackTrace = RLogConfig.sLogStackTraceFormatter.format(
                    RLogStackTraceUtil.getCroppedRealStackTrace(new Throwable().getStackTrace(), I_LOG_PACKAGE, config.stackTraceDepth()));
            sb.append(stackTrace);
            sb.append("\n");
        }
        String msg = parseObject(contents, config);
        if (msg != null) {//替换转义字符\
            msg = msg.replace("\\\"", "\"");
        }
        sb.append(msg);
        for (ILogPrinter printer : RLogManager.getInstance().getPrinters()) {
            printer.print(RLogManager.getInstance().getConfig(), level, tag, sb.toString());
        }
    }

    private static String parseObject(@NonNull Object[] contents, @NonNull RLogConfig config) {
        if (config.injectJsonParser() != null) {
            // 只有一个数据且为String的情况下不再进行序列化
            if (contents.length == 1 && contents[0] instanceof String) {
                return (String) contents[0];
            }
            return config.injectJsonParser().toJson(contents);
        }
        StringBuilder sb = new StringBuilder();
        for (Object o : contents) {
            sb.append(o.toString()).append(";");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
