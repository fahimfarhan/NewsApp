/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp.database;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import app.applications.newsapp.model.Article;
import app.applications.newsapp.model.Source;

public class Converters {

    @TypeConverter
    public static Article getArticle(String value) {
        return new Gson().fromJson(value, Article.class);
    }

    @TypeConverter
    public static String saveArticle(Article article) {
        return new Gson().toJson(article, Article.class);
    }

    @TypeConverter
    public static Source getSource(String value) {
        return new Gson().fromJson(value, Source.class);
    }

    @TypeConverter
    public static String saveSource(Source source) {
        return new Gson().toJson(source, Source.class);
    }


}
