package com.kotlinnews.api.reddit

import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RedditRestApi {
    /**
     * Returns the items after an specific point (older news)
     * @param after name of the last news (it would be the oldest news loaded, the bottom of the list)
     * */
    @GET("r/kotlin/.json")
    fun getNewsAfterRx(@Query("after") after: String, @Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>

    /**
     * Returns the items before an specific point (newer news)
     * @param before name of the news you want to start the search (it would be newest news loaded, the top of the list)
     * */
    @GET("r/kotlin/.json")
    fun getNewsBeforeRx(@Query("before") before: String, @Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>

    @GET("r/kotlin/.json")
    fun getNewsRx(@Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>


    @GET("r/kotlin/.json")
    fun getNewsAfter(@Query("after") after: String, @Query("limit") limit: Int): Call<KotlinNewsGetRes>

    /**
     * Returns the items before an specific point (newer news)
     * @param before name of the news you want to start the search (it would be newest news loaded, the top of the list)
     * */
    @GET("r/kotlin/.json")
    fun getNewsBefore(@Query("before") before: String, @Query("limit") limit: Int): Call<KotlinNewsGetRes>

    @GET("r/kotlin/.json")
    fun getNews(@Query("limit") limit: Int): Call<KotlinNewsGetRes>


}