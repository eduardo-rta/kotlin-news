package com.kotlinnews.repository.reddit

import android.os.AsyncTask
import androidx.annotation.MainThread
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.models.res.KotlinNewsItemGetRes
import com.kotlinnews.repository.GenericListResult
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import androidx.paging.toLiveData
import com.kotlinnews.repository.reddit.utils.toNewsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.Executor


class RedditNewsRepository constructor(
    private val db: RedditDb,
    private val newsDao: NewsDao,
    private val api: RedditRestApi,
    private val pageSize: Int,
    private val executor: Executor
) {
    /**
     * Inserts a list of data coming from network into the database
     * Must not be called from Main Thread
     * */
    private fun insertNews(items: List<KotlinNewsItemGetRes>) {
        this.db.runInTransaction {
            this.newsDao.insertAll(items.map { it.toNewsEntity() })
        }
    }

    /**
     * Refreshes the search (loading from the top) and returns the operation state as Live Data
     * Deletes all news from the database as we want to reload the data
     * Saves the result of the refresh into the database
     * */
    @MainThread
    private fun refresh(): LiveData<OperationState> {
        val state = MutableLiveData<OperationState>(OperationState.loading())
        this.executor.execute {
            newsDao.deleteAll()
            try {
                val response = this.api.getNews(this.pageSize).execute()
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        this.db.runInTransaction {
                            this.newsDao.deleteAll()
                            this.insertNews(body.news)
                        }
                    }
                    state.postValue(OperationState.success())
                } else {
                    state.postValue(OperationState.error(null, response.message()))
                }
            } catch (t: Throwable) {
                state.postValue(OperationState.error(t))
            }
        }
        return state
    }


    /**
     * Retrieve the news from the database. If nothing is present, it loads from the API
     * @return Returns a GenericListResult object which contains all properties and live data for statuses and the list, as well as the retry and refresh methods
     * */
    @MainThread
    fun getResults(): GenericListResult<NewsEntity> {
        Timber.d("getResults")

        val boundaryCallback = RedditNewsBoundaryCallback(
            this.api,
            this.pageSize,
            this.executor
        ) {
            this.insertNews(it.news)
        }

        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap {
            this.refresh()
        }

        val config = PagedList.Config.Builder()
            .setPrefetchDistance(3)
            .setPageSize(pageSize)
            .build()

        val pagedList = this.newsDao.getAllDataSource()
            .toLiveData(boundaryCallback = boundaryCallback, config = config)

        return GenericListResult(
            pagedList = pagedList,
            loadState = boundaryCallback.operationState,
            loadAtFrontState = boundaryCallback.operationFrontState,
            refreshState = refreshState,
            refresh = {
                boundaryCallback.operationFrontState.postValue(OperationState.success(null))
                refreshTrigger.value = null
            },
            retry = {
                boundaryCallback.retryFailed()
            })
    }
}

