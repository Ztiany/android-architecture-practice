package com.android.sdk.social;

/**
 * @author Ztiany
 * Email: ztiany3@gmail.com
 * Date : 2018-11-07 17:42
 */
public class Status<T> {

    private final T result;
    private final Throwable t;
    private final boolean requesting;

    private Status(T result, Throwable t, boolean requesting) {
        this.result = result;
        this.t = t;
        this.requesting = requesting;
    }

    public boolean isRequesting() {
        return requesting;
    }

    public boolean isError() {
        return t != null;
    }

    public boolean isSuccess() {
        return result != null;
    }

    public T getResult() {
        return result;
    }

    public Throwable getError() {
        return t;
    }

    public static <T> Status<T> success(T t) {
        return new Status<>(t, null, false);
    }

    public static <T> Status<T> error(Throwable throwable) {
        return new Status<>(null, throwable, false);
    }

    public static <T> Status<T> loading() {
        return new Status<>(null, null, true);
    }

    @Override
    public String toString() {
        return "Status{" +
                "result=" + result +
                ", t=" + t +
                ", requesting=" + requesting +
                '}';
    }
}
