package com.app.base.router;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-04-12 18:10
 */
public interface AppNavigationCallback extends NavigationCallback {

    @Override
    void onFound(Postcard postcard);

    @Override
    void onLost(Postcard postcard);

    @Override
    void onArrival(Postcard postcard);

    @Override
    void onInterrupt(Postcard postcard);

}
