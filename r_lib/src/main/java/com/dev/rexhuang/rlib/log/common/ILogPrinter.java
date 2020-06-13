package com.dev.rexhuang.rlib.log.common;

import androidx.annotation.NonNull;

/**
 * *  created by RexHuang
 * *  on 2020/5/30
 */
public interface ILogPrinter {
    void print(@NonNull RLogConfig config, @RLogType.level int level, String tag, String printString);
}
