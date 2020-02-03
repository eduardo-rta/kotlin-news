package com.kotlinnews.repository.reddit.dataSource

import androidx.paging.PageKeyedDataSource
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.entities.NewsEntity
import com.kotlinnews.repository.reddit.useCases.RedditNewsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RedditNewsRxDataSource @Inject constructor(
    private val api: RedditRestApi,
    private val dao: NewsDao,
    private val db: RedditDb,
    private val compositeDisposable: CompositeDisposable,
    private val pageSize: Int = 20,
    private val shouldLoadDiffOnInitial: Boolean = true,
    private val shouldLoadInitialDiffFromApi: Boolean = false
) : PageKeyedDataSource<String, NewsEntity>() {
    private val useCase = RedditNewsUseCase(api, dao, db, compositeDisposable, pageSize, shouldLoadDiffOnInitial, shouldLoadInitialDiffFromApi)

    /**
     * We first try to load it locally, if no results found we load from the api
     * */
    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<String, NewsEntity>
    ) {
//        params.requestedLoadSize
        compositeDisposable.add(
            //Reads the database for news
            this.useCase.getLoadInitialObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onResult(it, 0, it.count(), null, it.lastOrNull()?.name)
                }, {
                    callback.onError(it)
                })
        )
    }

    override fun loadAfter(
        params: LoadParams<String>,
        callback: LoadCallback<String, NewsEntity>
    ) {
        compositeDisposable.add(
            //Reads the database for news
            this.useCase.getNewsAfterObservable(params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onResult(it, it.lastOrNull()?.name)
                }, {
                    callback.onError(it)
                })
        )
    }

    override fun loadBefore(
        params: LoadParams<String>,
        callback: LoadCallback<String, NewsEntity>
    ) {
        compositeDisposable.add(
            //Reads the database for news
            this.useCase.getNewsBeforeObservable(params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    callback.onResult(it, it.lastOrNull()?.name)
                }, {
                    callback.onError(it)
                })
        )
    }
}