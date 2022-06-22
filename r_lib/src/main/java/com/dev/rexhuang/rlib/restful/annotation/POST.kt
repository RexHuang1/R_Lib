package com.dev.rexhuang.rlib.restful.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 *  @POST("/cities/{province}")
 *  fun test(@Path("province") int provinceId)
 *
 **  created by RexHuang
 **  on 2020/8/4
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(RetentionPolicy.RUNTIME)
annotation class POST(val value: String, val formPost: Boolean = true)