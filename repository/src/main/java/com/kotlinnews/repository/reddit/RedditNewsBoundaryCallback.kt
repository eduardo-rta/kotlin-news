package com.kotlinnews.repository.reddit

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit

class RedditNewsBoundaryCallback(
    private val compositeDisposable: CompositeDisposable,
    private val scheduler: Scheduler,
    private val api: RedditRestApi,
    private val pageSize: Int,
    private val handleResponseItems: (res: KotlinNewsGetRes) -> Unit
) : PagedList.BoundaryCallback<NewsEntity>() {

    var operationState = MutableLiveData<OperationState>(OperationState.loading())

    companion object {
        private var isRunningZeroItems = false
        private var isRunningItemsAtEnd = false
        private var errorAtEndItem: NewsEntity? = null
    }

    override fun onZeroItemsLoaded() {
        if (!isRunningZeroItems) {
            isRunningZeroItems = true
            operationState.postValue(OperationState.loading())
            compositeDisposable.add(
                api.getNewsSingle(pageSize)
                    //If you need to test the loading state, with progress, uncomment the line below
                    //.delay(5, TimeUnit.SECONDS)
                    .subscribeOn(scheduler)
                    .subscribe({
                        isRunningZeroItems = false
                        if (it.isSuccessful) {
                            it.body()?.let(handleResponseItems)
                            operationState.postValue(OperationState.success())
                        } else {
                            operationState.postValue(OperationState.error(Exception("Error loading data (${it.message()})")))
                        }
                    }, {
                        isRunningZeroItems = false
                        Timber.e(it)
                        operationState.postValue(OperationState.error(it))
                    })
            )
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: NewsEntity) {
        Timber.d("onItemAtEndLoaded - External (${itemAtEnd.name}) - (${pageSize})")
        loadAtEnd(itemAtEnd)
    }

    private fun loadAtEnd(itemAtEnd: NewsEntity) {
        if (!isRunningItemsAtEnd) {
            errorAtEndItem = null
            isRunningItemsAtEnd = true
            operationState.postValue(OperationState.loading())
            Timber.d("onItemAtEndLoaded (${itemAtEnd.name}) - (${pageSize})")
            compositeDisposable.add(
                api.getNewsAfterSingle(itemAtEnd.name, pageSize)
                    //If you need to test the loading state, with progress, uncomment the line below
                    //.delay(5, TimeUnit.SECONDS)
                    .subscribeOn(scheduler)
                    .subscribe({
                        isRunningItemsAtEnd = false
                        if (it.isSuccessful) {
                            it.body()?.let(handleResponseItems)
                            operationState.postValue(OperationState.success())
                        } else {
                            operationState.postValue(OperationState.error(Exception("Error loading data (${it.message()})")))
                        }
                    }, {
                        errorAtEndItem = itemAtEnd
                        isRunningItemsAtEnd = false
                        Timber.e(it)
                        operationState.postValue(OperationState.error(it))
                    })
            )
        }
    }

    fun retryFailed() {
        if (errorAtEndItem != null) {
            this.loadAtEnd(errorAtEndItem!!)
        }
    }
}