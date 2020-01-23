package com.kotlinnews.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.kotlinnews.api.reddit.models.deserializers.KotlinNewsDeserializer
import com.kotlinnews.api.reddit.models.res.KotlinNewsGetRes
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiUnitTest {
    @Test
    fun testCustomDeserialization() {
        val json = "{" +
                "\"data\" : {" +
                "\"children\" : [" +
                "{" +
                "\"data\" : {" +
                "\"id\": \"ermx0p\"," +
                "\"title\": \"Is it bad practice to directly set the value of an object's field?\"," +
                "\"name\": \"t3_ermx0p\"," +
                "\"thumbnail\": \"\"," +
                "\"url\": \"https://www.reddit.com/r/Kotlin/comments/ermx0p/is_it_bad_practice_to_directly_set_the_value_of/\"," +
                "\"created_utc\": 1579568267.0" +
                "}" +
                "}" +
                "]" +
                ", after : \"after1\"" +
                ", before : \"beforeX\"" +
                "}" +
                "}"


        val gson = GsonBuilder()
            .registerTypeAdapter(KotlinNewsGetRes::class.java, KotlinNewsDeserializer())
            .create()
        val model = gson.fromJson<KotlinNewsGetRes>(
            json,
            KotlinNewsGetRes::class.java
        )
        assert(model.after == "after1")
        assert(model.before == "beforeX")
        assert(model.news.size == 1)
        assert(model.news[0].id == "ermx0p")
        assert(model.news[0].title == "Is it bad practice to directly set the value of an object's field?")
        assert(model.news[0].name == "t3_ermx0p")
        assert(model.news[0].thumbnail == "")
        assert(model.news[0].url == "https://www.reddit.com/r/Kotlin/comments/ermx0p/is_it_bad_practice_to_directly_set_the_value_of/")
        assert(model.news[0].createdUtc == 1579568267L)
    }
}

