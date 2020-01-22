package com.kotlinnews.api

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kotlinnews.api.di.ApiTestComponent
import com.kotlinnews.api.di.DaggerApiTestComponent
import com.kotlinnews.api.reddit.RedditRestApi
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AndroidApiTest {
    private lateinit var component: ApiTestComponent

    @Inject
    lateinit var api: RedditRestApi

    @Before
    fun initialize() {
        component = DaggerApiTestComponent.create()
        component.inject(this)
    }

    @Test
    fun testInjection() {
        assertTrue(::api.isInitialized)
    }


    @Test
    fun getOne() {
        api.getNewsBeforeSingle("", 1)
            .test()
            .assertComplete()
            .assertValue { r -> r.code() == 200 }
            .assertValue { r -> r.body()?.news != null }
            .assertValue { r -> r.body()!!.news.size == 1 }
            .dispose()
    }

    @Test
    fun getFive() {
        api.getNewsBeforeSingle("", 5)
            .test()
            .assertComplete()
            .assertValue { r -> r.code() == 200 }
            .assertValue { r -> r.body()?.news != null }
            .assertValue { r -> r.body()!!.news.size == 5 }
            .dispose()
    }
}