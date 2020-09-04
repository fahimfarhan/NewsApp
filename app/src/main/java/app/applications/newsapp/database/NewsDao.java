package app.applications.newsapp.database;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import app.applications.newsapp.model.Article;

@Dao
public interface NewsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Article> articleList);

    @Query("DELETE FROM article_table")
    void deleteAll();

    @Query("SELECT * from article_table ORDER BY pk DESC")
    DataSource.Factory<Integer, Article> getArticles();
}
