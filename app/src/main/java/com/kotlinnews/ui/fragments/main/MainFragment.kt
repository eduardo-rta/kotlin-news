package com.kotlinnews.ui.fragments.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.KotlinNewsApp
import com.kotlinnews.R
import com.kotlinnews.mvvm.viewModels.RedditNewsViewModel
import com.kotlinnews.repository.OperationStatus
import com.kotlinnews.ui.adapters.news.NewsAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber
import javax.inject.Inject

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: RedditNewsViewModel

    private val adapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val app = this.activity?.application
        if (app is KotlinNewsApp) {
            app.appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this, this.viewModelFactory).get(RedditNewsViewModel::class.java)

        viewModel.loadState.observe(this, Observer {
            Timber.d("loadStateObserve - " + it.status.toString())
            //            addStatusText("load Observe")
//            addStatusText(it.status.toString())
//            if (it.status == OperationStatus.ERROR) {
//                if (it.throwable != null) {
//                    addStatusText(it.throwable?.message ?: "")
//                    Timber.e(it.throwable)
//                }
//                if (it.message != null) {
//                    addStatusText(it.message!!)
//                }
//            }
        })


        viewModel.news.observe(this, Observer {
            Timber.d("newsObserve")
            adapter.submitList(it) {
                val layoutManager = (newsRecyclerView.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    newsRecyclerView.scrollToPosition(position)
                }
            }
        })


        setupAdapter()
        setupSwipeToRefresh()

        viewModel.load.postValue(null)
    }

    private fun setupAdapter() {
        newsRecyclerView.adapter = this.adapter
    }

    private fun setupSwipeToRefresh() {
        this.viewModel.refreshState.observe(this, Observer {
            Timber.d("refreshStateObserver - " + it.status.toString())
            swipeRefreshLayout.isRefreshing = it.status == OperationStatus.LOADING
        })
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
        }
    }
}
