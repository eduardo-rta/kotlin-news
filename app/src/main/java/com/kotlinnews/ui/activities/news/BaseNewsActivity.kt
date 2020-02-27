package com.kotlinnews.ui.activities.news

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlinnews.R
import com.kotlinnews.mvvm.viewModels.news.IRedditNewsViewModel
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.OperationStatus
import com.kotlinnews.ui.activities.BaseAppComponentActivity
import com.kotlinnews.ui.adapters.news.NewsAdapter
import kotlinx.android.synthetic.main.activity_news.*
import timber.log.Timber
import javax.inject.Inject

abstract class BaseNewsActivity<T> : BaseAppComponentActivity() where T : ViewModel, T : IRedditNewsViewModel {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected val adapter = NewsAdapter()

    lateinit var viewModel: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        this.viewModel = this.createViewModel()

        this.loadUpdatesTextView.setOnClickListener {
            this.viewModel.refresh()
        }

        this.setupViewModel()
        this.setupAdapter()
        this.setupSwipeToRefresh()

        this.viewModel.load()
    }

    abstract fun createViewModel(): T

    abstract fun setupViewModel()

    protected open fun setupAdapter() {
        this.adapter.retryLoad = {
            this.viewModel.retry()
        }

        this.newsRecyclerView.adapter = this.adapter
    }

    protected open fun setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    open fun loadAtFrontStateChanged(state: OperationState) {
        Timber.d("loadAtFrontStateChanged - ${state.status} | ${state.affectedItems}")
        this.swipeRefreshLayout.isRefreshing = state.status == OperationStatus.LOADING
        this.loadUpdatesTextView.visibility = if (state.status == OperationStatus.SUCCESS && state.affectedItems ?: 0 > 0) View.VISIBLE else View.GONE
    }

    open fun loadStateChanged(state: OperationState) {
        Timber.d("loadStateChanged - ${state.status}")
        progressBar.visibility = if (state.status == OperationStatus.LOADING && adapter.itemCount == 0) View.VISIBLE else View.GONE
        adapter.setState(state)
    }

    open fun refreshStateChanged(state: OperationState) {
        Timber.d("refreshStateChanged - ${state.status}")
        swipeRefreshLayout.isRefreshing = state.status == OperationStatus.LOADING
    }
}