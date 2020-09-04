# Connect RecyclerView with Database
We need to create a pagedList of Articles from database. For that we write this method:

```
    private fun initList() {
        val config:PagedList.Config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build();

        liveArticleList = initializedPagedListBuilder(config).build();

    }
```

And,
```
    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Article> {

        val database:NewsRoomDatabase = NewsRoomDatabase.getDatabase(this);
        val livePageListBuilder = LivePagedListBuilder<Int, Article>(
            database.newsDao().articles,
            config);

        livePageListBuilder.setBoundaryCallback(NewsBoundaryCallback(database));
        return livePageListBuilder
    }
```
So now we create `NewsBoundaryCallback.kt` file. It extends `BoundaryCallBack` class. Whenever, we reach at the end
of recyclerview, this callback is triggered. It will handle network call to fetch new data.

When we reach to the end of our pagedList, we need to make a network call, insert the data inside the database
and show it to out recyclerView. This is a complicated task. Luckily we have `PagingRequestHelper.java` to help us.
It is written by the same dudes who wrote the paging library. For some weirdo reason, it is not inside the
library itself, so we need to copy/download it from github, and use it.

If we run our code, we'll see that there is nothing. So next we'll connect with network to actually populate
the database and show it to recyclerView.