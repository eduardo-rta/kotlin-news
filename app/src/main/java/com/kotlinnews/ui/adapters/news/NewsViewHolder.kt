package com.kotlinnews.ui.adapters.news

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.R
import com.kotlinnews.repository.reddit.entities.NewsEntity
import com.kotlinnews.ui.activities.newsDetail.NewsDetailActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_news.view.*

class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
    private val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)
    lateinit var currentNews: NewsEntity

    init {
        this.itemView.isClickable = true
        this.itemView.isFocusable = true
        this.itemView.setOnClickListener { v ->
            val intent = Intent(v.context, NewsDetailActivity::class.java)
            intent.putExtra(NewsDetailActivity.EXTRA_ID, currentNews.id)
            v.context.startActivity(intent)
        }
    }

    fun bind(newsEntity: NewsEntity) {
        this.currentNews = newsEntity

        this.titleTextView.text = newsEntity.title
        if (!newsEntity.thumbnail.isNullOrBlank()) {
            this.newsImageView.visibility = View.VISIBLE
            Picasso.get()
                .load(newsEntity.thumbnail)
                .into(newsImageView)

        } else {
            this.newsImageView.visibility = View.GONE
        }
    }
}