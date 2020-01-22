package com.kotlinnews.repository.reddit.datasource


import androidx.paging.PageKeyedDataSource
import com.kotlinnews.repository.reddit.entities.NewsEntity

class RedditNewsDataSource : PageKeyedDataSource<Int, NewsEntity>() {
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NewsEntity>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, NewsEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, NewsEntity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}