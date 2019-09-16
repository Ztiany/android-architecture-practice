package com.app.base.router;

import android.net.Uri;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-11-04 13:45
 */
public class AppRouterImpl implements AppRouter {

    @Override
    public IPostcard build(String path) {
        return RouterManager.build(path);
    }

    @Override
    public IPostcard build(Uri path) {
        return RouterManager.build(path);
    }

    @Override
    public void inject(Object object) {
        RouterManager.inject(object);
    }

    @Override
    public <T extends IProvider> T navigation(Class<T> iProviderClass) {
        return RouterManager.navigation(iProviderClass);
    }

}
