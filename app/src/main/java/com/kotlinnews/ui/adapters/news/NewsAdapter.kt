package com.kotlinnews.ui.adapters.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.R
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.OperationStatus
import com.kotlinnews.repository.reddit.entities.NewsEntity

typealias RetryClickHandler = () -> Unit

class NewsAdapter : PagedListAdapter<NewsEntity, RecyclerView.ViewHolder>(diffCallback) {

    private var operationState: OperationState = OperationState.success()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_news -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
                NewsViewHolder(view)
            }
            R.layout.item_news_status -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_news_status, parent, false)
                NewsStatusViewHolder(view)
            }
            else -> throw IllegalArgumentException("Unknown viewType ($viewType)")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val viewType = getItemViewType(position)) {
            R.layout.item_news -> {
                getItem(position)?.let {
                    (holder as? NewsViewHolder)?.bind(it)
                }
            }
            R.layout.item_news_status -> {
                (holder as? NewsStatusViewHolder)?.bind(this.operationState)
            }
            else -> throw IllegalArgumentException("Unknown viewType ($viewType)")
        }
    }

    private fun hasExtraRow(): Boolean {
        return operationState.status != OperationStatus.SUCCESS
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_news_status
        } else {
            R.layout.item_news
        }
    }

    fun setState(newState: OperationState) {
        val previousState = this.operationState
        val hadExtraRow = hasExtraRow()
        this.operationState = newState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem.newsId == newItem.newsId
            }

            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem.title == newItem.title
            }
        }
    }
}