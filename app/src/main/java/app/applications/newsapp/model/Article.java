/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

@Entity(tableName = "article_table")
public class Article {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pk")
    private int pk;

    @SerializedName("url")
    @ColumnInfo(name = "url")
    private String url;

    @SerializedName("source")
    @ColumnInfo(name = "source")
    private Source source;

    @SerializedName("author")
    @ColumnInfo(name = "author")
    private String author;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("urlToImage")
    @ColumnInfo(name = "urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    @ColumnInfo(name = "publishedAt")
    private String publishedAt;


    public Article(){}

    public Source getSource() { return source; }

    public void setSource(Source source) { this.source = source; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getUrlToImage() { return urlToImage; }

    public void setUrlToImage(String urlToImage) { this.urlToImage = urlToImage; }

    public String getPublishedAt() { return publishedAt; }

    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt; }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    @Override
    public String toString() {
        return "Article{" +
                "pk=" + pk +
                ", url='" + url + '\'' +
                ", source=" + source +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", urlToImage='" + urlToImage + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return pk == article.pk &&
                Objects.equals(url, article.url) &&
                Objects.equals(source, article.source) &&
                Objects.equals(author, article.author) &&
                Objects.equals(title, article.title) &&
                Objects.equals(description, article.description) &&
                Objects.equals(urlToImage, article.urlToImage) &&
                Objects.equals(publishedAt, article.publishedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pk, url, source, author, title, description, urlToImage, publishedAt);
    }
}