package com.app.base.widget.address;

import com.android.base.utils.BaseUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Ztiany
 * @version 1.0
 * Email: 1169654504@qq.com
 */
final class AddressInquirers {

    private static final String CHINA_ADDRESS_PATH = "address.json";

    private AddressQueryCallback mAddressQueryCallback;
    private Disposable mDisposable;

    void setAddressQueryCallback(AddressQueryCallback addressQueryCallback) {
        mAddressQueryCallback = addressQueryCallback;
    }

    public void start() {
        mDisposable = Observable.fromCallable(this::getAddressJson)
                .subscribeOn(Schedulers.io())
                .map(this::formJson)
                .observeOn(AndroidSchedulers.mainThread())
                .onTerminateDetach()
                .subscribe(provinces -> {
                    if (mAddressQueryCallback != null) {
                        List<IName> names = new ArrayList<>(provinces);
                        mAddressQueryCallback.onGetAddress(names);
                    }
                });
    }

    interface AddressQueryCallback {
        void onGetAddress(List<IName> names);
    }

    private String getAddressJson() {
        try {
            return IOUtils.convertToString(BaseUtils.getAssets().open(CHINA_ADDRESS_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private List<AddressItem> formJson(String content) {

        final Type type = new TypeToken<List<AddressItem>>() {
        }.getType();

        return new Gson().fromJson(content, type);
    }

    void destroy() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

}