# APP 基础模块

## 跨模块提供服务

有两种方式来向各个业务模块来提供服务：

1. 在 `common:api` 中定义服务接口，在 `common:core` 中提供实现，通过 hilt 暴露服务。【这种一般是共用的基础服务】
2. 业务模块通过 `common:api` 中的 AppServiceManager 暴露服务（也是基于 hilt） 。【这种一般某个业务模块需要对外提供的服务】
