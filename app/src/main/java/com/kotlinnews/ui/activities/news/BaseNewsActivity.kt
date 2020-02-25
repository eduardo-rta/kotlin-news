package com.kotlinnews.ui.activities.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kotlinnews.R
import com.kotlinnews.mvvm.viewModels.RedditNewsViewModel
import com.kotlinnews.ui.adapters.news.NewsAdapter
import javax.inject.Inject

abstract class BaseNewsActivity<T> : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val adapter = NewsAdapter()

    lateinit var viewModel: T


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }
}