# Android App Architecture Template

## 1 download

```shell
# base
git clone git@github.com:Ztiany/Android-Libs.git

# project
git clone git@github.com:Ztiany/AndroidArchitecture.git
```

make sure the projects in the same directory.

## 2 modules explanation

common modules: 

- lib_base：common util classes and base classes，including BaseActivity, BaseFragment, BaseStateFragment, BaseListFragment, BaseAdapter, MVVM, MVP, MultiStateView, Dagger2, AAC, RxJava2, Kotlin, etc.
- lib_media_selector：gets photos or videos from system.
- lib_qrcode：library for scanning and generating qrcode.
- lib_network：RxJava + Retrofit + OkHttp.
- lib_cache： DiskLruCache + MMKV.

business modules: 

- app: combine all of the business module.
- module_base
- module_account
- module_home

## 3 architecture 

refer android architecture parts in [notes](https://github.com/Ztiany/notes)。
