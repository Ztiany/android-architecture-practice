package com.app.base.data.net;

import android.support.annotation.NonNull;

import com.android.base.utils.functional.Optional;


/**
 * 用于处理 Retrofit + RxJava2 网络请求返回的结果
 *
 * @author Ztiany
 * Email: 1169654504@qq.com
 * Date : 2017-11-22 17:22
 */
public class RxNetUtils {

    private static final DefaultApiChecker DEFAULT_API_CHECKER = new DefaultApiChecker();

    private static final DataTransformer DATA_TRANSFORMER = new DataTransformer(DEFAULT_API_CHECKER);

    private static final OptionalDataTransformer OPTIONAL_TRANSFORMER = new OptionalDataTransformer(DEFAULT_API_CHECKER);

    private static final ResultChecker RESULT_CHECKER = new ResultChecker();

    /**
     * 返回一个Transformer，用于统一处理网络请求返回的 Observer 数据。对网络异常和请求结果做了通用处理：
     * <pre>
     * 1.  网络无连接抛出 {@link com.eclite.library.components.net.exception.NetworkErrorException} 由下游处理
     * 2. HttpResult==null 抛出 {@link com.eclite.library.components.net.exception.NetworkErrorException} 由下游处理
     * 3. HttpResult.getCode() != SUCCESS 抛出 {@link com.eclite.library.components.net.exception.ApiErrorException} 由下游处理
     * 4. 返回的结果不符合约定的数据模型处理或为 null 抛出 {@link com.eclite.library.components.net.exception.ServerErrorException} 由下游处理
     * 5. 最后把 HttpResult&lt;T&gt; 中的数据 T 提取到下游
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static <Upstream> HttpResultTransformer<Upstream, Upstream> resultExtractor() {
        return (HttpResultTransformer<Upstream, Upstream>) DATA_TRANSFORMER;
    }

    /**
     * 与{@link #resultExtractor()}的行为类型，但是最后把 HttpResult&lt;T&gt; 中的数据 T 用 {@link Optional} 包装后再转发到下游。
     * 适用于 HttpResult.getData() 可以为 Null 的情况
     */
    @SuppressWarnings("unchecked")
    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>> optionalExtractor() {
        return (HttpResultTransformer<Upstream, Optional<Upstream>>) OPTIONAL_TRANSFORMER;
    }

    /**
     * 不提取 HttpResult&lt;T&gt; 中的数据 T，只进行网络异常、空数据异常、错误JSON格式异常处理。
     */
    @SuppressWarnings("unchecked")
    public static <Upstream> HttpResultTransformer<Upstream, HttpResult<Upstream>> resultChecker() {
        return (HttpResultTransformer<Upstream, HttpResult<Upstream>>) RESULT_CHECKER;
    }
    
    /**
     * 某些业务调用所产生的异常不是全局通用的，可以传递此接口用于创建特定的异常
     */
    public interface ExceptionFactory {
        /**
         * 根据{@link HttpResult}创建特定的业务异常
         */
        Exception create(HttpResult httpResult);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream> newExtractor(ExceptionFactory exceptionFactory) {
        return newExtractor(exceptionFactory, DEFAULT_API_CHECKER);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream> newExtractor(ApiChecker apiChecker) {
        return newExtractor(null, apiChecker);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Upstream> newExtractor(ExceptionFactory exceptionFactory, ApiChecker apiChecker) {
        return new DataTransformer<Upstream>(apiChecker) {
            @Override
            protected Throwable createException(@NonNull HttpResult<Upstream> rHttpResult) {
                if (exceptionFactory == null) {
                    return super.createException(rHttpResult);
                }
                return exceptionFactory.create(rHttpResult);
            }
        };
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>> newOptionalExtractor(ExceptionFactory exceptionFactory) {
        return newOptionalExtractor(exceptionFactory, DEFAULT_API_CHECKER);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>> newOptionalExtractor(ApiChecker apiChecker) {
        return newOptionalExtractor(null, apiChecker);
    }

    public static <Upstream> HttpResultTransformer<Upstream, Optional<Upstream>> newOptionalExtractor(ExceptionFactory exceptionFactory, ApiChecker apiChecker) {
        return new OptionalDataTransformer<Upstream>(apiChecker) {
            @Override
            protected Throwable createException(@NonNull HttpResult<Upstream> rHttpResult) {
                if (exceptionFactory == null) {
                    return super.createException(rHttpResult);
                }
                return exceptionFactory.create(rHttpResult);
            }
        };
    }

}
