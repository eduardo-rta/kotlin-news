package com.kotlinnews.repository.reddit.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface NewsDao {

    @Query("select id, news_id, title, name, thumbnail, url, created_utc from news where id = :id")
    fun getNews(id: Long): LiveData<NewsEntity>

    @Query("select id, news_id, title, name, thumbnail, url, created_utc from news")
    fun getAll(): List<NewsEntity>

    @Query("select id, news_id, title, name, thumbnail, url, created_utc from news order by id ASC")
    fun getAllPaged(): DataSource.Factory<Int, NewsEntity>

    @Query("select id, news_id, title, name, thumbnail, url, created_utc from news LIMIT :pageSize offset :startsFrom")
    fun getAllPagedRx(pageSize: Int, startsFrom: Int): Single<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: NewsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entity: List<NewsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingle(entity: NewsEntity): Single<Long>

    @Delete
    fun delete(entity: NewsEntity)

    @Query("delete from news where 1 = 1")
    fun deleteAll()

}