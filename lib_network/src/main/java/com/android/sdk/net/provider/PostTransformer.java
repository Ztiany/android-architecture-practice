package com.android.sdk.net.provider;

import io.reactivex.FlowableTransformer;
import io.reactivex.ObservableTransformer;

/**
 * 经过 {@link com.android.sdk.net.RxNetKit} 处理网络结果后，可以添加此接口来添加统一的再处理逻辑，比如 token 实现后的重试。
 *
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-12-21 14:31
 */
public interface PostTransformer<Data> extends ObservableTransformer<Data, Data>, FlowableTransformer<Data, Data> {

}
