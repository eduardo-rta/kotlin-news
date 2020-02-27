package com.kotlinnews.mvvm.viewModels.news

interface IRedditNewsViewModel {
    fun load()
    fun refresh()
    fun retry()
}