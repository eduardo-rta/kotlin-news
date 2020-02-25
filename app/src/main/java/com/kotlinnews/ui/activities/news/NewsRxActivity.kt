package com.kotlinnews.ui.activities.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kotlinnews.KotlinNewsApp
import com.kotlinnews.R
import com.kotlinnews.mvvm.viewModels.RedditNewsRxViewModel
import com.kotlinnews.mvvm.viewModels.RedditNewsViewModel
import com.kotlinnews.ui.adapters.news.NewsAdapter
import javax.inject.Inject

class NewsRxActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: RedditNewsRxViewModel

    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val app = this.application
        if (app is KotlinNewsApp) {
            app.appComponent.inject(this)
        }

        this.viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(RedditNewsRxViewModel::class.java)

        this.setupAdapter()
        this.setupSwipeToRefresh()
    }
}