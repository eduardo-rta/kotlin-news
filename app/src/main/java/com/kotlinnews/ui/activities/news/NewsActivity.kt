package com.kotlinnews.ui.activities.news

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.KotlinNewsApp
import com.kotlinnews.R
import com.kotlinnews.mvvm.viewModels.RedditNewsViewModel
import com.kotlinnews.repository.OperationStatus
import com.kotlinnews.ui.adapters.news.NewsAdapter
import kotlinx.android.synthetic.main.activity_news.*
import timber.log.Timber
import javax.inject.Inject

class NewsActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: RedditNewsViewModel

    private val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val app = this.application
        if (app is KotlinNewsApp) {
            app.appComponent.inject(this)
        }

        this.viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(RedditNewsViewModel::class.java)

        this.setupAdapter()
        this.setupSwipeToRefresh()

        this.viewModel.loadAtFrontState.observe(this, Observer {
            Timber.d("viewModel.loadAtFrontState - ${it.status} | ${it.affectedItems}")
            this.swipeRefreshLayout.isRefreshing = it.status == OperationStatus.LOADING
            this.loadUpdatesTextView.visibility = if (it.status == OperationStatus.SUCCESS && it.affectedItems ?: 0 > 0) View.VISIBLE else View.GONE
        })

        this.loadUpdatesTextView.setOnClickListener {
            this.viewModel.refresh()
        }
        this.viewModel.loadNews()
    }

    private fun setupAdapter() {
        this.adapter.retryLoad = {
            this.viewModel.retry()
        }
        
        newsRecyclerView.adapter = this.adapter

        viewModel.loadState.observe(this, Observer {
            Timber.d("loadStateObserve - ${it.status}")
            progressBar.visibility = if (it.status == OperationStatus.LOADING && adapter.itemCount == 0) View.VISIBLE else View.GONE
            adapter.setState(it)
        })

        viewModel.news.observe(this, Observer {
            Timber.d("newsObserve")
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

    private fun setupSwipeToRefresh() {
        this.viewModel.refreshState.observe(this, Observer {
            Timber.d("refreshStateObserver - ${it.status}")
            swipeRefreshLayout.isRefreshing = it.status == OperationStatus.LOADING
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}