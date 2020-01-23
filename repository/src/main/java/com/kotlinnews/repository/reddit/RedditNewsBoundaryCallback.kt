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

class RedditNewsBoundaryCallback(
    private val compositeDisposable: CompositeDisposable,
    private val scheduler: Scheduler,
    private val api: RedditRestApi,
    private val pageSize: Int,
    private val handleResponseItems: (res: KotlinNewsGetRes) -> Unit
) : PagedList.BoundaryCallback<NewsEntity>() {

    var operationState = MutableLiveData<OperationState>(OperationState.success())

    override fun onZeroItemsLoaded() {
        compositeDisposable.add(
            api.getNewsSingle(pageSize)
                .subscribeOn(scheduler)
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let(handleResponseItems)
                        operationState.postValue(OperationState.success())
                    } else {
//                        throw Exception("Error loading data (${it.message()})")
                        operationState.postValue(OperationState.error(Exception("Error loading data (${it.message()})")))
                    }
                }, {
                    Timber.e(it)
//                    throw it
                    operationState.postValue(OperationState.error(it))
                })
        )
    }

    override fun onItemAtEndLoaded(itemAtEnd: NewsEntity) {
        compositeDisposable.add(
            api.getNewsSingle(pageSize)
                .subscribeOn(scheduler)
                .subscribe({
                    if (it.isSuccessful) {
                        it.body()?.let(handleResponseItems)
                        operationState.postValue(OperationState.success())
                    } else {
//                        throw Exception("Error loading data (${it.message()})")
                        operationState.postValue(OperationState.error(Exception("Error loading data (${it.message()})")))
                    }
                }, {
                    Timber.e(it)
                    operationState.postValue(OperationState.error(it))
//                    throw it
                })
        )
    }
}