package com.kotlinnews.repository

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kotlinnews.repository.di.DaggerTestComponent
import com.kotlinnews.repository.di.TestComponent
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.repository.reddit.di.RedditDbModule
import com.kotlinnews.repository.reddit.entities.NewsEntity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import java.util.*
import javax.inject.Inject

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class RedditDbTest {

    private lateinit var component: TestComponent

    @Inject
    internal lateinit var db: RedditDb

    @Inject
    internal lateinit var newsDao: NewsDao

    @Before
    fun initialize() {
        Log.d(RedditDbTest::javaClass.name, "initialize")
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        this.component = DaggerTestComponent.builder()
            .redditDbModule(RedditDbModule(appContext))
            .build()

        this.component.inject(this)

        //Making sure we start the test with an empty database
        db.clearAllTables()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.kotlinnews.repository.test", appContext.packageName)
    }

    @Test
    fun testInjection() {
        assertTrue(::db.isInitialized)
        assertTrue(::newsDao.isInitialized)
    }

    @Test
    fun testAddNews() {
        var newsId = 1234L
        var title = "Title here"
        var name = "name"
        var thumbnail: String? = null
        var url: String = "http1"

        val addNews: () -> Unit = {
            var news = NewsEntity(
                0,
                newsId = newsId,
                title = title,
                name = name,
                thumbnail = thumbnail,
                url = url,
                createdUtc = System.currentTimeMillis()
            )
            newsDao.insert(news)
        }
        addNews()

        var newsList = newsDao.getAll()
        assertEquals(1, newsDao.getAll().size)
        var news = newsList[0]
        assertEquals(newsId, news.newsId)
        assertEquals(title, news.title)
        assertEquals(name, news.name)
        assertNull(news.thumbnail)
        assertEquals(url, news.url)

        newsId = 2233L
        title = "Second Title"
        name = "Second Name"
        thumbnail = "thumbnailUrl"
        url = "http2"
        addNews()

        newsList = newsDao.getAll()
        assertEquals(2, newsDao.getAll().size)
        news = newsList[1]
        assertEquals(newsId, news.newsId)
        assertEquals(title, news.title)
        assertEquals(name, news.name)
        assertEquals(thumbnail, news.thumbnail)
        assertEquals(url, news.url)
    }
}