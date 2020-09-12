package app.applications.newsapp.newsdatasource

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import app.applications.newsapp.Const
import app.applications.newsapp.LoaderState
import app.applications.newsapp.database.NewsDao
import app.applications.newsapp.database.NewsRoomDatabase
import app.applications.newsapp.model.Article
import app.applications.newsapp.model.NewsApiResponse
import app.applications.newsapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public class NewsDataSource : PageKeyedDataSource<Long, Article> {
    /** Consts */
    companion object{
        val TAG: String = NewsDataSource::class.java.simpleName;
    }

    /** Variables */
    private val context: Context;
    private val database: NewsRoomDatabase;
    private val liveLoaderState: MutableLiveData<LoaderState>;
    /** Constructors */
    constructor(context: Context, liveLoaderState: MutableLiveData<LoaderState>) {
        this.context = context;
        this.database = NewsRoomDatabase.getDatabase(context);
        this.liveLoaderState = liveLoaderState;
    }

    /** Private methods */
    private fun backgroundBulkInsert(articles: List<Article>?, id: Long) {
        if(articles!=null) {
            val name: String = "dbHandlerThread"+id;
            val handlerThread = HandlerThread(name);
            handlerThread.start();
            val looper = handlerThread.looper;
            val handler = Handler(looper);
            handler.post(Runnable {
                database.newsDao().insertAll(articles);
            });
        }
    }

    /** Overrride methods */
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, Article>) {
        Log.e(TAG, "loadInitial");
        var pageNumber: Long = 1L;

        liveLoaderState.postValue(LoaderState.LOADING);
        var call: Call<NewsApiResponse?> = RetrofitClient.getService().fetchFeed(
            "bbc-news", Const.API_KEY, pageNumber, Const.PAGE_SIZE
        );

        call.enqueue(object : Callback<NewsApiResponse?>{
            override fun onResponse(
                call: Call<NewsApiResponse?>,
                response: Response<NewsApiResponse?>) {
                    if(response.isSuccessful && response.body()!=null) {
                        val next: Long = pageNumber + 1;
                        val prev: Long = pageNumber - 1;
                        val articles: List<Article>? = response.body()?.articles;
                        if(articles!=null) {
                            callback.onResult(articles,  prev, next);
                            backgroundBulkInsert(articles, pageNumber);
                            liveLoaderState.postValue(LoaderState.DONE);
                            Log.e(TAG, "onSuccess");
                        }
                    }else{
                        liveLoaderState.postValue(LoaderState.ERROR);
                        Log.e(TAG, "onError");
                    }
                }

            override fun onFailure(call: Call<NewsApiResponse?>, t: Throwable) {
                Log.e(TAG, "onFailure");
                liveLoaderState.postValue(LoaderState.ERROR);
             }
        });
    }

    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {
        var pageNumber: Long = params.key;
        Log.e(TAG, "loadBefore-->"+pageNumber);
        var call: Call<NewsApiResponse?> = RetrofitClient.getService().fetchFeed(
            "bbc-news", Const.API_KEY, pageNumber, Const.PAGE_SIZE
        );

        liveLoaderState.postValue(LoaderState.LOADING);
        call.enqueue(object : Callback<NewsApiResponse?>{
            override fun onResponse(
                call: Call<NewsApiResponse?>,
                response: Response<NewsApiResponse?>) {

                if(response.isSuccessful && response.body()!=null) {
                    val prev: Long = pageNumber - 1;
                    val articles: List<Article>? = response.body()?.articles;
                    if(articles!=null) {
                        callback.onResult(articles,  prev);
                        backgroundBulkInsert(articles, pageNumber);
                        liveLoaderState.postValue(LoaderState.DONE);
                        Log.e(TAG, "onSuccess");
                    }
                }else{
                    liveLoaderState.postValue(LoaderState.ERROR);
                    Log.e(TAG, "onError");
                }
            }

            override fun onFailure(call: Call<NewsApiResponse?>, t: Throwable) {
                liveLoaderState.postValue(LoaderState.ERROR);
                Log.e(TAG, "onFailure");
            }
        });
    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, Article>) {
        var pageNumber: Long = params.key;
        Log.e(TAG, "loadAfter-->"+pageNumber);
        var call: Call<NewsApiResponse?> = RetrofitClient.getService().fetchFeed(
            "bbc-news", Const.API_KEY, pageNumber, Const.PAGE_SIZE
        );

        liveLoaderState.postValue(LoaderState.LOADING);
        call.enqueue(object : Callback<NewsApiResponse?>{
            override fun onResponse(
                call: Call<NewsApiResponse?>,
                response: Response<NewsApiResponse?>) {

                if(response.isSuccessful && response.body()!=null) {
                    val next: Long = pageNumber + 1;
                    val articles: List<Article>? = response.body()?.articles;
                    if(articles!=null) {
                        callback.onResult(articles,  next);
                        backgroundBulkInsert(articles, pageNumber);
                        liveLoaderState.postValue(LoaderState.DONE);
                        Log.e(TAG, "onSuccess");
                    }
                }else{
                    liveLoaderState.postValue(LoaderState.ERROR);
                    Log.e(TAG, "onError");
                }
            }

            override fun onFailure(call: Call<NewsApiResponse?>, t: Throwable) {
                liveLoaderState.postValue(LoaderState.ERROR);
                Log.e(TAG, "onFailure");
            }
        });
    }




}