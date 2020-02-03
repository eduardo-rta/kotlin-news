package com.kotlinnews.di

import com.kotlinnews.api.reddit.di.RedditApiModule
import com.kotlinnews.mvvm.di.ViewModelModule
import com.kotlinnews.repository.reddit.di.RedditDbModule
import com.kotlinnews.repository.reddit.di.RedditRepositoryModule
import com.kotlinnews.ui.activities.main.MainActivity
import com.kotlinnews.ui.activities.news.NewsActivity
import com.kotlinnews.ui.activities.news.NewsRxActivity
import com.kotlinnews.ui.activities.newsDetail.NewsDetailActivity
import com.kotlinnews.ui.activities.newsDetail.NewsDetailRxActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RedditDbModule::class, RedditRepositoryModule::class, RedditApiModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
    fun inject(activity: NewsActivity)
    fun inject(activity: NewsRxActivity)
    fun inject(activity: NewsDetailActivity)
    fun inject(activity: NewsDetailRxActivity)
}