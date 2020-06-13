package com.dev.rexhuang.rlib.log;

import com.dev.rexhuang.rlib.log.common.ILogFormatter;

/**
 * *  created by RexHuang
 * *  on 2020/5/30
 */
public class RLogThreadFormatter implements ILogFormatter<Thread> {

    @Override
    public String format(Thread thread) {
        StringBuilder sb = new StringBuilder();
        if (thread != null) {
            sb.append("Thread:\n\t")
                    .append(thread.getName());
        }
        return "\n"+sb.toString();
    }
}
