package com.android.sdk.social.share;

import com.android.sdk.social.Platforms;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-06 14:55
 */
public class SharePlatformDevInfo {

    final int mPlatformId;

    private HashMap<String, Object> mDevInfo = new HashMap<>();

    {
        //默认不绕过
        mDevInfo.put("BypassApproval", Boolean.toString(false));
        //默认开启
        mDevInfo.put("Enable", Boolean.toString(true));
    }

    /**
     * @param platformId {@link Platforms} 中定义的平台标识。
     */
    public static SharePlatformDevInfo newDevInfo(int platformId) {
        return new SharePlatformDevInfo(platformId);
    }

    private SharePlatformDevInfo(int platformId) {
        this.mPlatformId = platformId;
        //新浪默认字段
        if (mPlatformId == Platforms.SINA) {
            mDevInfo.put("RedirectUrl", "http://sns.whalecloud.com/sina2/callback");
            mDevInfo.put("ShareByAppClient", Boolean.toString(true));
        }
    }

    /**
     * @param id 数字，平台的id，可以设置为任何不重复的数字,(可选字段)
     */
    public SharePlatformDevInfo setId(String id) {
        mDevInfo.put("Id", id);
        return this;
    }

    /**
     * @param sortId 数字，九宫格界面平台的排序，越小越靠前，可以设置为任何不重复的数字,(可选字段)
     */
    public SharePlatformDevInfo setSortId(int sortId) {
        mDevInfo.put("SortId", String.valueOf(sortId));
        return this;
    }

    /**
     * @param appId 文本，对应ShareSDK.xml中的AppId、ClientID、ApplicationId、ChannelID
     */
    public SharePlatformDevInfo setAppId(String appId) {
        mDevInfo.put("AppId", appId);
        return this;
    }

    /**
     * @param appKey 文本，对应ShareSDK.xml中的AppKey、ConsumerKey、ApiKey、OAuthConsumerKey
     */
    public SharePlatformDevInfo setAppKey(String appKey) {
        mDevInfo.put("AppKey", appKey);
        return this;
    }

    /**
     * @param appSecret 文本，对应ShareSDK.xml中的AppSecret、ConsumerSecret、SecretKey、Secret、ClientSecret、ApiSecret、ChannelSecret
     */
    public SharePlatformDevInfo setAppSecret(String appSecret) {
        mDevInfo.put("AppSecret", appSecret);
        return this;
    }

    /**
     * @param redirectUrl 新浪微博的回调地址，可以不设置。
     */
    public SharePlatformDevInfo setRedirectUrl(String redirectUrl) {
        mDevInfo.put("RedirectUrl", redirectUrl);
        return this;
    }

    /**
     * 绕过审核，默认为 false。
     */
    public SharePlatformDevInfo setBypassApproval(boolean bypassApproval) {
        mDevInfo.put("BypassApproval", Boolean.toString(bypassApproval));
        return this;
    }

    public SharePlatformDevInfo setEnable(boolean enable) {
        mDevInfo.put("Enable", Boolean.toString(enable));
        return this;
    }

    void setup(String platform) {
        ShareSDK.setPlatformDevInfo(platform, mDevInfo);
    }

}
