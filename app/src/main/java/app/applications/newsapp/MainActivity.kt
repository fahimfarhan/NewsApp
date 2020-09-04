package app.applications.newsapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.applications.newsapp.database.NewsRoomDatabase
import app.applications.newsapp.model.Article
import app.applications.newsapp.model.NewsApiResponse
import app.applications.newsapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    /** Consts */
    companion object{
        public var TAG = MainActivity::class.java.simpleName;
    }
    /** Variables */
    private lateinit var liveArticleList: LiveData<PagedList<Article>>;
    private lateinit var newsFeedRecyclerView:RecyclerView;
    private var newsFeedAdapter:NewsFeedAdapter? = null;
    private var liveLoaderState: MutableLiveData<LoaderState> = MutableLiveData();

    /** Constructors */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();
        initList();
        loadStories();
        liveArticleList.observe(this, Observer<PagedList<Article>> {
            pagedList -> newsFeedAdapter?.submitList(pagedList);
        })

        liveLoaderState.observe(this, Observer<LoaderState>{
            newState -> newsFeedAdapter?.setLoaderState(newState);
        })
    }

    /** private methods */
    private fun initGui(){
        newsFeedRecyclerView = findViewById(R.id.newsFeedRecyclerView);
        newsFeedAdapter = NewsFeedAdapter();

        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(this);
        newsFeedRecyclerView.layoutManager = linearLayoutManager;
        newsFeedRecyclerView.adapter = newsFeedAdapter;
    }

    private fun initList() {
        val config:PagedList.Config = PagedList.Config.Builder()
            .setPageSize(30)
            .setEnablePlaceholders(false)
            .build();

        liveArticleList = initializedPagedListBuilder(config).build();

    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Article> {

        val database:NewsRoomDatabase = NewsRoomDatabase.getDatabase(this);
        val livePageListBuilder = LivePagedListBuilder<Int, Article>(
            database.newsDao().articles,
            config);

        var newsBoundaryCallback:NewsBoundaryCallback = NewsBoundaryCallback(database, liveLoaderState);
        livePageListBuilder.setBoundaryCallback(newsBoundaryCallback);
        return livePageListBuilder
    }

    // optional
    private fun loadStories() {
       var call: Call<NewsApiResponse> =  RetrofitClient.getService().fetchStories("cnn", Const.API_KEY);
        call.enqueue(object : Callback<NewsApiResponse>{
            override fun onResponse(
                call: Call<NewsApiResponse>,
                response: Response<NewsApiResponse>
            ) {
                var articles:List<Article> = response.body()?.articles ?: listOf();
                var imagesList:ArrayList<String> = articles.map { it.urlToImage } as ArrayList<String>;
                newsFeedAdapter?.setImages(images = imagesList);
            }

            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                Log.e(TAG, "story loading failed");
            }

        })
    }

    // test ---------------------------------------
    private fun isApiWorkingTest() {
        // working!
        var call:Call<NewsApiResponse> = RetrofitClient.getService().fetchFeed("bbc-news", Const.API_KEY, 1, 30);
        call.enqueue(object : Callback<NewsApiResponse> {
            override fun onFailure(call: Call<NewsApiResponse>, t: Throwable) {
                Log.e(TAG, "onFailed");
            }

            override fun onResponse(
                call: Call<NewsApiResponse>,
                response: Response<NewsApiResponse>
            ) {
                Log.e(TAG, response.body().toString());
            }

        })
    }

    /** public apis */
}