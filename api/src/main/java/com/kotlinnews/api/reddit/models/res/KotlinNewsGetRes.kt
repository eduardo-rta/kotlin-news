package com.kotlinnews.api.reddit.models.res

import com.google.gson.annotations.SerializedName

/**
 * Response model for r/kotlin/.json
 * We are ignoring fields that are not important for the call at this point
 * */

data class KotlinNewsGetRes(
    val news: List<KotlinNewsItemGetRes>
)