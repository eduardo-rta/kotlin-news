package com.kotlinnews.repository.reddit.utils

import com.kotlinnews.api.reddit.models.res.KotlinNewsItemGetRes
import com.kotlinnews.repository.reddit.entities.NewsEntity

/**
 * Converts a Response News Item to a database Entity
 * */
fun KotlinNewsItemGetRes.toNewsEntity(): NewsEntity {
    val thumb = when {
        !this.thumbnail.isNullOrBlank() -> this.thumbnail
        !this.media?.oembed?.thumbnailUrl.isNullOrBlank() -> this.media?.oembed?.thumbnailUrl ?: ""
        else -> null
    }
    return NewsEntity(0, this.id, this.title, this.url, thumb, this.url, this.createdUtc)
}