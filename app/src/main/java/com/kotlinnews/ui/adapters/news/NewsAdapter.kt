package com.kotlinnews.ui.adapters.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.kotlinnews.R
import com.kotlinnews.repository.reddit.entities.NewsEntity

class NewsAdapter : PagedListAdapter<NewsEntity, NewsViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onBindViewHolder(
        holder: NewsViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }



    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem.newsId == newItem.newsId
            }

            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem.newsId == newItem.newsId
            }
        }
    }
}