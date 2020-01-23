package com.kotlinnews

import android.app.Application
import com.kotlinnews.di.AppComponent
import com.kotlinnews.di.DaggerAppComponent
import com.kotlinnews.repository.reddit.di.RedditDbModule
import timber.log.Timber

class KotlinNewsApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .redditDbModule(RedditDbModule(this.applicationContext))
            .build()

        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}