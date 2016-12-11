package com.jipengfei.android.async;

/**
 * Created by jibuji on 15/10/10.
 */
public interface Result<T> {
    void onResult(T t);
    void onError(Throwable e);
}
