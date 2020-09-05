# Force Refresh on Swipe
Now we want to add a swipe forced refresh.

Note that, I'm not sure how to use `newsapi` to ask for specific / new items. That's why I am gonna
delete the existing data from my app. But I suppose in production, one should properly check and request
for just the necessary portions of data. So suppose, I have the news from id = 1234 to id = 2345, or maybe
greatest timestamp = todat 10:00 am. Then I should ask the api to give me news with id > 2345 or timestamp
> 10:00 am, things like that.


That being said, to refesh news, first put your recyclerView inside a `SwipeRefreshLayout`. Next in
your main activity, add this:

```
swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            // todo: refresh
            newsViewModel.newsRepository.forcedRefresh();
            swipeRefreshLayout.isRefreshing = false;

        });
```

Now go to the `NewsRepository.kt` file and add:
```
fun forcedRefresh() {
        val handlerThread = HandlerThread("dbHandlerThread");
        handlerThread.start();
        val looper = handlerThread.looper;
        val handler = Handler(looper);
        handler.post(Runnable {
            database.newsDao().deleteAll();
        });
        newsBoundaryCallback?.pageNumber = 1L;
        var dataSource = datasourceFactory.create();
        dataSource.invalidate();
    }
```

There are some other changes, such as converting some local variables into private fields, and some
private fields into public ones for convenience.

