package com.jipengfei.android.async;



/**
 * Created by jibuji on 15/10/9.
 */
public class Async<T> {

    private static final Follower EmptyFollower = new Follower() {
        @Override
        public void onNext(Object o) {

        }

        @Override
        public void onError(Throwable e) {

        }
    };

    OnFollow<T> onFollow;

    public Async(OnFollow<T> onFollow) {
        this.onFollow = onFollow;
    }


    public Async<T> result(final Result<T> result) {
        return new Async<>(new OnFollow<T>() {

            @Override
            public void call(final Follower<T> s) {
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
        });
    }

    public void fire() {
        onFollow.call(EmptyFollower);
    }

    public <R> Async<R> next(final Func0<R> func) {
        return new Async<>(new OnFollow<R>() {

            @Override
            public void call(final Follower<R> s) {
                Follower<T> fT = new Follower<T>() {
                    @Override
                    public void onNext(T t) {
                        try {
                            R r = func.run();
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

    public <R> Async<R> next(final AsyncAction2<T, R> af) {
        return new Async<>(new OnFollow<R>(){

            @Override
            public void call(final Follower<R> s) {
                Follower<T> fT = new Follower<T>() {
                    @Override
                    public void onNext(T t) {
                        try {
                            af.run(t, s);
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

    public <R> Async<R> next(final AsyncAction1<R> af) {
        return new Async<>(new OnFollow<R>(){

            @Override
            public void call(final Follower<R> s) {
                Follower<T> subT = new Follower<T>() {
                    @Override
                    public void onNext(T t) {
                        try {
                            af.run(s);
                        } catch (Throwable e) {
                            s.onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        s.onError(e);
                    }
                };
                onFollow.call(subT);
            }
        });
    }

    public <Void> Async<Void> next(final Action0 action) {
        return new Async<>(new OnFollow<Void>() {

            @Override
            public void call(final Follower<Void> s) {
                Follower<T> subT = new Follower<T>() {
                    @Override
                    public void onNext(T t) {
                        try {
                            action.run();
                            s.onNext(null);
                        } catch (Throwable e) {
                            s.onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        s.onError(e);
                    }
                };
                onFollow.call(subT);
            }
        });
    }

    public <Void> Async<Void> next(final Action1<T> action) {
        return new Async<>(new OnFollow<Void>() {

            @Override
            public void call(final Follower<Void> s) {
                Follower<T> subT = new Follower<T>() {
                    @Override
                    public void onNext(T t) {
                        try {
                            action.run(t);
                            s.onNext(null);
                        } catch (Throwable e) {
                            s.onError(e);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        s.onError(e);
                    }
                };
                onFollow.call(subT);
            }
        });
    }

    public Async<T> on(final Scheduler scheduler) {
        return new Async<>(new OnFollow<T>() {
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

    public static Scheduler io() {
        return Schedulers.threadPool();
    }

    public static Scheduler mainThread() {
        return Schedulers.mainThread();
    }

    public static <T> Async<T> fromValue(final T t) {
        return new Async<>(new OnFollow<T>() {

            @Override
            public void call(Follower<T> s) {
                s.onNext(t);
            }
        });
    }

    public static <T> Async<T> create() {
        return new Async<>(new OnFollow<T>() {

            @Override
            public void call(Follower<T> s) {
                s.onNext(null);
            }
        });
    }

    public <R> Async<R> onMainThread(final Func0<R> func) {
        return this.on(mainThread()).next(func);
    }
    public <R> Async<R> onMainThread(final Func1<T, R> func) {
        return this.on(mainThread()).next(func);
    }
    public Async<T> onMainThread(final Action0 action) {
        return this.on(mainThread()).next(action);
    }
    public Async<T> onMainThread(final Action1<T> action) {
        return this.on(mainThread()).next(action);
    }
    public Async<T> onMainThread(final AsyncAction1<T> action) {
        return this.on(mainThread()).next(action);
    }
    public <R> Async<R> onMainThread(final AsyncAction2<T, R> action) {
        return this.on(mainThread()).next(action);
    }

    public <R> Async<R> onThreadPool(final Func0<R> func) {
        return this.on(io()).next(func);
    }
    public <R> Async<R> onThreadPool(final Func1<T, R> func) {
        return this.on(io()).next(func);
    }
    public Async<T> onThreadPool(final Action0 action) {
        return this.on(io()).next(action);
    }
    public Async<T> onThreadPool(final Action1<T> action) {
        return this.on(io()).next(action);
    }
    public Async<T> onThreadPool(final AsyncAction1<T> action) {
        return this.on(io()).next(action);
    }
    public <R> Async<R> onThreadPool(final AsyncAction2<T, R> action) {
        return this.on(io()).next(action);
    }

}
