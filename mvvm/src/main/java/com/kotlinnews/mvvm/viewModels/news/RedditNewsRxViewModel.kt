package com.kotlinnews.mvvm.viewModels.news

import androidx.lifecycle.ViewModel
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

class RedditNewsRxViewModel @Inject constructor(
    private val newsDao: NewsDao,
    private val db: RedditDb,
    private val api: RedditRestApi
) : ViewModel(), IRedditNewsViewModel {
    private val compositeDisposable = CompositeDisposable()

    private lateinit var itemsEmitter: ObservableEmitter<NewsEntity>
    val itemsObservable: Observable<NewsEntity> = Observable.create { emmiter ->
        itemsEmitter = emmiter
    }

    private lateinit var refreshStateEmitter: ObservableEmitter<OperationState>
    val refreshStateObservable = Observable.create<OperationState> {
        refreshStateEmitter = it
    }

    private lateinit var loadStateEmitter: ObservableEmitter<OperationState>
    val loadStateObservable = Observable.create<OperationState> {
        loadStateEmitter = it
    }

    private lateinit var loadAtFrontStateEmitter: ObservableEmitter<OperationState>
    val loadAtFrontStateObservable = Observable.create<OperationState> {
        loadAtFrontStateEmitter = it
    }

    override fun load() {
        this.compositeDisposable.add(
            //We concat because we want to wait until the local is loaded before triggering the remote data
            Observable.concat(getLocalData(), getRemoteData())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe({

                })
        )
    }

    /**
     * Loads the data from current database
     * */
    private fun getLocalData(): Observable<NewsEntity> {
        return Observable.error(Exception(""))

    }

    /**
     * Loads teh data from the API and saves it locally, after that returns the payload to the screen
     * */
    private fun getRemoteData(): Observable<NewsEntity> {
        return Observable.error(Exception(""))
    }

    fun setFilter(filter: String) {

    }

    override fun refresh() {
        TODO("Not yet implemented")
    }

    override fun retry() {
        TODO("Not yet implemented")
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}