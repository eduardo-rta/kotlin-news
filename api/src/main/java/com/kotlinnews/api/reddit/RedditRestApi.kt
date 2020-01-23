package com.kotlinnews.api.reddit

import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//TODO: Rewrite documentation
interface RedditRestApi {
    /**
     * @param before Indicates that you want the new items to be before the indicated one
     * @param limit Number of items that will be loaded in the call
     * @return Returns a Single with the response of the call
     * */
    @GET("r/kotlin/.json")
    fun getNewsBeforeSingle(@Query("before") before: String, @Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>

    @GET("r/kotlin/.json")
    fun getNewsBefore(@Query("before") before: String, @Query("limit") limit: Int): Call<KotlinNewsGetRes>

    /**
     * Returns the the new items after an specific point
     * @param after last
     * */
    @GET("r/kotlin/.json")
    fun getNewsAfterSingle(@Query("after") after: String, @Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>

    @GET("r/kotlin/.json")
    fun getNewsAfter(@Query("after") after: String, @Query("limit") limit: Int): Call<KotlinNewsGetRes>


    @GET("r/kotlin/.json")
    fun getNews(@Query("limit") limit: Int): Call<KotlinNewsGetRes>

    @GET("r/kotlin/.json")
    fun getNewsSingle(@Query("limit") limit: Int): Single<Response<KotlinNewsGetRes>>

}