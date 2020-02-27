package com.kotlinnews.mvvm.viewModels.news

import androidx.lifecycle.*
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.RedditNewsRepository
import com.kotlinnews.repository.reddit.dao.NewsDao
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

class RedditNewsViewModel @Inject constructor(
    private val newsDao: NewsDao,
    private val db: RedditDb,
    private val api: RedditRestApi
) : ViewModel(), IRedditNewsViewModel {
    private val executor = Executors.newSingleThreadExecutor()

    private val newsRepository: RedditNewsRepository = RedditNewsRepository(this.db, this.newsDao, this.api, 20, this.executor)

    private var loadLiveData = MutableLiveData<Unit>()
    private val result = loadLiveData.map { newsRepository.getResults() }
    val newsLiveData = result.switchMap { it.pagedList }
    val loadStateLiveData = result.switchMap { it.loadState }
    val refreshStateLiveData = result.switchMap { it.refreshState }
    val loadAtFrontStateLiveData = result.switchMap { it.loadAtFrontState }

    override fun refresh() {
        result.value?.refresh?.invoke()
    }

    override fun retry() {
        result.value?.retry?.invoke()
    }

    override fun load() {
        Timber.d("load")
        this.loadLiveData.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
    }
}