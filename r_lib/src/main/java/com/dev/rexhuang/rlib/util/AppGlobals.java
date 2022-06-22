package com.dev.rexhuang.rlib.util;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;

/**
 * *  created by RexHuang
 * *  on 2020/6/12
 */
public class AppGlobals {

    public static Application get() {
        try {
            return (Application) Class.forName("android.app.ActivityThread")
                    .getMethod("currentApplication")
                    .invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
