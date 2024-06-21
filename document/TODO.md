# TODO

## UI 层

- [ ] 优化 fragment 转场动画
- [ ] StatusHeightView 优化 + 扩展 API
- [ ] StatusInsetsViewForAPI19
- [ ] 实现 doSafeFragmentOperation
- [x] RetainedStateHandlerBuilder【multi-state】
- [ ] Hook LifecycleBoundObserver，支持配置 shouldBeActive。参考 LiveBus
- [ ] 优化 epoxy list【load more item】
- [ ] 优化 segment list【load more item】
- [ ] OnBackPressedDispatcher
- [ ] Banner by ViewPager2
- [ ] MultiStateLayout 添加 OnStateViewCreatedListener
- [ ] Dialog API 2.0（DSL）
- [ ] 移除 QMUI
- [ ] 拆出 Gallery
- [ ] 拆出 WebView
- [ ] 拆出 upgrade
- [ ] 拆出 debug
- [ ] 实现 sample 模块
- [ ] Common Core 中相关的代码抽离到 UI 模块中。

## 网络层

- OkHttp 支持按模块或接口配置拦截器；
- Retrofit：Hook Converter.Factory，处理返回数据的统一格式；参考 ：[Design WanAndroid](https://github.com/Lowae/Design-WanAndroid)
