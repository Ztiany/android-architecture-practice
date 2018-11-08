package com.android.sdk.social.wechat;


import com.tencent.mm.opensdk.modelmsg.SendAuth;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-07 14:36
 */
class WeChatLoginImpl {

    static Observable<WXUser> doWeChatLogin(SendAuth.Resp resp) {
        final WXApiFactory.ServiceApi serviceApi = WXApiFactory.createWXApi();
        return serviceApi
                .getAccessToken(WeChatPlatformManager.getAppId(), WeChatPlatformManager.getAppSecret(), resp.code, "authorization_code"/*固定参数*/)
                .flatMap(new Function<WXToken, ObservableSource<WXUser>>() {
                    @Override
                    public ObservableSource<WXUser> apply(WXToken wxToken) {
                        return serviceApi.getWeChatUser(wxToken.getAccess_token(), wxToken.getOpenid());
                    }
                });
    }

}
