# R_Lib 项目结构
- app库是demo
- r_lib是工具库源码

#### 1. 通用线程池
位置：r_lib的executor(r_lib/src/main/java/com/dev/rexhuang/rlib/executor)包下
使用方法：

1. RExecutor.execute(priority:Int, runnable: Runnable),priority表示优先级(0-9),默认值0。runnable是工作任务对象
2. RExecutor.pause()、RExecutor.resume()为暂停线程池和恢复线程池
3. RExecutor.execute(priority:Int, callable: Callable),priority表示优先级(0-9),默认值0。callable是有执行结果回调工作任务对象

demo:DemoExecutorActivity.kt(app/src/main/java/com/dev/rexhuang/r_lib/app/demo/executor/DemoExecutorActivity.kt)