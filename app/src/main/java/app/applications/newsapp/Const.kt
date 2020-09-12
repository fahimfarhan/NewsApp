/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp

object Const {
    const val PAGE_SIZE: Int = 10;
    const val BASE_URL = "https://newsapi.org/v2/";
    const val API_KEY = BuildConfig.NEWS_API_KEY;

    const val PREFERENCE: String = "PREFERENCE";
    const val LAST_STORIES_UPDATE_TIME:String = "LAST_UPDATE_TIME";
    const val ONE_DAY_IN_MILLIS:Long = 1*24*60*60*1000;
    const val LAST_NEWS_UPDATE_TIME: String = "LAST_NEWS_UPDATE_TIME";
    const val STORIES_SET: String = "STORIES_SET";
    const val ONLINE: Int = 0;
    const val OFFLINE: Int = 1;

}