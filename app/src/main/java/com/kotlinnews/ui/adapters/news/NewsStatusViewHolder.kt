package com.kotlinnews.ui.adapters.news

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.kotlinnews.R
import com.kotlinnews.repository.OperationState
import com.kotlinnews.repository.OperationStatus
import com.kotlinnews.repository.reddit.entities.NewsEntity
import com.kotlinnews.ui.activities.newsDetail.NewsDetailActivity
import com.squareup.picasso.Picasso

class NewsStatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val retryTextView: TextView = itemView.findViewById(R.id.retryTextView)
    private val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    private var retryClick: RetryClickHandler? = null

    init {
        this.itemView.isClickable = true
        this.itemView.isFocusable = true
        this.itemView.setOnClickListener { v ->
            if (this.retryTextView.visibility == View.VISIBLE) {
                retryClick?.invoke()
            }
        }
    }

    fun bind(state: OperationState) {
        when (state.status) {
            OperationStatus.LOADING -> {
                retryTextView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            else -> {
                retryTextView.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }
    }
}