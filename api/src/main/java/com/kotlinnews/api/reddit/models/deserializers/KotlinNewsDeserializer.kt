package com.kotlinnews.api.reddit.models.deserializers

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import com.kotlinnews.api.reddit.models.res.KotlinNewsItemGetRes
import java.lang.Exception
import java.lang.reflect.Type

/**
 * The only purpose of having a custom deserializer for our model is to convert it into a flat object
 * instead of having nested objects to imitate the original payload
 * */
class KotlinNewsDeserializer : JsonDeserializer<KotlinNewsGetRes> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): KotlinNewsGetRes {
        if (json == null || context == null) {
            throw Exception("Json and Context must be not null")
        }
        val items = mutableListOf<KotlinNewsItemGetRes>()
        val root = json.asJsonObject

        if (!root.has("data")) {
            return KotlinNewsGetRes(listOf())
        }
        val data = root.getAsJsonObject("data")

        if (!data.has("children")) {
            return KotlinNewsGetRes(listOf())
        }

        val children = data.getAsJsonArray("children")

        children.forEach children@{ childElement ->
            val childObj = childElement.asJsonObject
            if (!childObj.has("data")) {
                return@children
            }
            items.add(
                context.deserialize<KotlinNewsItemGetRes>(
                    childObj.get("data"),
                    KotlinNewsItemGetRes::class.java
                )
            )
        }

        return KotlinNewsGetRes(items.toList())
    }
}