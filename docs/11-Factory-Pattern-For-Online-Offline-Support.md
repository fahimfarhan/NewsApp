# Use a Factory Pattern to Properly Support Online-Offline modes

Previously, the data was read from database. When end of data were reached, a network call was triggered to fetch new data.
It was a bit awkward. So after studying and experimenting for some more time, I came up with a factory pattern
to properly manage the online offline modes.

So first, we'll be creating a class named `NewsDataSource` that extends `PageKeyedDataSource<Long, Article>`.
Here, we use `Long` as our news api uses number type to indicate next page. If your api uses something else
(say, `String`), then you should use `PageKeyedDataSource<String, Article>` somwthing like that.

Now you have to complete loadInitial, loadBefore, loadAfter. If you google for paging library tutorial, this
is the thing that pops up everywhere. Just follow one if you need details. My personal favourite is [https://www.raywenderlich.com/6948-paging-library-for-android-with-kotlin-creating-infinite-lists](https://www.raywenderlich.com/6948-paging-library-for-android-with-kotlin-creating-infinite-lists).
So I'm gonna skip this part.

Once you're done with it, we'll start making the data factory. Create a class named `NewsFactory`.
This is the most important part of the code:
```
    /** Private methods */
    // offline data
    private fun offlinePagedListBuilder(): LivePagedListBuilder<Int, Article> {
        val livePageListBuilder = LivePagedListBuilder<Int, Article>(offlineDataSourceFactory, config);
        return livePageListBuilder
    }

    // online data
    private fun onlinePagedListBuilder(): LivePagedListBuilder<Long, Article> {
        val dataSourceFactory = object : DataSource.Factory<Long, Article>() {
            override fun create(): DataSource<Long, Article> {
                val newsDataSource = NewsDataSource(context, liveLoaderState);
                dataSource = newsDataSource;
                return newsDataSource;
            }
        };
        val livePageListBuilder = LivePagedListBuilder<Long, Article>(dataSourceFactory, config);
        return livePageListBuilder;
    }

    /** Public apis */
    fun getLivePagedArticles(type: Int): LiveData<PagedList<Article>> {
        if(type == Const.OFFLINE) {
            Log.e("NewsFactory", "NewsFactory->OfflineDataSet");
            val livePagedListArticles: LiveData<PagedList<Article>> = offlinePagedListBuilder().build();
            return livePagedListArticles;
        }else if(type == Const.ONLINE) {
            Log.e("NewsFactory", "NewsFactory->OnlineDataSet");
            val livePagedListArticles: LiveData<PagedList<Article>> = onlinePagedListBuilder().build();
            return livePagedListArticles;
        }else{
            // todo: if you have other sources, maybe place them here...
            Log.e("NewsFactory", "NewsFactory->OtherDataSet");
            return MutableLiveData(); // empty live data :/ some weirdo kotlin thing...
        }
    }
```

Once done, go to `NewsRepository` class, and initialize your `Live<PagedList<Article> >` in this way:
```
    fun initList() {
        var lastNewsUpdateTimeMillis:Long = sharedpreferences.getLong(Const.LAST_NEWS_UPDATE_TIME, 0);

        if(   System.currentTimeMillis() - lastNewsUpdateTimeMillis > (Const.ONE_DAY_IN_MILLIS)  ) {  // once daily

            this.liveArticleList = newsFactory.getLivePagedArticles(Const.ONLINE);
            var editor: SharedPreferences.Editor = sharedpreferences.edit();
            editor.putLong(Const.LAST_NEWS_UPDATE_TIME, System.currentTimeMillis());
            editor.apply();
        }else{
            this.liveArticleList = newsFactory.getLivePagedArticles(Const.OFFLINE);
        }
    }
```

Now if you run it, you will see that each day, on the first time, the data will load from network call,
and then it will be stored in the database.
After that, it will load data from database only.
You can see the evidence yourself if you run the code and check the log prints.

Finally, for the force refresh part, I did this:
```
    fun forcedRefresh() {
        val handlerThread = HandlerThread("dbHandlerThread");
        handlerThread.start();
        val looper = handlerThread.looper;
        val handler = Handler(looper);
        handler.post(Runnable {
            database.newsDao().deleteAll();
        });
        liveArticleList = newsFactory.getLivePagedArticles(Const.ONLINE);
        newsFactory.dataSource?.invalidate();                                // <------ This is the important line
    }
```
This will delete the old news, and restart loading data from network. Although this is not the ideal way.
I really don't understand how to tell the newsapi to give me only the latest news. So I did this.

Thank you for reading.