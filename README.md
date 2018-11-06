# Android App Architecture Template

## download

```shell
git clone git@github.com:Ztiany/AndroidArchitecture.git
cd AndroidArchitecture
git submodule init
git submodule update
```


## 模块说明

业务无关基础类库：

- lib_base：通用基础功能库，包括 BaseActivity、BaseFragment、BaseAdapter、MVVM等通用组件。
- lib_media_selector：图片、视频选择器，调用系统相机获取照片。
- lib_qrcode：二维码扫描与生成。

业务模块

- app：壳工程，不包含具体的业务实现，用于集成所有的业务组件
- lib_security：虽然是lib，但是是与业务相关的，用于存储敏感信息和提供加密组件。
- module_base：基于业务封装的基础模块，所有业务组件都需要依赖此模块
- module_home：首页组件
- lib_social：第三方分享、登录

todo（待集成）

- lib_pay：第三方支付
- lib_push：第三方推送
- lib_map：地图
- lib_statistics：统计

## todo

- 抽离网络层，HttpResult 接口化，使用 Gson 动态注册 HttpResult 的解析器。