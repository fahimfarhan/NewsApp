package app.applications.newsapp

object Const {
    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_KEY =
        BuildConfig.NEWS_API_KEY //BuildConfig.NEWS_API_KEY;    // todo: read api key from a file and regenerate this key from NewsApi.org
}