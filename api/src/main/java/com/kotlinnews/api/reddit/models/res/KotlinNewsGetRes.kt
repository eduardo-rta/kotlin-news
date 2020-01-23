package com.kotlinnews.api.reddit.models.res

import com.google.gson.annotations.SerializedName

/**
 * Response model for r/kotlin/.json
 * We are ignoring fields that are not important for the call at this point
 * */

data class KotlinNewsGetRes(
    var after: String? = null,
    var before: String? = null,
    val news: List<KotlinNewsItemGetRes>
)