# Lib Media Selector

多媒体文件选择库。

## 1 应用内多媒体文件选择

### 基于 boxing 封装

由于 boxing 内部存在一些 bug，且官方不再维护，后改为本地依赖并对 bug 进行了修复。

boxing 库存在以下问题：

1. [RotatePhotoView](https://github.com/ChenSiLiang/RotatePhotoView) 依赖混乱导致的崩溃。
2. [Android 10 SQLiteException](https://github.com/bilibili/boxing/issues/154) 崩溃问题，解决方案可以参考  [replacement-for-group-by-in-contentresolver-query-in-android-q-android-10-a](https://stackoverflow.com/questions/60623594/replacement-for-group-by-in-contentresolver-query-in-android-q-android-10-a) 和 [https://stackoverflow.com/questions/56823336/query-mediastore-on-android-q](https://stackoverflow.com/questions/56823336/query-mediastore-on-android-q) 。
3. 没有适配 Android 10 的 ScopedStorage。

另外，使用时需要配置好 FileProvider：

```xml
<external-files-path name="app_external" path="/" />
```

### 其他可选方案

- [EasyPhotos](https://github.com/HuanTanSheng/EasyPhotos)
- [PictureSelector](https://github.com/LuckSiege/PictureSelector)
- [ImageSelector](https://github.com/smuyyh/ImageSelector)

## 2 使用系统内置组件进行文件选择

SystemMediaSelector 用于调用系统相机或 SAF 获取图片或文件。

需要考虑的问题：

1. Android 7.0 默认启动严苛模式对`file:`类 Uri 的限制。
2. 获取的图片方向问题，需要通过 exif 修正。
3. 系统返回的不是 file 路径，而是其他类型的uri，需要通过相关方法转换。

相关参考：

- [官方文档：拍照](https://developer.android.com/training/camera/photobasics)
- [Android. How to capture image from camera and store it in server?](https://stackoverflow.com/questions/53645370/android-how-to-capture-image-from-camera-and-store-it-in-server)
- [你需要知道的 Android 拍照适配方案](http://www.jianshu.com/p/f269bcda335f)
- [Android 调用系统相机和相册-填坑篇](http://wuxiaolong.me/2016/05/24/Android-Photograph-Album2/)
- [Android 大图裁剪](http://ryanhoo.github.io/blog/2014/06/03/the-ultimate-approach-to-crop-photos-on-android-2/)
- [get-filename-and-path-from-uri-from-media-store](https://stackoverflow.com/questions/3401579/get-filename-and-path-from-uri-from-mediastore)
- [how-to-get-the-full-file-path-from-uri](https://stackoverflow.com/questions/13209494/how-to-get-the-full-file-path-from-uri)

## 3 图片裁剪库

使用系统裁剪是发现不同设备厂商以及不同系统版本之间有这样那样的问题，于是决定内置图片裁剪库，可选裁剪库有：

- [uCrop](https://github.com/Yalantis/uCrop)
- [smartCropper](https://github.com/pqpo/SmartCropper)
- [simpleCropper](https://github.com/igreenwood/SimpleCropView)
- [Android-Image-Cropper](https://github.com/ArthurHub/Android-Image-Cropper)

目前采用的是 uCrop。
