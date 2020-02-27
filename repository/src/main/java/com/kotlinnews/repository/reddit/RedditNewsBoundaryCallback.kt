package com.kotlinnews.repository.reddit

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.reddit.entities.NewsEntity
import timber.log.Timber
import java.util.concurrent.Executor

/**
 * Callback class to handle the all different scenarios in the PagedList live data
 * */
class   RedditNewsBoundaryCallback(
    private val api: RedditRestApi,
    private val pageSize: Int,
    private val executor: Executor,
    private val handleResponseItems: (res: KotlinNewsGetRes) -> Unit
) : PagedList.BoundaryCallback<NewsEntity>() {

    var operationState = MutableLiveData<OperationState>(OperationState.loading())
    var operationFrontState = MutableLiveData<OperationState>(OperationState.success())

    private var shouldLoadItemsAtFront = true

    //Variables to avoid method to be called multiple times
    private var isRunningZeroItems = false
    private var isRunningItemsAtEnd = false
    private var isRunningItemsAtFront = false
    private var errorAtEndItem: NewsEntity? = null
    private var lastItemAtFrontLoaded: String = ""

    override fun onItemAtFrontLoaded(itemAtFront: NewsEntity) {
        if (!this.shouldLoadItemsAtFront || this.isRunningItemsAtFront || itemAtFront.newsId == this.lastItemAtFrontLoaded) {
            return
        }

        this.lastItemAtFrontLoaded = itemAtFront.newsId

        Timber.d("onItemAtFrontLoaded - ${itemAtFront.name}")
        this.isRunningItemsAtFront = true
        this.operationFrontState.postValue(OperationState.loading())
        this.executor.execute {
            try {
                //We load 1 just to know if there is any updated news. After that a refresh is called
                val response = this.api.getNewsBefore(itemAtFront.name, 1).execute()
                if (response.isSuccessful) {
                    Timber.d("onItemAtFrontLoaded: Total: ${response.body()?.news?.count() ?: 0}")
                    this.operationFrontState.postValue(OperationState.success(response.body()?.news?.count()))
                } else {
                    this.operationFrontState.postValue(OperationState.error(Exception("Error loading data (${response.message()})")))
                }
            } catch (t: Throwable) {
                Timber.e(t)
                this.operationFrontState.postValue(OperationState.error(Exception("Error loading data (${t.message ?: t.localizedMessage ?: "--No Description--"})")))
            }
        }
    }

    override fun onZeroItemsLoaded() {
        Timber.d("onZeroItemsLoaded")
        if (!this.isRunningZeroItems) {
            Timber.d("onZeroItemsLoaded-true")
            this.isRunningZeroItems = true

            //If no items in the database, it means we're getting all new items from the api in the first place. In this case we should not call loadItemsAtFront
            this.shouldLoadItemsAtFront = false

            this.operationState.postValue(OperationState.loading())

            executor.execute {
                try {
                    //TODO: Uncomment line below and delete getNewsAfter line, The getNewsAfter at this point is to force testing the "Update News" button
//                    val response = this.api.getNews(pageSize).execute()

                    val response = this.api.getNewsAfter("t3_eta1en", pageSize).execute()
                    this.isRunningZeroItems = false
                    if (response.isSuccessful) {
                        response.body()?.let(handleResponseItems)
                        this.operationState.postValue(OperationState.success())
                    } else {
                        this.operationState.postValue(OperationState.error(Exception("Error loading data (${response.message()})")))
                    }
                } catch (t: Throwable) {
                    Timber.e(t)
                    this.isRunningZeroItems = false
                    this.operationState.postValue(OperationState.error(t))
                }
            }
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: NewsEntity) {
        Timber.d("onItemAtEndLoaded - (${itemAtEnd.name}) - (${pageSize})")
        this.loadAtEnd(itemAtEnd)
    }

    private fun loadAtEnd(itemAtEnd: NewsEntity) {
        if (!this.isRunningItemsAtEnd) {
            this.errorAtEndItem = null
            this.isRunningItemsAtEnd = true
            this.operationState.postValue(OperationState.loading())
            Timber.d("onItemAtEndLoaded (${itemAtEnd.name}) - (${pageSize})")
            this.executor.execute {
                try {
                    val response = this.api.getNewsAfter(itemAtEnd.name, pageSize).execute()
                    if (response.isSuccessful) {
                        response.body()?.let(handleResponseItems)
                        this.operationState.postValue(OperationState.success())
                    } else {
                        this.operationState.postValue(OperationState.error(Exception("Error loading data (${response.message()})")))
                    }
                    this.isRunningItemsAtEnd = false
                } catch (t: Throwable) {
                    Timber.e(t)
                    this.errorAtEndItem = itemAtEnd
                    this.operationState.postValue(OperationState.error(t))
                    this.isRunningItemsAtEnd = false
                }
            }
        }
    }

    fun retryFailed() {
        if (this.errorAtEndItem != null) {
            this.loadAtEnd(this.errorAtEndItem!!)
        }
    }
}