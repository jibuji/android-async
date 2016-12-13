package com.jipengfei.android.async;



/**
 * Created by jibuji on 15/10/9.
 */
public class Async2<T> {

    OnFollow<T> onFollow;

    private Async2(OnFollow<T> onFollow) {
        this.onFollow = onFollow;
    }

    public static <T> Async2<T> create(final Func0<T> func) {
        return new Async2<>(new OnFollow<T>() {

            @Override
            public void call(final Follower<T> s) {
                try {
                    T r = func.run();
                    s.onNext(r);
                } catch (Throwable e) {
                    s.onError(e);
                }
            }
        });
    }

    public static <T> Async2<T> create(final Func<Void, T> func) {
        return new Async2<>(new OnFollow<T>() {

            @Override
            public void call(final Follower<T> s) {
                try {
                    T r = func.run(null);
                    s.onNext(r);
                } catch (Throwable e) {
                    s.onError(e);
                }
            }
        });
    }

    public void result(final Result<T> result) {
        //ignore s
        Follower<T> fT = new Follower<T>() {
            @Override
            public void onNext(T t) {
                try {
                    result.onResult(t);
                } catch (Throwable e) {
                    result.onError(e);
                }
            }
            @Override
            public void onError(Throwable e) {
                result.onError(e);
            }
        };
        onFollow.call(fT);
    }

    public <R> Async2<R> next(final Func<T, R> func) {
        return new Async2<>(new OnFollow<R>() {

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

    public Async2<T> on(final Scheduler scheduler) {
        return new Async2<>(new OnFollow<T>() {
            @Override
            public void call(final Follower<T> s) {
                Follower<T> fT = new Follower<T>() {
                    @Override
                    public void onNext(final T t) {
                        scheduler.schedule(new Runnable() {
                            public void run() {
                                s.onNext(t);
                            }
                        });
                    }

                    @Override
                    public void onError(final Throwable e) {
                        scheduler.schedule(new Runnable() {
                            public void run() {
                                s.onError(e);
                            }
                        });
                    }
                };
                onFollow.call(fT);
            }
        });
    }

    public static Scheduler threadPool() {
        return Schedulers.threadPool();
    }

    public static Scheduler mainThread() {
        return Schedulers.mainThread();
    }

    public static <T> Async2<T> fromValue(final T t) {
        return new Async2<>(new OnFollow<T>() {

            @Override
            public void call(Follower<T> s) {
                s.onNext(t);
            }
        });
    }


    public <R> Async2<R> onMainThread(final Func<T, R> func) {
        return this.on(mainThread()).next(func);
    }


    public <R> Async2<R> onThreadPool(final Func<T, R> func) {
        return this.on(threadPool()).next(func);
    }
}
