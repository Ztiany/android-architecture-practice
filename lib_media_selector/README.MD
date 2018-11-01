# 图片选择库

    目前基于boxing修改


## 1 AndroidN 在FileProvider的xml配置中加入：

```        
    <cache-path name="internal" path="boxing" />
    <external-path name="external" path="DCIM/bili/boxing" />
```

## 2 So库


```
                defaultConfig {
                    ......

                      ndk {//只打包armeabi架构的so库
                          abiFilters 'armeabi'
                      }
                }
```

## 3 其他参考

-  [boxing](https://github.com/Bilibili/boxing)
-  [uCrop](https://github.com/Yalantis/uCrop)
-  [ImagePicker](https://github.com/jeasonlzy/ImagePicker)
