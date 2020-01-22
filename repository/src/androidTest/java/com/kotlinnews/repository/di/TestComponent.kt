package com.kotlinnews.repository.di

import com.kotlinnews.repository.RedditDbTest
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.di.RedditDbModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RedditDbModule::class])
interface TestComponent {

    fun inject(test: RedditDbTest)


    var db: RedditDb
}