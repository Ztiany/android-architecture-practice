package com.app.base.web;

import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.Arrays;

import timber.log.Timber;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-01-24 14:30
 */
public class JsBridgeHandler {

    private static final String JS_DIVIDER = "#A#";
    private static final String JS_FLAG = "callAndroid";
    private static final String TAG = JsBridgeHandler.class.getSimpleName();

    private final BaseWebFragment mFragment;
    private JsCallInterceptor mJsCallInterceptor;

    JsBridgeHandler(BaseWebFragment fragment) {
        mFragment = fragment;
    }

    boolean handleJsCall(String message) {
        if (TextUtils.isEmpty(message)) {
            return false;
        }
        if (!message.startsWith(JS_FLAG)) {
            return false;
        }
        String[] split = message.split(JS_DIVIDER);
        Log.d(TAG, "split:" + Arrays.toString(split));

        if (split.length <= 1) {
            return false;
        }

        String method = split[1];
        String[] args = split.length == 2 ? null : Arrays.copyOfRange(split, 2, split.length);

        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            if (mFragment.getActivity() != null) {
                mFragment.getActivity().runOnUiThread(() -> dispatchJsMethodCall(method, args));
            }
        } else {
            dispatchJsMethodCall(method, args);
        }
        return true;
    }

    private void dispatchJsMethodCall(String method, @Nullable String[] args) {
        Timber.d("dispatchJsMethodCall() called with: method = [" + method + "], args = [" + Arrays.toString(args) + "]");
        if (mJsCallInterceptor == null || !mJsCallInterceptor.intercept(method, args)) {
            innerProcess(method, args);
        }
    }

    public interface JsCallInterceptor {
        boolean intercept(String method, @Nullable String[] args);
    }

    public void setJsCallInterceptor(JsCallInterceptor jsCallInterceptor) {
        mJsCallInterceptor = jsCallInterceptor;
    }

    private void innerProcess(String method, String[] args) {
        Runnable action = JsActionFactory.createAction(mFragment, method, args);
        if (action != null) {
            action.run();
        }
    }

}
