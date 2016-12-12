package com.jipengfei.android.async;

import java.util.List;
import java.util.concurrent.FutureTask;

/**
 * Created by james on 12/12/16.
 */

public class Async2<T> {
    private OnFollow<T> onFollow;
    private Async2(OnFollow<T> onFollow) {
        this.onFollow = onFollow;
    }


    public <R> Async<R> next(final Func1<T, R> func) {
        return new Async<>(new OnFollow<R>() {

            @Override
            public void call(final Follower<R> s) {
                Follower<T> fT = new Follower<T>() {
                    @Override
                    public void onNext(T t) {
                        try {
                            R r = func.run(t);
                            s.onNext(r);
                        } catch (Throwable e) {
                            s.onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        s.onError(e);
                    }
                };
                onFollow.call(fT);
            }
        });
    }
}

