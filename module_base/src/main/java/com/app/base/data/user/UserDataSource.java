package com.app.base.data.user;

import com.gwchina.sdk.base.data.models.User;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-01 17:38
 */
public interface UserDataSource {

    /**
     * 同步获取获取用户是否已经登录，判断条件为{@link #user()}返回值不为null
     *
     * @return true 表示已经登录
     */
    boolean isUserLogined();

    /**
     * 同步获取用户信息。
     * <pre>
     *     大部分情况下此方法从内存中读取用户信息，如果内存中没有用户信息才会从本地缓存加载
     * </pre>
     *
     * @return 用户信息
     */
    User user();

    /**
     * 观察用户信息，当用户信息被修改后，总是可以得到通知，这是一个全局多播的观察者，注意在不需要观察用户信息的时候取消订阅
     *
     * @return 数据源
     */
    Observable<User> subscribeUser();

    /**
     * 保存用户信息
     *
     * @param user 用户信息
     */
    void saveUser(User user);

    /**
     * 远程同步用户信息
     */
    void syncUserData();

    /**
     * 退出登录
     *
     * @return 结果
     */
    Completable logout();

}
