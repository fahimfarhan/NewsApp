/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import app.applications.newsapp.repository.NewsRepository

class NewsViewModel : AndroidViewModel {

    val newsRepository:NewsRepository;

    public constructor(application: Application):super(application){
        this.newsRepository = NewsRepository(application);
    }
}