# R_Lib 项目结构
- app库是demo

- r_lib是工具库源码

  [TOC]

## 1. 通用线程池(RExecutor)
主要用于使用线程的场景,使用封装好的线程池可以更好的管理线程
位置: r_lib的executor(r_lib/src/main/java/com/dev/rexhuang/rlib/executor)包下
使用方法：

1. RExecutor.execute(priority:Int, runnable: Runnable),priority表示优先级(0-9),默认值0。runnable是工作任务对象
2. RExecutor.pause()、RExecutor.resume()为暂停线程池和恢复线程池
3. RExecutor.execute(priority:Int, callable: Callable),priority表示优先级(0-9),默认值0。callable是有执行结果回调工作任务对象

demo:DemoExecutorActivity.kt(app/src/main/java/com/dev/rexhuang/r_lib/app/demo/executor/DemoExecutorActivity.kt)

------



## 2. Util实用工具类
位置: r_lib的util(r_lib/src/main/java/com/dev/rexhuang/rlib/util)包下

###2.1 Util:获取全局application(AppGlobals)
主要用于在全局获取application上下文对象(防止组件化的情况下不能获得上下文的情况)
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/AppGlobals.kt
使用方法:
1. Java调用:AppGlobals.INSTANCE.get()
2. Kotlin调用:AppGlobals.get()



### 2.2 Util:获取全局Res(RResUtil)
主要用于在全局获取Res对象(防止在非Activity等不能获得上下文的情况需要获得资源对象)
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/RResUtil.kt
使用方法:
1. Java调用:RResUtil.INSTANCE.getString()/getColor()/getColorStateList()/getDrawable()
2. Kotlin调用:RResUtil.getString()/getColor()/getColorStateList()/getDrawable()



### 2.3 Util:根据百分比计算出start --> end 之间的中间色(ColorUtil)
主要用于根据百分比获取两种颜色的渐变色
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/ColorUtil.kt
使用方法:
1. Java调用:ColorUtil.INSTANCE.getCurrentColor(startColor: Int, endColor: Int, fraction: Float)
2. Kotlin调用:ColorUtil.getCurrentColor(startColor: Int, endColor: Int, fraction: Float)



### 2.4 Util:显示工具类(RDisplayUtil)
主要用于转换dp/sp等单位为px像素值单位,获取当前window的宽高(单位为px),以及获取状态栏高度值(单位为px)
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/RDisplayUtil.java
使用方法:
方法注释有说明



### 2.5 Util:其他线程向主线程提交任务的工具类(MainHandler)
主要用于子线程等其他线程向主线程提交任务让其执行的情况
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/MainHandler.kt
使用方法:
方法注释有说明



### 2.6 Util:状态栏设置工具类(RStatusBar)
主要用于调整状态栏的显示
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/RStatusBar.kt
使用方法:
1. Java调用:RStatusBar.INSTANCE.setStatusBar(activity: Activity, darkContent: Boolean,
                                      statusBarColor: Int = Color.WHITE, translucent: Boolean)
2. Kotlin调用:RStatusBar.setStatusBar(activity: Activity, darkContent: Boolean,
                                                  statusBarColor: Int = Color.WHITE, translucent: Boolean)
3. darkContent true:意味着 白底黑字， false:黑底白字
4. statusBarColor  状态栏的背景色
5. translucent  沉浸式效果，也就是页面的布局延伸到状态栏之下

------



## 3.通用缓存组件(RStorage)
主要用于快速缓存数据在本地,key(String字符串形式)为缓存的关键字,data(Bytearray二进制数组形式)为目标数据
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/cache
使用方法:
1. Java调用:RStorage.INSTANCE.saveCache(key,data)/get(key)/delete(key);
2. Kotlin调用:RStorage.save(key,data)/get(key)/delete(key)
原理: Room数据库,将key和data封装为Cache对象,存放在Room数据库的cache表中

**注意:**
1. data对象需要序列化(实现Serializable接口)
2. 需要在非UI线程使用RStorage(因为Room数据库的限制)