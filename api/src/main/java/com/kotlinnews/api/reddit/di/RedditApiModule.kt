package com.kotlinnews.api.reddit.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kotlinnews.api.BuildConfig
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.models.deserializers.KotlinNewsDeserializer
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class RedditApiModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)

    @Provides
    @Singleton
    fun provideStethoInterceptor(): StethoInterceptor =
        StethoInterceptor()

    @Provides
    @Singleton
    @Named("httpClient")
    fun provideOkHttp(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        stethoInterceptor: StethoInterceptor
    ): OkHttpClient =
        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(stethoInterceptor)
                .readTimeout(45, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
        } else {
            OkHttpClient.Builder()
                .readTimeout(45, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build()
        }

    @Provides
    @Singleton
    fun provideRxJavaCallAdapterFactory() = RxJava2CallAdapterFactory.create()

    @Provides
    @Singleton
    @Named("RedditGson")
    fun providesGson(): Gson =
        GsonBuilder()
            .registerTypeAdapter(KotlinNewsGetRes::class.java, KotlinNewsDeserializer())
            .create()

    @Provides
    @Singleton
    fun providesRetrofitApi(
        @Named("RedditGson") gson: Gson,
        rxJavaCallAdapterFactory: RxJava2CallAdapterFactory,
        @Named("httpClient") httpClient: OkHttpClient
    ): RedditRestApi =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl("https://www.reddit.com/")
            .addCallAdapterFactory(rxJavaCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create()


}