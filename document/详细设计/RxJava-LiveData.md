# RxJava-LiveData

为什么不直接将 RxJava 的 Observable/Flowable 暴露给 View 层，而是将结果包装成 LiveData

1. 这更符合 Google 的规范，使用 LiveData 将数据暴露给 View 层。
2. 后续 RxJava 可能将被替换为 kotlin 协程，View 层统一处理 LiveData，有助于后续迁移。
