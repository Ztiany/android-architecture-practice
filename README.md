# Android App Architecture Template

## 1 download

```shell
# base
git clone git@github.com:Ztiany/Android-Libs.git

# project
git clone git@github.com:Ztiany/AndroidArchitecture.git
```

## 2 explanation

业务无关基础类库：

- lib_base：通用基础功能库，包括 BaseActivity、BaseFragment、BaseAdapter、MVVM 等通用组件。
- lib_media_selector：图片、视频选择器，调用系统相机获取照片。
- lib_qrcode：二维码扫描与生成。
- lib_social：微信、QQ登录分享等。
- lib_network：RxJava + Retrofit + OkHttp 网络封装。

业务模块

- app：壳工程，不包含具体的业务实现，用于集成所有的业务组件。
- lib_security：虽然是lib，但是是与业务相关的，用于存储敏感信息和提供加密组件。
- module_base：基于业务封装的基础模块，所有业务组件都需要依赖此模块。
- module_home：首页组件。

## 3 架构说明

参考 [Android开发总结](https://github.com/Ztiany/Programming-Notes/blob/master/Android/README.md) 架构总结部分。
