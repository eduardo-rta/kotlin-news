package com.kotlinnews.api.reddit

import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditRestApi {
    /**
     * Returns the the new items after an specific point
     * @param after last
     * */
    @GET("r/kotlin/.json")
    fun getNewsAfterSingle(@Query("after") after: String, @Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>

    @GET("r/kotlin/.json")
    fun getNewsSingle(@Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>
}