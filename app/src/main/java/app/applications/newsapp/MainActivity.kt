package app.applications.newsapp

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.applications.newsapp.database.NewsRoomDatabase
import app.applications.newsapp.model.Article
import app.applications.newsapp.model.NewsApiResponse
import app.applications.newsapp.network.RetrofitClient
import app.applications.newsapp.viewmodel.NewsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    /** Consts */
    companion object{
        public var TAG = MainActivity::class.java.simpleName;
    }
    /** Variables */
    private lateinit var newsViewModel:NewsViewModel;
    private lateinit var newsFeedRecyclerView:RecyclerView;
    private var newsFeedAdapter:NewsFeedAdapter? = null;

    /** Constructors */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();

        this.newsViewModel = ViewModelProvider(this).get(NewsViewModel::class.java);

        this.newsViewModel.newsRepository.liveArticleList.observe(this, Observer<PagedList<Article>> {
            pagedList -> newsFeedAdapter?.submitList(pagedList);
        });

        this.newsViewModel.newsRepository.liveLoaderState.observe(this, Observer<LoaderState>{
            newState -> newsFeedAdapter?.setLoaderState(newState);
        });

        this.newsViewModel.newsRepository.liveImagesList.observe(this, Observer<ArrayList<String>>{
            imagesList -> newsFeedAdapter?.submitImages(imagesList);
        });
    }

    /** private methods */
    private fun initGui(){
        newsFeedRecyclerView = findViewById(R.id.newsFeedRecyclerView);
        newsFeedAdapter = NewsFeedAdapter();

        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(this);
        newsFeedRecyclerView.layoutManager = linearLayoutManager;
        newsFeedRecyclerView.adapter = newsFeedAdapter;
    }

    // test ---------------------------------------

    /** public apis */
}