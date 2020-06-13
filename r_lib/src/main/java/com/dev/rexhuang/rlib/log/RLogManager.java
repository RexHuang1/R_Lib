package com.dev.rexhuang.rlib.log;

import androidx.annotation.NonNull;

import com.dev.rexhuang.rlib.log.common.ILogPrinter;
import com.dev.rexhuang.rlib.log.common.RLogConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * *  created by RexHuang
 * *  on 2020/5/30
 */
public class RLogManager {

    private RLogConfig mLogConfig;
    private List mPrinters = new ArrayList();
    private static RLogManager instance;

    private RLogManager(@NonNull RLogConfig config, ILogPrinter... printers) {
        mLogConfig = config;
        mPrinters.addAll(Arrays.asList(printers));
    }

    public static RLogManager getInstance() {
        return instance;
    }

    public static void init(@NonNull RLogConfig config, ILogPrinter... printers) {
        instance = new RLogManager(config, printers);
    }

    public RLogConfig getConfig() {
        return mLogConfig;
    }

    public List<ILogPrinter> getPrinters() {
        return mPrinters;
    }

    public void addPrinter(ILogPrinter printer){
        mPrinters.add(printer);
    }

    public void removePrinter(ILogPrinter printer){
        if (mPrinters != null) {
            mPrinters.remove(printer);
        }
    }
}
