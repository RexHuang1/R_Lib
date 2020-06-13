package com.dev.rexhuang.rlib.log.common;

/**
 * *  created by RexHuang
 * *  on 2020/5/30
 */
public interface ILogFormatter<T> {
    String format(T data);
}
