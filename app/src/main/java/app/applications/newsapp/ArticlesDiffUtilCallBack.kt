/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp

import androidx.recyclerview.widget.DiffUtil
import app.applications.newsapp.model.Article

class ArticlesDiffUtilCallBack : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.pk == newItem.pk
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.equals(newItem);
    }
}