package com.kotlinnews.repository.reddit.di

import android.content.Context
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class RedditDbModule(private val context: Context) {
    @Provides
    @Singleton
    fun providesRedditDb(): RedditDb {
        return RedditDb.createDb(context)
    }

    @Singleton
    @Provides
    fun provideNewsDao(db: RedditDb): NewsDao {
        return db.newsDao()
    }
}