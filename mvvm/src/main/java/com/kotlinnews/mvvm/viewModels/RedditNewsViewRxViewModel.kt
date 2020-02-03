package com.kotlinnews.mvvm.viewModels

import androidx.lifecycle.ViewModel
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import javax.inject.Inject

class RedditNewsViewRxViewModel @Inject constructor(
    private val newsDao: NewsDao,
    private val db: RedditDb,
    private val api: RedditRestApi
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable(

    )
    private lateinit var itemsEmmiter: ObservableEmitter<NewsEntity>
    val itemsObservable: Observable<NewsEntity> = Observable.create { emmiter ->
        itemsEmmiter = emmiter
    }


    fun loadNews() {
        this.compositeDisposable.add(
            //We concat because we want to wait until the local is loaded before triggering the remote data
            Observable.concat(getLocalData(), getRemoteData())
                .subscribeOn(Schedulers.io())
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

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}