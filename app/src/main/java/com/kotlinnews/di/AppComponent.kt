package com.kotlinnews.di

import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.api.reddit.di.RedditApiModule
import com.kotlinnews.mvvm.di.ViewModelModule
import com.kotlinnews.repository.reddit.RedditRepository
import com.kotlinnews.repository.reddit.di.RedditDbModule
import com.kotlinnews.repository.reddit.di.RedditRepositoryModule
import com.kotlinnews.ui.activities.newsDetail.NewsDetailActivity
import com.kotlinnews.ui.fragments.main.MainFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RedditDbModule::class, RedditRepositoryModule::class, RedditApiModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(fragment: MainFragment)
    fun inject(activity: NewsDetailActivity)
}