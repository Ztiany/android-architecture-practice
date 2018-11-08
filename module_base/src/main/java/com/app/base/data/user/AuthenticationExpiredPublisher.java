package com.app.base.data.user;

import com.android.base.rx.RxBus;

import io.reactivex.Flowable;

/**
 * 表示认证过期，通过 RxBus 进行订阅此事件。
 *
 * @author Ztiany
 */
public class AuthenticationExpiredPublisher {

    private AuthenticationExpiredPublisher() {
    }

    static void publishAuthenticationExpired() {
        RxBus.getDefault().send(new AuthenticationExpiredPublisher());
    }

    public static Flowable<AuthenticationExpiredPublisher> onAuthenticationExpired() {
        return RxBus.getDefault().toObservable(AuthenticationExpiredPublisher.class);
    }

}
