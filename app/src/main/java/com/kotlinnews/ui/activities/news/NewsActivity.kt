package com.kotlinnews.ui.activities.news

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.R
import com.kotlinnews.di.AppComponent
import com.kotlinnews.mvvm.viewModels.news.RedditNewsViewModel
import com.kotlinnews.repository.OperationStatus
import kotlinx.android.synthetic.main.activity_news.*
import timber.log.Timber

class NewsActivity : BaseNewsActivity<RedditNewsViewModel>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("NewsActivity-onCreate")

        this.loadUpdatesTextView.setOnClickListener {
            this.viewModel.refresh()
        }
    }

    override fun createViewModel(): RedditNewsViewModel {
        return ViewModelProviders.of(this, this.viewModelFactory).get(RedditNewsViewModel::class.java)
    }

    override fun setupViewModel() {
        this.viewModel.loadAtFrontStateLiveData.observe(this, Observer {
            this.loadAtFrontStateChanged(it)
        })

        this.viewModel.loadStateLiveData.observe(this, Observer {
            this.loadStateChanged(it)
        })

        this.viewModel.refreshStateLiveData.observe(this, Observer {
            this.refreshStateChanged(it)
        })

        this.viewModel.newsLiveData.observe(this, Observer {
            Timber.d("newsLiveDataObserve")
            if (progressBar.visibility == View.VISIBLE && it.size > 0) {
                progressBar.visibility = View.GONE
            }

            adapter.submitList(it) {
                val layoutManager = (newsRecyclerView.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    newsRecyclerView.scrollToPosition(position)
                }
            }
        })
    }

    override fun inject(appComponent: AppComponent) {
        appComponent.inject(this)
    }
}