# R_Lib 项目结构
- app库是demo
- r_lib是工具库源码

#### 1. 通用线程池(RExecutor)
主要用于使用线程的场景,使用封装好的线程池可以更好的管理线程
位置: r_lib的executor(r_lib/src/main/java/com/dev/rexhuang/rlib/executor)包下
使用方法：

1. RExecutor.execute(priority:Int, runnable: Runnable),priority表示优先级(0-9),默认值0。runnable是工作任务对象
2. RExecutor.pause()、RExecutor.resume()为暂停线程池和恢复线程池
3. RExecutor.execute(priority:Int, callable: Callable),priority表示优先级(0-9),默认值0。callable是有执行结果回调工作任务对象

demo:DemoExecutorActivity.kt(app/src/main/java/com/dev/rexhuang/r_lib/app/demo/executor/DemoExecutorActivity.kt)

#### 2. Util实用工具类
位置: r_lib的util(r_lib/src/main/java/com/dev/rexhuang/rlib/util)包下

##### 2.1 Util:获取全局application(AppGlobals)
主要用于在全局获取application上下文对象(防止组件化的情况下不能获得上下文的情况)
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/AppGlobals.kt
使用方法:
1. Java调用:AppGlobals.INSTANCE.get()
2. Kotlin调用:AppGlobals.get()

##### 2.2 Util:获取全局Res(RResUtil)
主要用于在全局获取Res对象(防止在非Activity等不能获得上下文的情况需要获得资源对象)
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/RResUtil.kt
使用方法:
1. Java调用:RResUtil.INSTANCE.getString()/getColor()/getColorStateList()/getDrawable()
2. Kotlin调用:RResUtil.getString()/getColor()/getColorStateList()/getDrawable()

##### 2.3 Util:根据百分比计算出start --> end 之间的中间色(ColorUtil)
主要用于根据百分比获取两种颜色的渐变色
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/util/ColorUtil.kt
使用方法:
1. Java调用:ColorUtil.INSTANCE.getCurrentColor(startColor: Int, endColor: Int, fraction: Float)
2. Kotlin调用:ColorUtil.getCurrentColor(startColor: Int, endColor: Int, fraction: Float)

#### 3.通用缓存组件(RStorage)
主要用于快速缓存数据在本地,key(String字符串形式)为缓存的关键字,data(Bytearray二进制数组形式)为目标数据
位置: r_lib/src/main/java/com/dev/rexhuang/rlib/cache
使用方法:
1. Java调用:RStorage.INSTANCE.saveCache(key,data)/get(key)/delete(key);
2. Kotlin调用:RStorage.save(key,data)/get(key)/delete(key)
原理: Room数据库,将key和data封装为Cache对象,存放在Room数据库的cache表中
**注意:**
1. data对象需要序列化(实现Serializable接口)
2. 需要在非UI线程使用RStorage(因为Room数据库的限制)