# Android App Architecture Practice

## 1 download

```shell
# base
# project
git clone git@github.com:Ztiany/AndroidArchitecture.git
cd AndroidArchitecture

git submodule init
git submodule update
```

## 2 explanation

[common modules](libraries):

- lib_foundation: DelegateFragment, DelegateActivity.
- lib_base: common util classes and base classes, including BaseActivity, BaseFragment, BaseStateFragment, BaseListFragment, BaseAdapter, MVVM, MVP, MultiStateView, Dagger2, AAC, RxJava2, Kotlin, etc.
- lib_media_selector: gets photos or videos from system.
- lib_qrcode: library for scanning and generating qrcode.
- lib_network: RxJava + Retrofit + OkHttp.
- lib_cache:  DiskLruCache + MMKV.
- lib_upgrade: A library for checking app upgrade.

business modules:

- app: assemble all of the business module.
- common_sdk: assemble all the libs.
- module_account: business module for login/register.
- module_home: business module.

## 3 feature planing

1. lib_base：File Operation【Scoped Storage】
2. lib_common_ui：ShapeableDrawable