package com.kotlinnews.ui.activities.newsDetail

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.kotlinnews.KotlinNewsApp
import com.kotlinnews.R
import com.kotlinnews.mvvm.viewModels.RedditNewsDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_news_detail.*
import javax.inject.Inject

class NewsDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ID = "EXTRA_ID"
    }

    lateinit var viewModelReddit: RedditNewsDetailViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        val app = this.application
        if (app is KotlinNewsApp) {
            app.appComponent.inject(this)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        viewModelReddit =
            ViewModelProviders.of(this, this.viewModelFactory).get(RedditNewsDetailViewModel::class.java)


        backImageView.setOnClickListener {
            this.finish()
        }

        viewModelReddit.news.observe(this, Observer {
            titleTextView.text = it.title
            if (!it.thumbnail.isNullOrBlank()) {
                newsImageView.visibility = View.VISIBLE
                Picasso.get()
                    .load(it.thumbnail)
                    .into(newsImageView)
            } else {
                newsImageView.visibility = View.GONE
            }
            contentWebView.settings.loadWithOverviewMode = true
            contentWebView.settings.useWideViewPort = true
            contentWebView.settings.setSupportZoom(true)
            contentWebView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    loadingLayout.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    loadingLayout.visibility = View.GONE
                }
            }
            contentWebView.loadUrl(it.url)
        })

        val id = this.intent.getLongExtra(EXTRA_ID, 0)

        if (id == 0L) {
            this.finish()
            Toast.makeText(this.applicationContext, R.string.invalid_id, Toast.LENGTH_LONG).show()
            return
        }

        viewModelReddit.loadNews.postValue(id)
    }
}
