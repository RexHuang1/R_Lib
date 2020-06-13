package com.dev.rexhuang.rlib.log;

import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.dev.rexhuang.rlib.log.common.ILogPrinter;
import com.dev.rexhuang.rlib.log.common.RLogConfig;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * *  created by RexHuang
 * *  on 2020/6/5
 */
public class RLogFilePrinter implements ILogPrinter {

    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private final String logPath;
    private final long retentionTime;
    private LogWriter mLogWriter;
    private volatile PrintWorker mWorker;
    private static RLogFilePrinter instance;

    public RLogFilePrinter(String logPath, long retentionTime) {
        this.logPath = logPath;
        this.retentionTime = retentionTime;
        this.mLogWriter = new LogWriter();
        this.mWorker = new PrintWorker();
        cleanExpiredLog();
    }

    public static synchronized RLogFilePrinter getInstance(String logPath, long retentionTime) {
        if (instance == null) {
            instance = new RLogFilePrinter(logPath, retentionTime);
        }
        return instance;
    }

    @Override
    public void print(@NonNull RLogConfig config, int level, String tag, String printString) {
        long timeMillis = System.currentTimeMillis();
        if (!mWorker.isRunning()) {
            mWorker.start();
        }
        mWorker.put(new RLogMo(timeMillis, level, tag, printString));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void doPrint(RLogMo logMo) {
        String lastFileName = mLogWriter.getPreFileName();
        if (lastFileName == null) {
            String newFileName = genFileName();

            if (mLogWriter.isReady()) {
                mLogWriter.close();
            }
            if (!mLogWriter.ready(newFileName)) {
                return;
            }
        }

        mLogWriter.append(logMo.flattenedLog());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String genFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }


    private void cleanExpiredLog() {
        if (retentionTime <= 0) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        File logDir = new File(logPath);
        File[] files = logDir.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (currentTimeMillis - file.lastModified() > retentionTime) {
                file.delete();
            }
        }
    }

    private class PrintWorker implements Runnable {

        private BlockingQueue<RLogMo> logs = new LinkedBlockingQueue<>();
        private volatile boolean running;

        void put(RLogMo logMo) {
            try {
                logs.put(logMo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        boolean isRunning() {
            synchronized (this) {
                return running;
            }
        }

        void start() {
            synchronized (this) {
                EXECUTOR.execute(this);
                running = true;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void run() {
            RLogMo logMo;

            try {
                while (true) {
                    logMo = logs.take();
                    doPrint(logMo);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                synchronized (this) {
                    running = false;
                }
            }

        }
    }

    private class LogWriter {

        private String preFileName;
        private File logFile;
        private BufferedWriter mBufferedWriter;

        boolean isReady() {
            return mBufferedWriter != null;
        }

        String getPreFileName() {
            return preFileName;
        }

        boolean ready(String newFileName) {
            preFileName = newFileName;
            logFile = new File(logPath, newFileName);

            if (!logFile.exists()) {
                try {
                    File parent = logFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    preFileName = null;
                    logFile = null;
                    return false;
                }
            }

            try {
                mBufferedWriter = new BufferedWriter(new FileWriter(logFile, true));
            } catch (IOException e) {
                e.printStackTrace();
                preFileName = null;
                return false;
            }
            return true;
        }

        boolean close() {
            if (mBufferedWriter != null) {
                try {
                    mBufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    mBufferedWriter = null;
                    preFileName = null;
                    logFile = null;
                }
            }
            return true;
        }

        void append(String flattenedLog) {
            try {
                mBufferedWriter.write(flattenedLog);
                mBufferedWriter.newLine();
                mBufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
