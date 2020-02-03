package com.kotlinnews.repository.reddit.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "news", indices = [Index("news_id", unique = true)])
data class NewsEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "news_id") val newsId: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "thumbnail") val thumbnail: String?,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "created_utc") val createdUtc: Long
)