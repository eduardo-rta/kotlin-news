package com.kotlinnews.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.RedditRepository
import com.kotlinnews.repository.reddit.dao.NewsDao
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RedditNewsViewModel : ViewModel() {
    var compositeDisposable = CompositeDisposable()

    private lateinit var repository: RedditRepository

    var scheduler = Schedulers.io()

    fun initialize(newsDao: NewsDao, db: RedditDb, api: RedditRestApi) {
        repository = RedditRepository(compositeDisposable, scheduler, db, newsDao, api, 10)
    }

    var load = MutableLiveData<Unit>()
    val result = load.map { repository.getNews() }
    val news = result.switchMap { it.pagedList }
    val loadState = result.switchMap { it.loadState }
    val refreshState = result.switchMap { it.refreshState }

    fun refresh() {
        result.value?.refresh?.invoke()
    }

    fun retry() {
        result.value?.retry?.invoke()
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}