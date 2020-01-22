package com.kotlinnews.repository.reddit

import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.repository.reddit.dao.NewsDao
import io.reactivex.Observable
import javax.inject.Inject

class RedditRepository @Inject constructor(val newsDao: NewsDao, val api: RedditRestApi) {
    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

//    private fun getNetworkDataObservable(lastNews: String): Observable<String> {
//        return api.getNewsAfterSingle(lastNews, DEFAULT_PAGE_SIZE)
//            .toObservable()
//            .flatMap {
//                if (it.isSuccessful) {
//
//                }
//                Observable.just("a")
//            }
//    }
//
//    fun getRows() {
//        Observable.merge(api.)
//    }
}