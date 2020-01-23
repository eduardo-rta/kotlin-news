package com.kotlinnews.ui.fragments.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.kotlinnews.KotlinNewsApp
import com.kotlinnews.R
import com.kotlinnews.api.reddit.RedditRestApi
import com.kotlinnews.mvvm.RedditNewsViewModel
import com.kotlinnews.repository.OperationStatus
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.repository.reddit.dao.NewsDao
import com.kotlinnews.ui.adapters.news.NewsAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import timber.log.Timber
import javax.inject.Inject

class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    @Inject
    internal lateinit var newsDao: NewsDao

    @Inject
    internal lateinit var db: RedditDb

    @Inject
    internal lateinit var api: RedditRestApi

    lateinit var viewModel: RedditNewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val app = this.activity?.application
        if (app is KotlinNewsApp) {
            app.appComponent.inject(this)
        }

        viewModel = ViewModelProviders.of(this).get(RedditNewsViewModel::class.java)
        viewModel.initialize(newsDao, db, api)


        viewModel.loadState.observe(this, Observer {
            addStatusText("load Observe")
            addStatusText(it.status.toString())
            if (it.status == OperationStatus.ERROR) {
                if (it.throwable != null) {
                    addStatusText(it.throwable?.message ?: "")
                    Timber.e(it.throwable)
                }
                if (it.message != null) {
                    addStatusText(it.message!!)
                }
            }
        })

        viewModel.refreshState.observe(this, Observer {
            addStatusText("refresh Observe")
            addStatusText(it.status.toString())
        })

        viewModel.news.observe(this, Observer {
            addStatusText("news Observe")
            addStatusText(it.count().toString())
        })

//        val adapter = NewsAdapter()

//        newsRecyclerView.adapter
    }

    override fun onStart() {
        super.onStart()
        viewModel.load.value = null
    }

    private fun addStatusText(textToAdd: String) {
        var text = textView.text.toString()
        text += textToAdd + "\n"
        textView.text = text
    }

}
