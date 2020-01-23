package com.kotlinnews.repository.reddit

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.PagingRequestHelper
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.Executor

class RedditNewsBoundaryCallback(
    private val compositeDisposable: CompositeDisposable,
    private val scheduler: Scheduler,
    private val api: RedditRestApi,
    private val pageSize: Int,
    private val ioExecutor: Executor,
    private val handleResponseItems: (res: KotlinNewsGetRes) -> Unit
) : PagedList.BoundaryCallback<NewsEntity>() {

    var helper = PagingRequestHelper(ioExecutor)
    var operationState = MutableLiveData<OperationState>(OperationState.loading())

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL) {
            compositeDisposable.add(
                api.getNewsSingle(pageSize)
                    .subscribeOn(scheduler)
                    .subscribe({
                        if (it.isSuccessful) {
                            it.body()?.let(handleResponseItems)
                            operationState.postValue(OperationState.success())
                        } else {
                            operationState.postValue(OperationState.error(Exception("Error loading data (${it.message()})")))
                        }
                    }, {
                        Timber.e(it)
                        operationState.postValue(OperationState.error(it))
                    })
            )
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: NewsEntity) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER) {
            Timber.d("onItemAtEndLoaded (${itemAtEnd.name}) - (${pageSize})")
            compositeDisposable.add(
                api.getNewsAfterSingle(itemAtEnd.name, pageSize)
                    .subscribeOn(scheduler)
                    .subscribe({
                        if (it.isSuccessful) {
                            it.body()?.let(handleResponseItems)
                            operationState.postValue(OperationState.success())
                        } else {
                            operationState.postValue(OperationState.error(Exception("Error loading data (${it.message()})")))
                        }
                    }, {
                        Timber.e(it)
                        operationState.postValue(OperationState.error(it))
                    })
            )
        }
    }
}