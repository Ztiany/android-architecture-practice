package com.android.base.rxjava.retrial;

public interface RetryChecker {

    boolean doRetry(Throwable throwable);

}