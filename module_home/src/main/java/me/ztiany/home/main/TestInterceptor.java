package me.ztiany.home.main;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;


/**
 * fix ARouter 编译bug
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-02 16:39
 */
@Interceptor(priority = 1)
public class TestInterceptor implements IInterceptor {

    @Override
    public void process(Postcard postcard, InterceptorCallback callback) {

    }

    @Override
    public void init(Context context) {

    }

}
