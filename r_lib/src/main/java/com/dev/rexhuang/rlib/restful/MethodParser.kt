package com.dev.rexhuang.rlib.restful

import com.dev.rexhuang.rlib.restful.annotation.*
import java.lang.IllegalStateException
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 **  created by RexHuang
 **  on 2020/8/5
 */
class MethodParser(
    private val baseUrl: String,
    method: Method
) {

    private var replaceRelativeUrl: String? = null
    private var cacheStrategy: Int = CacheStrategy.NET_ONLY
    private var formPost: Boolean = false
    private var httpMethod: Int = -1
    private var domainUrl: String? = null
    private lateinit var relativeUrl: String
    private var returnType: Type? = null
    private var headers: MutableMap<String, String> = mutableMapOf()
    private val parameters: MutableMap<String, String> = mutableMapOf()

    init {
        // parse method annotations
        parseMethodAnnotations(method)

        // parse method parameters
//        parseMethodParameters(method, args)

        // parse method generic return type
        parseMethodReturnType(method)
    }

    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != RCall::class.java) {
            throw IllegalStateException(
                String.format(
                    "method %s return type must be RCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) { "method ${method.name} can only has one generic return type" }
            returnType = actualTypeArguments[0]
        } else {
            throw IllegalStateException("method ${method.name} must has one generic return type")
        }
    }

    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        // 每次调用api接口时  应该吧上一次解析到的参数清理掉，因为methodParser存在复用
        parameters.clear()

        val parameterAnnotations = method.parameterAnnotations
        val equals = parameterAnnotations.size == args.size
        require(equals) {
            String.format(
                "arguments annotations count %s dont match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        for (index in args.indices) {
            val annotationArray = parameterAnnotations[index]
            require(annotationArray.size <= 1) {
                "each parameter can only has one annotation"
            }
            val value = args[index]
            require(isPrimitive(value)) {
                "8 basic types are supported for now,index=$index"
            }
            val annotation = annotationArray[0]
            if (annotation is com.dev.rexhuang.rlib.restful.annotation.Field) {
                val name = annotation.value
                parameters[name] = value.toString()
            } else if (annotation is Path) {
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    replaceRelativeUrl = relativeUrl.replace("{$replaceName}", replacement)
                }
            } else if (annotation is CacheStrategy) {
                cacheStrategy = value as Int
            } else {
                throw IllegalStateException("cannot handle parameter annotation :" + annotation.javaClass.toString())
            }
        }
    }

    private fun isPrimitive(value: Any): Boolean {
        // String
        if (value.javaClass == String::class.java) {
            return true
        }
        try {
            //int byte short long boolean char double float
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return false
    }

    private fun parseMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            if (annotation is GET) {
                relativeUrl = annotation.value
                httpMethod = RRequest.METHOD.GET
            } else if (annotation is POST) {
                relativeUrl = annotation.value
                formPost = annotation.formPost
                httpMethod = RRequest.METHOD.POST
                // @Headers("auth-token:token", "accountId:123456")
            } else if (annotation is Headers) {
                val headerArray = annotation.value
                for (header in headerArray) {
                    val colon = header.indexOf(":")
                    check(!(colon == 0 || colon == -1)) {
                        String.format(
                            "@headers value must be in the form [name:value], but found[%s]",
                            header
                        )
                    }
                    val name = header.substring(0, colon)
                    val value = header.substring(colon + 1).trim()
                    headers[name] = value
                }
            } else if (annotation is BaseUrl) {
                domainUrl = annotation.value
            }else if (annotation is CacheStrategy) {
                cacheStrategy = annotation.value
            } else {
                throw IllegalStateException("cannot handle method annotation:" + annotation.javaClass.toString())
            }
        }

        require((httpMethod == RRequest.METHOD.GET) || (httpMethod == RRequest.METHOD.POST)) {
            String.format("method %s must has one of GET,POST ", method.name)
        }

        if (domainUrl == null) {
            domainUrl = baseUrl
        }
    }

    fun newRequest(method: Method, args: Array<out Any>?): RRequest {
        val request = RRequest()
        val arguments: Array<Any> = args as Array<Any>? ?: arrayOf()

        parseMethodParameters(method, arguments)

        request.httpMethod = httpMethod
        request.domainUrl = domainUrl
        request.relativeUrl = replaceRelativeUrl ?: relativeUrl
        request.headers = headers
        request.parameters = parameters
        request.returnType = returnType
        request.formPost = formPost
        request.cacheStrategy = cacheStrategy
        return request
    }

    companion object {
        fun parse(baseUrl: String, method: Method): MethodParser {
            return MethodParser(baseUrl, method)
        }
    }
}