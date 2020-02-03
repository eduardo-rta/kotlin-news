package com.kotlinnews.mvvm.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.kotlinnews.repository.reddit.dao.NewsDao
import javax.inject.Inject

class RedditNewsDetailRxViewModel @Inject constructor(private val newsDao: NewsDao) : ViewModel() {
    var loadNews = MutableLiveData<Long>()

    var news = loadNews.switchMap {
        newsDao.getSpecificNews(it)
    }
}