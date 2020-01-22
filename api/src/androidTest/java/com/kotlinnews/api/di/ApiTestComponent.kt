package com.kotlinnews.api.di

import com.kotlinnews.api.AndroidApiTest
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.di.RedditApiModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RedditApiModule::class])
interface ApiTestComponent {

    fun inject(test: AndroidApiTest)

    var api: RedditRestApi
}