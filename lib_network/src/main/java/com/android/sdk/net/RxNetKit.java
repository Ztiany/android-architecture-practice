package com.android.sdk.net;

import android.support.annotation.NonNull;

import com.android.sdk.functional.Optional;
import com.android.sdk.net.core.HttpResultTransformer;
import com.android.sdk.net.core.Result;
import com.android.sdk.net.exception.NetworkErrorException;

import java.util.Comparator;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;

/**
 * 用于处理 Retrofit + RxJava2 网络请求返回的结果
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-11-22 17:22
 */
public class RxNetKit {

    static class ResultTransformer<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, Upstream, T> {
        ResultTransformer() {
            super(true, Result::getData);
        }
    }

    static class OptionalResultTransformer<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, Optional<Upstream>, T> {
        OptionalResultTransformer() {
            super(false, rResult -> Optional.ofNullable(rResult.getData()));
        }
    }

    static class ResultChecker<Upstream, T extends Result<Upstream>> extends HttpResultTransformer<Upstream, T, T> {
        @SuppressWarnings("unchecked")
        ResultChecker() {
            super(false, rResult -> (T) rResult);
        }
    }

    /**
     * 某些业务调用所产生的异常不是全局通用的，可以传递此接口用于创建特定的异常
     */
    public interface ExceptionFactory {
        /**
         * 根据{@link Result}创建特定的业务异常
         */
        Exception create(Result result);
    }

    private static final ResultTransformer DATA_TRANSFORMER = new ResultTransformer();

    private static final OptionalResultTransformer OPTIONAL_TRANSFORMER = new OptionalResultTransformer();

    private static final ResultChecker RESULT_CHECKER = new ResultChecker();

    /**
     * 返回一个Transformer，用于统一处理网络请求返回的 Observer 数据。对网络异常和请求结果做了通用处理：
     * <pre>
     * 1.  网络无连接抛出 {@link com.android.sdk.net.exception.NetworkErrorException} 由下游处理
     * 2. HttpResult==null 抛出 {@link com.android.sdk.net.exception.NetworkErrorException} 由下游处理
     * 3. HttpResult.getCode() != SUCCESS 抛出 {@link com.android.sdk.net.exception.ApiErrorException} 由下游处理
     * 4. 返回的结果不符合约定的数据模型处理或为 null 抛出 {@link com.android.sdk.net.exception.ServerErrorException} 由下游处理
     * 5. 最后把 HttpResult&lt;T&gt; 中的数据 T 提取到下游
     * </pre>
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Upstream, T> _resultExtractor() {
        return (HttpResultTransformer<Upstream, Upstream, T>) DATA_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream, Result<Upstream>> resultExtractor() {
        return _resultExtractor();
    }

    /**
     * 与{@link #resultExtractor()}的行为类型，但是最后把 HttpResult&lt;T&gt; 中的数据 T 用 {@link Optional} 包装后再转发到下游。
     * 适用于 HttpResult.getData() 可以为 Null 的情况
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Optional<Upstream>, T> _optionalExtractor() {
        return (HttpResultTransformer<Upstream, Optional<Upstream>, T>) OPTIONAL_TRANSFORMER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>, Result<Upstream>> optionalExtractor() {
        return _optionalExtractor();
    }

    /**
     * 不提取 HttpResult&lt;T&gt; 中的数据 T，只进行网络异常、空数据异常、错误JSON格式异常处理。
     */
    @SuppressWarnings("unchecked")
    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, T, T> _resultChecker() {
        return (HttpResultTransformer<Upstream, T, T>) RESULT_CHECKER;
    }

    public static <Upstream> HttpResultTransformer<Upstream, Result<Upstream>, Result<Upstream>> resultChecker() {
        return _resultChecker();
    }

    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Upstream, T> _newExtractor(ExceptionFactory exceptionFactory) {
        return new ResultTransformer<Upstream, T>() {
            @Override
            protected Throwable createException(@NonNull Result<Upstream> rResult) {
                if (exceptionFactory == null) {
                    return super.createException(rResult);
                }
                return exceptionFactory.create(rResult);
            }
        };
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream, Result<Upstream>> newExtractor(ExceptionFactory exceptionFactory) {
        return _newExtractor(exceptionFactory);
    }

    private static <Upstream, T extends Result<Upstream>> HttpResultTransformer<Upstream, Optional<Upstream>, T> _newOptionalExtractor(ExceptionFactory exceptionFactory) {
        return new OptionalResultTransformer<Upstream, T>() {
            @Override
            protected Throwable createException(@NonNull Result<Upstream> rResult) {
                if (exceptionFactory == null) {
                    return super.createException(rResult);
                }
                return exceptionFactory.create(rResult);
            }
        };
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>, Result<Upstream>> newOptionalExtractor(ExceptionFactory exceptionFactory) {
        return _newOptionalExtractor(exceptionFactory);
    }

    /**
     * <pre>
     * 1. 如果网络不可用，直接返回缓存，如果没有缓存，报错没有网络连接
     * 2. 如果存在网络
     *      2.1 如果没有缓存，则从网络获取
     *      2.1 如果有缓存，则先返回缓存，然后从网络获取
     *      2.1 对比缓存与网络数据，如果没有更新，则忽略
     *      2.1 如果有更新，则更新缓存，并返回网络数据
     * </pre>
     *
     * @param remote    网络数据源
     * @param local     本地数据源
     * @param onNewData 当有更新时，返回新的数据，可以在这里存储
     * @param <T>       数据类型
     * @param isNew     比较器，当 comparator.compare(local, remote) < 0，表示有更新
     * @return 组合后的Observable
     * </T>
     */
    public static <T> Observable<Optional<T>> composeMultiSource(Observable<Optional<T>> remote, Observable<Optional<T>> local, Comparator<T> isNew, Consumer<T> onNewData) {
        if (!NetContext.get().connected()) {
            return local.flatMap((Function<Optional<T>, ObservableSource<Optional<T>>>) tOptional -> {
                if (tOptional.isPresent()) {
                    return Observable.just(tOptional);
                } else {
                    return Observable.error(new NetworkErrorException());
                }
            });
        }

        //有网络
        ConnectableObservable<Optional<T>> sharedLocal = local.replay();
        sharedLocal.connect();

        //组合数据
        Observable<Optional<T>> complexRemote = sharedLocal
                .flatMap((Function<Optional<T>, ObservableSource<Optional<T>>>) localData -> {
                    //没有缓存
                    if (!localData.isPresent()) {
                        return remote.doOnNext(tOptional -> {
                            if (tOptional.isPresent()) {
                                onNewData.accept(tOptional.get());
                            }
                        });
                    }
                    /*有缓存，不触发错误，只有在过期时返回新的数据*/
                    return remote
                            .onErrorResumeNext(Observable.empty())
                            .filter(remoteData -> remoteData.isPresent() && isNew.compare(localData.get(), remoteData.get()) < 0)
                            .doOnNext(newData -> onNewData.accept(newData.get()));
                });

        return Observable.concat(sharedLocal.filter(Optional::isPresent), complexRemote);
    }

}
