package com.kotlinnews.mvvm.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.RedditRepository
import com.kotlinnews.repository.reddit.dao.NewsDao
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RedditNewsViewModel @Inject constructor(
    private val newsDao: NewsDao,
    private val db: RedditDb,
    private val api: RedditRestApi
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val repository: RedditRepository = RedditRepository(compositeDisposable, Schedulers.io(), db, newsDao, api, 20)

    var load = MutableLiveData<Unit>()
    private val result = load.map { repository.getNews() }
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