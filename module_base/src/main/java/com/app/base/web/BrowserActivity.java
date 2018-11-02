package com.app.base.web;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.base.app.fragment.tools.FragmentHelper;
import com.app.base.R;
import com.app.base.app.AppBaseActivity;
import com.app.base.router.RouterPath;

/**
 * 用于展示 WEB 页面，与APP运行在同一个进程中
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-09-26 14:22
 */
@Route(path = RouterPath.Browser.PATH)
public class BrowserActivity extends AppBaseActivity {

    @Autowired(name = RouterPath.Browser.URL_KEY)
    String targetUrl;

    @Override
    protected Object layout() {
        return R.layout.app_base_activity;
    }

    @Override
    protected boolean hasRouterArguments() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            FragmentHelper.replace(R.id.common_container_id, getSupportFragmentManager(), AppWebFragment.newInstance(targetUrl));
        }
    }
}
