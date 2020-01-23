package com.kotlinnews.api.reddit.models.res

import com.google.gson.annotations.SerializedName

/**
 * Represents the child of the news payload
 * */
data class KotlinNewsItemGetRes(
    @SerializedName("id")
    val id: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("thumbnail")
    val thumbnail: String?,

    @SerializedName("url")
    val url: String,

    @SerializedName("created_utc")
    val createdUtc: Long,

    @SerializedName("media")
    val media: KotlinNewsItemMediaGetRes?
)

data class KotlinNewsItemMediaGetRes(
    @SerializedName("type")
    val type: String?,
    @SerializedName("oembed")
    val oembed: KotlinNewsItemMediaOembed?
)

data class KotlinNewsItemMediaOembed(
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?
)