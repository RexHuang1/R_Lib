package com.dev.rexhuang.rlib.restful.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 *  @BaseUrl("https://api.devio.org/as/")
 *  fun test(@Filed("province") int provinceId)
 *
 **  created by RexHuang
 **  on 2020/8/4
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(RetentionPolicy.RUNTIME)
annotation class BaseUrl(val value: String)