package com.kotlinnews.repository.reddit.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.kotlinnews.repository.reddit.entities.NewsEntity
import io.reactivex.Completable
import io.reactivex.Single


@Dao
interface NewsDao {

    // Live Data

    @Query("select * from news where id = :id")
    fun getSpecificNews(id: Long): LiveData<NewsEntity>

    @Query("select * from news order by id ASC")
    fun getAllDataSource(): DataSource.Factory<Int, NewsEntity>

//    @Query("select max(id) from news")
//    fun getLastId(): LiveData<Long>
//
//    @Query("select max(created_utc) from news")
//    fun getLastCreationUtc(): LiveData<Long>


//    @Query("select * from news where created_utc <= :creationUtc order by created_utc DESC")
//    fun getAllBeforeCreation(creationUtc: Long): DataSource.Factory<Int, NewsEntity>
//
//    @Query("select * from news where created_utc > :creationUtc order by created_utc DESC")
//    fun getAllAfterCreation(creationUtc: Long): DataSource.Factory<Int, NewsEntity>


    //Rx

    @Query("select * from news where id = :id")
    fun getSpecificNewsRx(id: Long): Single<NewsEntity>

    @Query("select * from news order by id ASC LIMIT :pageSize offset :startsFrom")
    fun getAllPagedRx(pageSize: Int, startsFrom: Int): Single<List<NewsEntity>>

    @Query("select n.* from news n where n.id > (select ifnull(n2.id, 0) from news n2 where n2.name = :after)  order by created_utc DESC LIMIT :pageSize offset :startsFrom")
    fun getAfterKeyRx(after: String, pageSize: Int, startsFrom: Int): Single<List<NewsEntity>>

    @Query("select n.* from news n where n.id < (select ifnull(n2.id, 0) from news n2 where n2.name = :before)  order by created_utc DESC LIMIT :pageSize offset :startsFrom")
    fun getBeforeKeyRx(before: String, pageSize: Int, startsFrom: Int): Single<List<NewsEntity>>

    @Query("select max(id) from news")
    fun getLastIdRx(): Single<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRx(entity: NewsEntity): Single<Long>

    @Query("select * from news where id > :id order by created_utc desc")
    fun getNewsAfterIdSingle(id: Long): Single<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRx(entity: List<NewsEntity>): Completable


    //Sync
    @Query("select * from news  order by created_utc DESC")
    fun getAll(): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: NewsEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entity: List<NewsEntity>)

    @Delete
    fun delete(entity: NewsEntity)

    @Query("delete from news where 1 = 1")
    fun deleteAll()


}