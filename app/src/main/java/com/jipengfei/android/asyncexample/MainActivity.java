package com.jipengfei.android.asyncexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jipengfei.android.async.*;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        test();
    }


    public static void test() {
        Async.create().on(Async.io()).next(new Func0<Float>() {
            public Float run() throws Throwable {
                Log.e(TAG, "thread=" + Thread.currentThread());
                return 1.0f;
            }
        }).on(Async.mainThread()).next(new Func1<Float, Float>() {
            public Float run(Float i) throws Throwable {
                Log.e(TAG, "thread=" + Thread.currentThread());
                return i + 1;
            }
        }).result(new Result<Float>() {

            @Override
            public void onResult(Float aFloat) {
                Log.e(TAG, "thread=" + Thread.currentThread());
                Log.e(TAG, "subscriber aFloat="+aFloat);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "thread=" + Thread.currentThread());
                Log.e(TAG, "onError", e);
            }
        }).fire();


        Async.create().onThreadPool(new Func0<String>() {
            @Override
            public String run() throws Throwable {
                return null;
            }
        }).onMainThread(new Action1<String>() {
            @Override
            public void run(String s) throws Throwable {
                Log.e(TAG, "last return " + s);
            }
        }).fire();

        Async2.create(new Func<Void, Float>() {
            @Override
            public Float run(Void v) throws Throwable {
                return 1.0f;
            }
        }).onThreadPool(new Func<Float, Integer>(){
            @Override
            public Integer run(Float f) throws Throwable {
                return f.intValue();
            }
        }).on(Async2.mainThread()).result(new Result<Integer>() {
            @Override
            public void onResult(Integer i) {
                Log.e(TAG, "result on mainthread:"+i);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
