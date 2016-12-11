# android-async

A simple library on android to simplify your async task's flow.

-----


# How to use?

```java

Async.create().on(Async.io()).next(new Func0<Float>() {
    // this block is running on a non-main thread,
    // so you can do some heavey things.
    Request request = new Request.Builder()
          .url(url)
          .build();
    Response response = client.newCall(request).execute();
    return response.body().string();

}).result(new Result<String>() {
    // this block is running on main thread,
    // so you can do some UI stuff.
    @Override
    public void onResult(String str) {
        Log.e(TAG, "http response:"+str);
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "http execption", e);
    }
}).fire();


```
