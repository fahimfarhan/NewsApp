package app.applications.newsapp

object Const {
    val STORIES_SET: String = "STORIES_SET";
    const val PAGE_SIZE: Int = 10;
    const val BASE_URL = "https://newsapi.org/v2/";
    const val API_KEY = BuildConfig.NEWS_API_KEY;

    const val PREFERENCE: String = "PREFERENCE";
    const val LAST_UPDATE_TIME:String = "LAST_UPDATE_TIME";
    const val ONE_DAY_IN_MILLIS:Long = 1*24*60*60*1000;
}