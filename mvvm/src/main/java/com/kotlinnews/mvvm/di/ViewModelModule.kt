package com.kotlinnews.mvvm.di

import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.mvvm.RedditNewsViewModel
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
//    @Provides
//    fun providesRedditNewsViewModel(newsDao: NewsDao, db: RedditDb, api: RedditRestApi): RedditNewsViewModel {
//        return RedditNewsViewModel(newsDao, db, api)
//    }
}