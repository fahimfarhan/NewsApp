# Connect BoundaryCallback with Network
Open the `NewsBoundaryCallback.kt` file and add the following code:
```
    /** override methods */
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded();

        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
                helperCallback: PagingRequestHelper.Request.Callback? ->
            var call:Call<NewsApiResponse> = api.fetchFeed("bbc-news", Const.API_KEY, pageNumber, Const.PAGE_SIZE);
            call.enqueue(object: Callback<NewsApiResponse>{
                override fun onResponse(call: Call<NewsApiResponse>, response: Response<NewsApiResponse>) {
                  if(response.isSuccessful && response.body()!=null) {
                      Log.e(TAG, "onSuccess");
                      val articles: List<Article>? = response.body()?.articles?.map { it };
                      executor.execute{
                          db.newsDao().insertAll(articles?: listOf()); // ?: listOf(); -> if null create emptyList
                          pageNumber++;
                          helperCallback?.recordSuccess();
                      }
                  }else{
                    Log.e(TAG, "onError");
                      helperCallback?.recordFailure(Throwable("onError"));
                  }
                }

                override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure");
                    helperCallback?.recordFailure(t);
                }

            })
        };
    }

    override fun onItemAtEndLoaded(itemAtEnd: Article) {
        super.onItemAtEndLoaded(itemAtEnd);
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
                helperCallback: PagingRequestHelper.Request.Callback? ->
            var call:Call<NewsApiResponse> = api.fetchFeed("bbc-news", Const.API_KEY, pageNumber, Const.PAGE_SIZE);
            call.enqueue(object: Callback<NewsApiResponse>{
                override fun onResponse(call: Call<NewsApiResponse>, response: Response<NewsApiResponse>) {
                    if(response.isSuccessful && response.body()!=null) {
                        Log.e(TAG, "onSuccess");
                        val articles: List<Article>? = response.body()?.articles?.map { it };
                        executor.execute{
                            db.newsDao().insertAll(articles?: listOf()); // ?: listOf(); -> if null create emptyList
                            pageNumber++;
                            helperCallback?.recordSuccess();
                        }
                    }else{
                        Log.e(TAG, "onError");
                        helperCallback?.recordFailure(Throwable("onError"));
                    }
                }

                override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure");
                    helperCallback?.recordFailure(t);
                }

            })
        };
    }
```

Basically we have to write some codes in `onZeroItemsLoaded()` and `onItemAtEndLoaded()` methods. We have
PagingRequestHelper helper. Inside `Helper.runIfNotRunning` we conplete this lambda. This is where we
write our networking code. Just some trivial retrofit call. On success, we insert the result in our database.
If we run our code, we'll see something like this! :tada: :fire:

![first success](first_success.png)