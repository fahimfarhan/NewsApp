/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp.viewholders;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import app.applications.newsapp.R;
import app.applications.newsapp.model.Article;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView description;
    TextView date;
    TextView author;
    TextView source;
    TextView url;

    ImageView imageView;

    public ArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.title);
        imageView = itemView.findViewById(R.id.imageView);
        description = itemView.findViewById(R.id.description);
        date = itemView.findViewById(R.id.date);
        author= itemView.findViewById(R.id.author);
        source= itemView.findViewById(R.id.source);
        url = itemView.findViewById(R.id.url);
    }

    public void bind(Article article) {
        if(article != null) {
            title.setText(article.getTitle());
            description.setText(article.getDescription());
            date.setText(article.getPublishedAt());
            author.setText(article.getAuthor());
            source.setText(""+article.getSource().getId() + " - "+article.getSource().getName());
            Picasso.get().load(article.getUrlToImage()).into(imageView);

            url.setClickable(true);
            url.setMovementMethod(LinkMovementMethod.getInstance());
            String text = "Link <a href='"+article.getUrl()+"'>"+article.getUrl()+"</a>";
            url.setText(Html.fromHtml(text));
        }
    }
}
