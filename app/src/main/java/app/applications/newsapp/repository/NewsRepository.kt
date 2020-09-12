/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp.repository

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import app.applications.newsapp.Const
import app.applications.newsapp.LoaderState
import app.applications.newsapp.MainActivity
import app.applications.newsapp.NewsBoundaryCallback
import app.applications.newsapp.database.NewsRoomDatabase
import app.applications.newsapp.model.Article
import app.applications.newsapp.model.NewsApiResponse
import app.applications.newsapp.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class NewsRepository {

    private var context: Context;
    var liveLoaderState: MutableLiveData<LoaderState> = MutableLiveData(LoaderState.DONE);
    var liveArticleList: LiveData<PagedList<Article>> = MutableLiveData();
    var liveImagesList:MutableLiveData<ArrayList<String>>;
    var sharedpreferences: SharedPreferences;
    val database: NewsRoomDatabase;

    private var newsBoundaryCallback: NewsBoundaryCallback? = null;
    private val newsFactory: NewsFactory;
    // =getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)

    public constructor(context: Context) {
        this.context = context;
        this.liveImagesList = MutableLiveData();
        this.database = NewsRoomDatabase.getDatabase(this.context);
        this.sharedpreferences = context.getSharedPreferences(
            Const.PREFERENCE,
            Context.MODE_PRIVATE
        );
        this.newsFactory = NewsFactory(context, liveLoaderState);
        initList();
        initStories();
    }

    fun forcedRefresh() {
        val handlerThread = HandlerThread("dbHandlerThread");
        handlerThread.start();
        val looper = handlerThread.looper;
        val handler = Handler(looper);
        handler.post(Runnable {
            database.newsDao().deleteAll();
        });
        liveArticleList = newsFactory.getLivePagedArticles(Const.ONLINE);
        newsFactory.dataSource?.invalidate();
        // todo: needs an update
    }

    fun initList() {
        var lastNewsUpdateTimeMillis:Long = sharedpreferences.getLong(Const.LAST_NEWS_UPDATE_TIME, 0);

        if(   System.currentTimeMillis() - lastNewsUpdateTimeMillis > (Const.ONE_DAY_IN_MILLIS)  ) {  // once in every 8 hour

            this.liveArticleList = newsFactory.getLivePagedArticles(Const.ONLINE);
            var editor: SharedPreferences.Editor = sharedpreferences.edit();
            editor.putLong(Const.LAST_NEWS_UPDATE_TIME, System.currentTimeMillis());
            editor.apply();
        }else{
            this.liveArticleList = newsFactory.getLivePagedArticles(Const.OFFLINE);
        }
    }

    // optional
    private fun initStories() {
        if(System.currentTimeMillis() - sharedpreferences.getLong(Const.LAST_STORIES_UPDATE_TIME, 0)
            > Const.ONE_DAY_IN_MILLIS   ) {
            loadStoriesFromNetwork();
        }else {

            val gson = Gson()
            val json: String? = sharedpreferences.getString(Const.STORIES_SET, null)

            if(json != null) {
                val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
                var images:ArrayList<String> =  gson.fromJson(json, type)
                liveImagesList.postValue(images);
            }else{
                loadStoriesFromNetwork();
            }
        }
    }

    private fun loadStoriesFromNetwork() {

        var call: Call<NewsApiResponse> =  RetrofitClient.getService().fetchStories(
            "cnn",
            Const.API_KEY
        );
        call.enqueue(object : Callback<NewsApiResponse> {
            override fun onResponse(
                call: Call<NewsApiResponse>,
                response: Response<NewsApiResponse>
            ) {
                var articles: List<Article> = response.body()?.articles ?: listOf();
                var imagesList: ArrayList<String> =
                    articles.map { it.urlToImage } as ArrayList<String>;

                liveImagesList.postValue(imagesList);
                val editor: SharedPreferences.Editor = sharedpreferences.edit()
                val gson = Gson()
                val json = gson.toJson(imagesList)
                editor.putString(Const.STORIES_SET, json)
                editor.apply()

            }

            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                Log.e(MainActivity.TAG, "story loading failed");
            }

        })
    }

}