package com.kotlinnews.mvvm.viewModels

import androidx.lifecycle.*
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.RedditNewsRepository
import com.kotlinnews.repository.reddit.dao.NewsDao
import java.util.concurrent.Executors
import javax.inject.Inject

class RedditNewsViewModel @Inject constructor(
    private val newsDao: NewsDao,
    private val db: RedditDb,
    private val api: RedditRestApi
) : ViewModel() {
    private val executor = Executors.newSingleThreadExecutor()

    private val newsRepository: RedditNewsRepository = RedditNewsRepository(this.db, this.newsDao, this.api, 20, this.executor)

    private var load = MutableLiveData<Unit>()
    private val result = load.map { newsRepository.getResults() }
    val news = result.switchMap { it.pagedList }
    val loadState = result.switchMap { it.loadState }
    val refreshState = result.switchMap { it.refreshState }
    val loadAtFrontState = result.switchMap { it.loadAtFrontState }

    init {
    }

    fun refresh() {
        result.value?.refresh?.invoke()
    }

    fun retry() {
        result.value?.retry?.invoke()
    }

    fun loadNews() {
        this.load.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
    }
}