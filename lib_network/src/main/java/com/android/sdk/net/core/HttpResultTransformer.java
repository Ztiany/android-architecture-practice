package com.android.sdk.net.core;

import android.support.annotation.NonNull;

import com.android.sdk.net.NetContext;
import com.android.sdk.net.exception.ApiErrorException;
import com.android.sdk.net.exception.NetworkErrorException;
import com.android.sdk.net.exception.ServerErrorException;
import com.android.sdk.net.provider.ApiHandler;
import com.android.sdk.net.provider.PostTransformer;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;

public class HttpResultTransformer<Upstream, Downstream, T extends Result<Upstream>> implements ObservableTransformer<T, Downstream>, FlowableTransformer<T, Downstream> {

    private final boolean mRequestNonNullData;
    private final DataExtractor<Downstream, Upstream> mDataExtractor;

    public interface DataExtractor<S, T> {
        S getDataFromHttpResult(Result<T> rResult);
    }

    public HttpResultTransformer(boolean requestNonNullData, @NonNull DataExtractor<Downstream, Upstream> dataExtractor) {
        mRequestNonNullData = requestNonNullData;
        mDataExtractor = dataExtractor;
    }

    @Override
    public Publisher<Downstream> apply(Flowable<T> upstream) {
        Flowable<Downstream> downstreamFlowable = upstream.map(this::processData);
        @SuppressWarnings("unchecked")
        PostTransformer<Downstream> postTransformer = (PostTransformer<Downstream>) NetContext.get().netProvider().postTransformer();
        if (postTransformer != null) {
            return downstreamFlowable.compose(postTransformer);
        } else {
            return downstreamFlowable;
        }
    }

    @Override
    public ObservableSource<Downstream> apply(Observable<T> upstream) {
        Observable<Downstream> downstreamObservable = upstream.map(this::processData);
        @SuppressWarnings("unchecked")
        PostTransformer<Downstream> postTransformer = (PostTransformer<Downstream>) NetContext.get().netProvider().postTransformer();
        if (postTransformer != null) {
            return downstreamObservable.compose(postTransformer);
        } else {
            return downstreamObservable;
        }
    }

    private static boolean isConnected() {
        return NetContext.get().connected();
    }

    private Downstream processData(Result<Upstream> rResult) {
        if (rResult == null) {

            if (isConnected()) {
                throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));//有连接无数据，服务器错误
            } else {
                throw new NetworkErrorException();//无连接网络错误
            }

        } else if (NetContext.get().netProvider().errorDataAdapter().isErrorDataStub(rResult)) {

            throwAs(new ServerErrorException(ServerErrorException.SERVER_DATA_ERROR));//服务器数据格式错误

        } else if (!rResult.isSuccess()) {//检测响应码是否正确
            ApiHandler apiHandler = NetContext.get().netProvider().aipHandler();
            if (apiHandler != null) {
                apiHandler.onApiError(rResult);
            }
            throwAs(createException(rResult));
        }

        if (mRequestNonNullData) {
            if (rResult.getData() == null) {//如果约定必须返回的数据却没有返回数据，则认为是服务器错误
                throwAs(new ServerErrorException(ServerErrorException.UNKNOW_ERROR));
            }
        }

        return mDataExtractor.getDataFromHttpResult(rResult);
    }

    protected Throwable createException(@NonNull Result<Upstream> rResult) {
        return new ApiErrorException(rResult.getCode(), rResult.getMessage());
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void throwAs(Throwable throwable) throws E {
        throw (E) throwable;
    }

}


