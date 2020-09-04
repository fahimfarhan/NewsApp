package app.applications.newsapp.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.applications.newsapp.Const.BASE_URL;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static APIService getService() {
        return getRetrofitInstance().create(APIService.class);
    }
}
