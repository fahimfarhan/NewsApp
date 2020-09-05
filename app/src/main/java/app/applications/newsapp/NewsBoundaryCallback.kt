/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import app.applications.newsapp.database.NewsRoomDatabase
import app.applications.newsapp.model.Article
import app.applications.newsapp.model.NewsApiResponse
import app.applications.newsapp.network.APIService
import app.applications.newsapp.network.RetrofitClient
import app.applications.newsapp.utils.PagingRequestHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NewsBoundaryCallback: PagedList.BoundaryCallback<Article> {
    /** Konsts */
    companion object{
        val TAG = NewsBoundaryCallback::class.java.simpleName;
    }

    /** Variables */
    private var db: NewsRoomDatabase;
    private var api: APIService;
    private val executor: ExecutorService;
    private val helper: PagingRequestHelper;
    var pageNumber:Long;
    private var liveLoaderState: MutableLiveData<LoaderState>;

    /** Konstructors */
    constructor(ndb: NewsRoomDatabase, liveLoaderState: MutableLiveData<LoaderState>) {
        this.db = ndb;
        this.api = RetrofitClient.getService();
        this.executor = Executors.newSingleThreadExecutor();
        this.helper = PagingRequestHelper(executor);
        this.liveLoaderState = liveLoaderState;
        this.pageNumber = 1L;
    }

    /** override methods */
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded();
        Log.e(TAG, "onZeroItemsLoaded()");
        Log.e(TAG, "pageNumber -> "+pageNumber);
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
                helperCallback: PagingRequestHelper.Request.Callback? ->
            var call:Call<NewsApiResponse> = api.fetchFeed("bbc-news", Const.API_KEY, pageNumber, Const.PAGE_SIZE);

            liveLoaderState.postValue(LoaderState.LOADING);
            call.enqueue(object: Callback<NewsApiResponse>{
                override fun onResponse(call: Call<NewsApiResponse>, response: Response<NewsApiResponse>) {
                  if(response.isSuccessful && response.body()!=null) {
                      Log.e(TAG, "onSuccess");
                      Log.e(TAG, "pageNumber -> "+pageNumber);
                      val articles: List<Article>? = response.body()?.articles?.map { it };
                      executor.execute{
                          db.newsDao().insertAll(articles?: listOf()); // ?: listOf(); -> if null create emptyList
                          pageNumber++;
                          helperCallback?.recordSuccess();
                      }
                      liveLoaderState.postValue(LoaderState.DONE);
                  }else{
                    Log.e(TAG, "onError");
                      Log.e(TAG, "pageNumber -> "+pageNumber);
                    helperCallback?.recordFailure(Throwable("onError"));
                    liveLoaderState.postValue(LoaderState.ERROR);
                  }
                }

                override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure");
                    Log.e(TAG, "pageNumber -> "+pageNumber);
                    helperCallback?.recordFailure(t);
                    liveLoaderState.postValue(LoaderState.ERROR);
                }

            })
        };
    }

    override fun onItemAtEndLoaded(itemAtEnd: Article) {
        super.onItemAtEndLoaded(itemAtEnd);
        Log.e(TAG, "onItemAtEndLoaded()");
        Log.e(TAG, "pageNumber -> "+pageNumber);
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
                helperCallback: PagingRequestHelper.Request.Callback? ->
            var call:Call<NewsApiResponse> = api.fetchFeed("bbc-news", Const.API_KEY, pageNumber, Const.PAGE_SIZE);

            liveLoaderState.postValue(LoaderState.LOADING);
            call.enqueue(object: Callback<NewsApiResponse>{
                override fun onResponse(call: Call<NewsApiResponse>, response: Response<NewsApiResponse>) {
                    if(response.isSuccessful && response.body()!=null) {
                        Log.e(TAG, "onSuccess");
                        Log.e(TAG, "pageNumber -> "+pageNumber);
                        val articles: List<Article>? = response.body()?.articles?.map { it };
                        executor.execute{
                            db.newsDao().insertAll(articles?: listOf()); // ?: listOf(); -> if null create emptyList
                            pageNumber++;
                            helperCallback?.recordSuccess();
                        }
                        liveLoaderState.postValue(LoaderState.DONE);
                    }else{
                        Log.e(TAG, "onError");
                        Log.e(TAG, "pageNumber -> "+pageNumber);
                        helperCallback?.recordFailure(Throwable("onError"));
                        liveLoaderState.postValue(LoaderState.ERROR);
                    }
                }

                override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                    Log.e(TAG, "onFailure");
                    Log.e(TAG, "pageNumber -> "+pageNumber);
                    helperCallback?.recordFailure(t);
                    liveLoaderState.postValue(LoaderState.ERROR);
                }

            })
        };
    }


}