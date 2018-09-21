package com.eclite.library.components.net

import com.android.base.kotlin.otherwise
import com.android.base.kotlin.yes
import com.android.base.utils.functional.Optional
import com.app.base.data.net.exception.NetworkErrorException
import com.blankj.utilcode.util.NetworkUtils
import io.reactivex.Observable
import io.reactivex.functions.Consumer


/**
 * <pre>
 * 1，如果网络不可用，直接返回缓存，如果没有缓存，报错没有网络连接
 * 2，如果存在网络
 * 2.1，如果没有缓存，则从网络获取
 * 2.1，如果有缓存，则先返回缓存，然后从网络获取
 * 2.1，对比缓存与网络数据，如果没有更新，则忽略
 * 2.1，如果有更新，则更新缓存，并返回网络数据
</pre> *
 *
 * @param remote     网络数据源
 * @param local      本地数据源
 * @param onNewData  当有更新时，返回新的数据，可以在这里存储
 * @param <T>        数据类型
 * @param isNew 比较器，当comparator.compare(local, remote) = -1，表示有更新
 * @return 组合后的Observable
</T> */
fun <T> composeMultiSource(remote: Observable<Optional<T>>, local: Observable<Optional<T>>,
                           isNew: (oldT: T, newT: T) -> Boolean,
                           onNewData: Consumer<T>): Observable<Optional<T>> {
    if (!NetworkUtils.isConnected()) {
        return local
                .flatMap {
                    it.isPresent.yes { Observable.just(it) }.otherwise { Observable.error(NetworkErrorException()) }
                }
    }
    //有网络
    val sharedLocal = local.replay()
    sharedLocal.connect()

    val complexRemote = sharedLocal
            .flatMap { localData ->
                //没有缓存
                if (!localData.isPresent) {
                    remote.doOnNext {
                        it.isPresent.yes { onNewData.accept(it.get()) }
                    }
                } else/*有缓存，不触发错误，只有在过期时返回新的数据*/ {
                    remote
                            .onErrorResumeNext(Observable.empty())
                            .filter {
                                it.isPresent && isNew(localData.get(), it.get())
                            }.doOnNext {
                                onNewData.accept(it.get())
                            }
                }
            }

    return Observable.concat(sharedLocal.filter { it.isPresent }, complexRemote)
}
