package com.jipengfei.android.async;

/**
 * Created by jibuji on 15/10/11.
 */
public interface AsyncAction2<T, R>  {

    void run(T t, Follower<R> s);
}
