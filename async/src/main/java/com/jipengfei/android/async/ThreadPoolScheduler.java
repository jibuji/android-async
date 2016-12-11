package com.jipengfei.android.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by jibuji on 15/10/10.
 */
public class ThreadPoolScheduler implements Scheduler {
    public final static int POOL_SIZE = 4;

    public final ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

    @Override
    public void schedule(Runnable runnable) {
        executor.execute(runnable);
    }
}
