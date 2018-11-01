package com.app.base.data.net;

import android.support.annotation.NonNull;

import com.android.base.utils.functional.Optional;
import com.app.base.data.net.exception.ApiErrorException;
import com.app.base.data.net.exception.NetworkErrorException;
import com.app.base.data.net.exception.ServerErrorException;
import com.blankj.utilcode.util.NetworkUtils;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

class DataTransformer<Upstream> extends HttpResultTransformer<Upstream, Upstream> {
    DataTransformer(ApiChecker apiChecker) {
        super(true, apiChecker, HttpResult::getData);
    }
}

class OptionalDataTransformer<Upstream> extends HttpResultTransformer<Upstream, Optional<Upstream>> {
    OptionalDataTransformer(ApiChecker apiChecker) {
        super(false, apiChecker, rHttpResult -> Optional.ofNullable(rHttpResult.getData()));
    }
}

class ResultChecker<Upstream> extends HttpResultTransformer<Upstream, HttpResult<Upstream>> {

    ResultChecker() {
        super(false, httpResult -> true, rHttpResult -> rHttpResult);
    }
}

class DefaultApiChecker implements ApiChecker {

    @Override
    public boolean isSuccess(HttpResult httpResult) {
        return ApiHelper.isSuccess(httpResult);
    }
}

public class HttpResultTransformer<Upstream, Downstream> implements ObservableTransformer<HttpResult<Upstream>, Downstream>,
        FlowableTransformer<HttpResult<Upstream>, Downstream> {

    private final boolean mRequestNonNullData;
    private final DataExtractor<Downstream, Upstream> mDataExtractor;
    private final ApiChecker mApiChecker;

    interface DataExtractor<S, T> {
        S getDataFromHttpResult(HttpResult<T> rHttpResult);
    }

    HttpResultTransformer(boolean requestNonNullData, @NonNull ApiChecker apiChecker, @NonNull DataExtractor<Downstream, Upstream> dataExtractor) {
        mRequestNonNullData = requestNonNullData;
        mDataExtractor = dataExtractor;
        mApiChecker = apiChecker;
    }

    private static boolean isConnected() {
        return NetworkUtils.isConnected();
    }

    @Override
    public Publisher<Downstream> apply(Flowable<HttpResult<Upstream>> upstream) {
        return upstream.map(this::processData);
    }

    @Override
    public ObservableSource<Downstream> apply(Observable<HttpResult<Upstream>> upstream) {
        return upstream.map(this::processData);
    }

    private Downstream processData(HttpResult<Upstream> rHttpResult) {
        if (rHttpResult == null) {

            if (isConnected()) {
                throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));//有连接无数据，服务器错误
            } else {
                throw new NetworkErrorException();//无连接网络错误
            }

        } else if (ApiHelper.isDataFormatError(rHttpResult)) {

            throwAs(new ServerErrorException(ServerErrorException.SERVER_DATA_ERROR));//服务器数据格式错误

        } else if (!mApiChecker.isSuccess(rHttpResult)) {//检测响应码是否正确
            throwAs(createException(rHttpResult));
        }

        if (mRequestNonNullData) {
            if (rHttpResult.getData() == null) {//如果约定必须返回的数据却没有返回数据，则认为是服务器错误
                throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));
            }
        }

        return mDataExtractor.getDataFromHttpResult(rHttpResult);
    }

    protected Throwable createException(@NonNull HttpResult<Upstream> rHttpResult) {
        return new ApiErrorException(rHttpResult.getCode(), rHttpResult.getMsg());
    }

    @SuppressWarnings("unchecked")
    private <T extends Throwable> void throwAs(Throwable throwable) throws T {
        throw (T) throwable;
    }

}


