package com.jipengfei.android.async;


import android.os.Handler;
import android.os.Looper;

/**
 * Created by jibuji on 15/10/10.
 */
public class MainThreadHandlerScheduler implements Scheduler {
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void schedule(Runnable runnable) {
        handler.post(runnable);
    }
}
