package com.dev.rexhuang.rlib.restful.annotation

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * @Headers({"connection:keep-alive","auth-token:token"})
 * fun test(@Filed("province") int provinceId)
 *
 **  created by RexHuang
 **  on 2020/8/5
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(RetentionPolicy.RUNTIME)
annotation class Headers(vararg val value: String)