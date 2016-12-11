package com.jipengfei.android.async;

/**
 * Created by jibuji on 15/10/11.
 */
public interface Follower<T> {
    void onNext(T t);

    void onError(Throwable e);
}
