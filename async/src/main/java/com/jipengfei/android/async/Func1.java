package com.jipengfei.android.async;

/**
 * Created by jibuji on 15/10/9.
 */
public interface Func1<T, R> {
    R run(T t) throws Throwable;
}
