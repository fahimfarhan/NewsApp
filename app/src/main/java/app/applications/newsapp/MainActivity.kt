package app.applications.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.applications.newsapp.model.NewsApiResponse
import app.applications.newsapp.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    companion object{
        public var TAG = MainActivity.javaClass.simpleName;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

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
}