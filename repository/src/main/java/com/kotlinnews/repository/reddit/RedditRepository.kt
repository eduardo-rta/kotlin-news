package com.kotlinnews.repository.reddit

import android.app.usage.NetworkStats
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import com.kotlinnews.api.reddit.models.res.KotlinNewsItemGetRes
import com.kotlinnews.repository.GenericListResult
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.Executor
import javax.inject.Inject
import androidx.lifecycle.Transformations
import androidx.lifecycle.switchMap
import androidx.paging.toLiveData


class RedditRepository constructor(
    private val compositeDisposable: CompositeDisposable,
    private val scheduler: Scheduler,
    private val db: RedditDb,
    private val newsDao: NewsDao,
    private val api: RedditRestApi,
    private val pageSize: Int
) {


    private fun insertNews(items: List<KotlinNewsItemGetRes>) {
        db.runInTransaction {
            newsDao.insertAll(items.map {
                //id is auto-generated
                NewsEntity(0, it.id, it.title, it.name, it.thumbnail, it.url, it.createdUtc)
            })
        }
    }

    /**
     * Refreshes the search (loading from the top) and returns the operation state as Live Data
     * Deletes all news from the database as we want to reload the data
     * Saves the result of the refresh into the database
     * */
    @MainThread
    private fun refresh(): LiveData<OperationState> {
        val state = MutableLiveData<OperationState>()
        state.value = OperationState.loading()
        compositeDisposable.add(api.getNewsSingle(pageSize)
            .flatMap {
                if (it.isSuccessful) {
                    it.body()?.let { body ->
                        db.runInTransaction {
                            this.newsDao.deleteAll()
                            this.insertNews(body.news)
                        }
                    }
                }
                Single.just(it)
            }
            .subscribeOn(scheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccessful) {
                    state.postValue(OperationState.success())
                } else {
                    state.postValue(OperationState.error(null, it.message()))
                }
            }, {
                state.postValue(OperationState.error(it))
            })
        )
        return state
    }


    /**
     * Retrieve the news from the database. If nothing is present, it loads from the API
     * @return Returns a GenericListResult object which contains all properties and live data for statuses and the list, as well as the retry and refresh methods
     * */
    @MainThread
    public fun getNews(): GenericListResult<NewsEntity> {
        val boundaryCallback =
            RedditNewsBoundaryCallback(compositeDisposable, scheduler, api, pageSize) {
                this.insertNews(it.news)
            }
        val refreshTrigger = MutableLiveData<Unit>()
        val refreshState = refreshTrigger.switchMap {
            refresh()
        }

        val pagedList = newsDao.getAllPaged()
            .toLiveData(pageSize = pageSize, boundaryCallback = boundaryCallback)

        return GenericListResult(
            pagedList = pagedList,
            loadState = boundaryCallback.operationState,
            refreshState = refreshState,
            refresh = {
                //TODO: Why?
                refreshTrigger.value = null
            },
            retry = {
                //TODO: No retry for now
            })
    }
}

