package com.dev.rexhuang.rlib.log;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dev.rexhuang.rlib.log.common.ILogPrinter;
import com.dev.rexhuang.rlib.log.common.RLogConfig;

/**
 * *  created by RexHuang
 * *  on 2020/5/30
 */
public class RLogConsolePrinter implements ILogPrinter {

    @Override
    public void print(@NonNull RLogConfig config, int level, String tag, String printString) {
        int len = printString.length();
        int countOfSub = len / RLogConfig.MAX_LEN;
        if (countOfSub > 0) {
            StringBuilder log = new StringBuilder();
            int index = 0;
            for (int i = 0; i < countOfSub; i++) {
                log.append(printString.substring(index, index + RLogConfig.MAX_LEN));
                index += RLogConfig.MAX_LEN;
            }
            if (index != len) {
                log.append(printString.substring(index, len));
            }
            Log.println(level, tag, log.toString());
        } else {
            Log.println(level, tag, printString);
        }
    }
}
