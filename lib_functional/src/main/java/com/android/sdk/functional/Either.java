package com.android.sdk.functional;

/**
 * Reference: https://www.ibm.com/developerworks/cn/java/j-ft13/
 */
public class Either<A, B> {

    private A left = null;
    private B right = null;

    private Either(A a, B b) {
        left = a;
        right = b;
    }

    public static <A, B> Either<A, B> left(A a) {
        return new Either<A, B>(a, null);
    }

    public A left() {
        return left;
    }

    public boolean isLeft() {
        return left != null;
    }

    public boolean isRight() {
        return right != null;
    }

    public B right() {
        return right;
    }

    public static <A, B> Either<A, B> right(B b) {
        return new Either<A, B>(null, b);
    }

}