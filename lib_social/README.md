## 集成

```groovy
//rootProject

        // 注册MobSDK
        classpath "com.mob.sdk:MobSDK:2018.0319.1724"


```

## 链接

- [ShareSDK 集成文档](http://wiki.mob.com/%E5%AE%8C%E6%95%B4%E9%9B%86%E6%88%90%E6%96%87%E6%A1%A3%EF%BC%88gradle%EF%BC%89/)
- [Github：ShareSDK-For-Android](https://github.com/MobClub/ShareSDK-for-Android)


## 说明

微信分享需要在包名类添加一个名为 `WXEntryActivity` 的 Activity，并继承 WechatHandlerActivity。

```java
/**
 * 微信分享回调
 *
 * @author Ztiany
 */
@SuppressWarnings("all")
public class WXEntryActivity extends WechatHandlerActivity {


}
```

manifest 配置参考

```xml
        <!--ShareSDK 其他分享回调处理-->
        <activity
            android:name="com.mob.tools.MobUIShell"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"
            android:windowSoftInputMode="stateHidden|adjustResize">
            <!--QQ-->
            <intent-filter>
                <data android:scheme="qq_id"/>
                <action android:name="android.intent.action.VIEW"/>

                <category android:name="android.intent.category.BROWSABLE"/>
                <category android:name="android.intent.category.DEFAULT"/>
            </intent-filter>
        </activity>

        <!--微信分享回调-->
        <activity
            android:name="应用包名.wxapi.WXEntryActivity"
            android:configChanges="keyboardHidden|orientation|screenSize"
            android:exported="true"
            android:screenOrientation="portrait"
            android:theme="@android:style/Theme.Translucent.NoTitleBar"/>
```