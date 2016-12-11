package com.jipengfei.android.async;

/**
 * Created by jibuji on 15/10/9.
 */
public class Schedulers {
    private final static Scheduler mainThread = new MainThreadHandlerScheduler();
    private final static Scheduler threadPool = new ThreadPoolScheduler();
    public static Scheduler threadPool() {
        return threadPool;
    }

    public static Scheduler mainThread() {
        return mainThread;
    }
}
