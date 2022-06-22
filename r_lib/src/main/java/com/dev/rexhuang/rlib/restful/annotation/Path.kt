package com.dev.rexhuang.rlib.restful.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 *  @GET("/cities/{province}")
 *  fun test(@Path("province") int provinceId)
 *
 **  created by RexHuang
 **  on 2020/8/4
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
annotation class Path(val value: String)