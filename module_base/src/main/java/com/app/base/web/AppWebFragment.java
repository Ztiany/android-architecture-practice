package com.app.base.web;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.android.base.utils.common.StringChecker;

/**
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-09-26 14:23
 */
public class AppWebFragment extends BaseWebFragment {

    private static final String URL_KEY = "url_key";

    public static AppWebFragment newInstance(String targetUrl) {
        AppWebFragment mallWebFragment = new AppWebFragment();
        Bundle args = new Bundle();
        args.putString(URL_KEY, targetUrl);
        mallWebFragment.setArguments(args);
        return mallWebFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listenUserStatus();
    }

    private void listenUserStatus() {
        //用户登录后需要刷新
       /* AppContext.getContext().userDataSource()
                .subscribeUser()
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(user -> {
                    if (user != null) {
                        refresh();
                    }
                }, RxUtils.logErrorHandler());*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            String url = getArguments().getString(URL_KEY);
            if (!StringChecker.isEmpty(url)) {
                startLoad(url);
            }
        }

    }

    @Override
    protected boolean autoHandleTitle() {
        return true;
    }
}
