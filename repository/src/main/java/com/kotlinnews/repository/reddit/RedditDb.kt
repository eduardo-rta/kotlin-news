package com.kotlinnews.repository.reddit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.entities.NewsEntity

@Database(entities = [NewsEntity::class], version = 1)
abstract class RedditDb : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        fun createDb(context: Context): RedditDb {
            return Room.databaseBuilder(context, RedditDb::class.java, "reddit_db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}