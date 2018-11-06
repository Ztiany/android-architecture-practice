package com.android.sdk.social.share;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.android.sdk.social.Platforms;
import com.mob.MobSDK;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import static com.android.sdk.social.Platforms.MOMENTS;
import static com.android.sdk.social.Platforms.QQ;
import static com.android.sdk.social.Platforms.WECHAT;


/**
 * 分享相关 SDK 封装
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2016-11-09 11:08
 */
public class SocialShareHelper {

    /**
     * 初始化SDK，必须首先调用。
     *
     * @param context   一个上下文。
     * @param appKey    ShareSDK 平台的 appKey。
     * @param appSecret ShareSDK 平台的 appSecret。
     */
    public static void init(Context context, String appKey, String appSecret) {
        MobSDK.init(context, appKey, appKey);
    }

    /**
     * 初始化使用到的平台信息，比如在微信平台注册应用后，得到的 appKey 。
     *
     * @param list 各个平台信息列表。
     */
    public static void initPlatformDevInfo(List<SharePlatformDevInfo> list) {
        if (list != null) {
            for (SharePlatformDevInfo sharePlatformDevInfo : list) {
                sharePlatformDevInfo.setup(mapPlatformId(sharePlatformDevInfo.mPlatformId));
            }
        }
    }

    /**
     * 使用默认的对话框展示一个分享列表，供用户选择分享到的平台。
     *
     * @param activity  当前 Activity
     * @param share     分享内容
     * @param platforms 平台列表
     */
    public static void showDefaultShareDialog(final FragmentActivity activity, final Share share, List<SharePlatform> platforms) {
        ShareFragmentDialog.newInstance(share, platforms)
                .show(activity.getSupportFragmentManager(), ShareFragmentDialog.class.getName());
    }

    /**
     * 直接处理分享动作。
     *
     * @param activity   当前 Activity。
     * @param platformId {@link Platforms} 中定义的平台标识。
     * @param share      分享的内容。
     */
    public static void handleShare(final Activity activity, final int platformId, final Share share) {

        OnekeyShare oks = new OnekeyShare();
        oks.setSilent(true);
        //setPlatform后，直接分享，而不展示九宫格。
        oks.setPlatform(mapPlatformId(platformId));

        //设置分享内容
        oks.setTitle(share.getTitle());//标题
        oks.setTitleUrl(share.getUrl());//连接
        oks.setText(share.getContent());//内容
        oks.setImageUrl(share.getImage());//图片
        oks.setUrl(share.getUrl());//连接

        //个别平台内容定制
        if (platformId == MOMENTS) {
            oks.setTitle(share.getTitle() + " : " + share.getContent());
        }

        oks.show(activity);
    }

    private static String mapPlatformId(int platformId) {
        switch (platformId) {
            case WECHAT: {
                return Wechat.NAME;
            }
            case MOMENTS: {
                return WechatMoments.NAME;
            }
            case QQ: {
                return cn.sharesdk.tencent.qq.QQ.NAME;
            }
        }
        throw new IllegalArgumentException("platformId can not be match");
    }


}
