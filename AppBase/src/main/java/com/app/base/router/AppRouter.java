package com.app.base.router;

import android.net.Uri;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-11-04 13:44
 */
public interface AppRouter {

    IPostcard build(String path);

    IPostcard build(Uri path);

    void inject(Object object);

    <T extends IProvider> T navigation(Class<T> iProviderClass);

}
