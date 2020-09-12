package app.applications.newsapp.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import app.applications.newsapp.Const
import app.applications.newsapp.LoaderState
import app.applications.newsapp.database.NewsRoomDatabase
import app.applications.newsapp.model.Article
import app.applications.newsapp.newsdatasource.NewsDataSource

class NewsFactory {
    /** Variables */
    private var sharedpreferences: SharedPreferences?=null;
    private val database: NewsRoomDatabase;
    private val context: Context;
    private val offlineDataSourceFactory: DataSource.Factory<Int, Article>;
    private val config: PagedList.Config;
    private val liveLoaderState: MutableLiveData<LoaderState>;
    var dataSource: NewsDataSource? = null;

    /** Constructors */
    public constructor(context: Context, liveLoaderState: MutableLiveData<LoaderState>) {
        this.context = context;
        // offline stuffs
        this.database = NewsRoomDatabase.getDatabase(this.context);
        this.offlineDataSourceFactory = database.newsDao().articles;
        this.sharedpreferences = context.getSharedPreferences(
            Const.PREFERENCE,
            Context.MODE_PRIVATE
        );
        this.liveLoaderState = liveLoaderState;

        config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build();

    }

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
}