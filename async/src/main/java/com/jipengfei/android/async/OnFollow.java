package com.jipengfei.android.async;

/**
 * Created by jibuji on 15/10/11.
 */
public interface OnFollow<T> {
    void call(Follower<T> s);
}