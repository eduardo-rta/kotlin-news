package com.kotlinnews.ui.activities.news

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.kotlinnews.R
import com.kotlinnews.di.AppComponent
import com.kotlinnews.mvvm.viewModels.news.RedditNewsRxViewModel

class NewsRxActivity : BaseNewsActivity<RedditNewsRxViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
    }

    override fun createViewModel(): RedditNewsRxViewModel {
        return ViewModelProviders.of(this, this.viewModelFactory).get(RedditNewsRxViewModel::class.java)
    }

    override fun setupViewModel() {

    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }


}