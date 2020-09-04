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
    private var pageNumber:Long;

    /** Konstructors */
    constructor(ndb: NewsRoomDatabase) {
        this.db = ndb;
        this.api = RetrofitClient.getService();
        this.executor = Executors.newSingleThreadExecutor();
        this.helper = PagingRequestHelper(executor);
        pageNumber = 1L;
    }


}