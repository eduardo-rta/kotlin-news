package com.kotlinnews.repository.reddit.useCases

import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.entities.NewsEntity
import com.kotlinnews.repository.reddit.utils.toNewsEntity
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class RedditNewsUseCase @Inject constructor(
    private val api: RedditRestApi,
    private val dao: NewsDao,
    private val db: RedditDb,
    private val compositeDisposable: CompositeDisposable,
    private val pageSize: Int = 20,
    private val shouldLoadDiffFromApi: Boolean = true,
    private val shouldLoadInitialDiffFromApi: Boolean = false
) {

    //We use synchronized to ensure other threads won't be running this method at the same time
    @Synchronized
    private fun processApiResponse(response: Response<KotlinNewsGetRes>): Single<List<NewsEntity>> {
        if (!response.isSuccessful) {
            return Single.error(HttpException(response))
        }

        val body = response.body()
        if (body == null || body.news.count() == 0) {
            return Single.just(emptyList())
        }

        val newItems = body.news.map { it.toNewsEntity() }.sortedByDescending { it.createdUtc }
        //We get the last id before inserting the new rows
        return dao.getLastIdRx().flatMap { lastId ->
            Single.fromCallable {
                db.runInTransaction {
                    dao.insertAll(newItems)
                }
            }.map { lastId }
        }.flatMap { lastId ->
            //We load the rows inserted, lastId will be the last Id before inserting them
            dao.getNewsAfterIdSingle(lastId)
        }
    }


    fun getLoadInitialObservable(): Single<List<NewsEntity>> {
        return dao.getAllPagedRx(pageSize, 0)
            .flatMap { dbItems ->
                if (dbItems.isEmpty()) {
                    //If no news in the db, we load from the API
                    api.getNewsRx(pageSize).flatMap { apiResponse ->
                        this.processApiResponse(apiResponse)
                    }
                } else if (dbItems.count() < pageSize && shouldLoadInitialDiffFromApi) {
                    //If the number of rows in the database does not match the required pageSize we load from the web trying to fulfill the list
                    //to test this feature, the very first call you can do (getAllPagedRx(pageSize - 1 {or any number})
                    //Then try to load just the difference
                    val diff = pageSize - dbItems.count()
                    api.getNewsBeforeRx(dbItems.first().name, diff).flatMap { apiResponse ->
                        this.processApiResponse(apiResponse)
                    }
                } else {
                    Single.just(dbItems)
                }
            }
    }

    fun getNewsAfterObservable(key: String): Single<List<NewsEntity>> {
        return dao.getAfterKeyRx(key, pageSize, 0)
            .flatMap { dbItems ->
                if (dbItems.isEmpty()) {
                    api.getNewsAfterRx(key, pageSize).flatMap { apiResponse ->
                        this.processApiResponse(apiResponse)
                    }
                } else if (dbItems.count() < pageSize && shouldLoadDiffFromApi) {
                    val diff = pageSize - dbItems.count()
                    val afterKey = dbItems.firstOrNull()?.name ?: key
                    api.getNewsAfterRx(afterKey, diff).flatMap { apiResponse ->
                        this.processApiResponse(apiResponse)
                    }
                } else {
                    Single.just(dbItems)
                }
            }
    }

    fun getNewsBeforeObservable(key: String): Single<List<NewsEntity>> {
        return dao.getBeforeKeyRx(key, pageSize, 0)
            .flatMap { dbItems ->
                if (dbItems.isEmpty()) {
                    api.getNewsBeforeRx(key, pageSize).flatMap { apiResponse ->
                        this.processApiResponse(apiResponse)
                    }
                } else if (dbItems.count() < pageSize && shouldLoadDiffFromApi) {
                    val diff = pageSize - dbItems.count()
                    val beforeKey = dbItems.lastOrNull()?.name ?: key
                    api.getNewsBeforeRx(beforeKey, diff).flatMap { apiResponse ->
                        this.processApiResponse(apiResponse)
                    }
                } else {
                    Single.just(dbItems)
                }
            }
    }
}