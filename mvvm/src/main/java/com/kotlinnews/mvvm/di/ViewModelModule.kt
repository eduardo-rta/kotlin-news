package com.kotlinnews.mvvm.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kotlinnews.mvvm.viewModels.RedditNewsViewModel
import com.kotlinnews.mvvm.ViewModelFactory
import com.kotlinnews.mvvm.viewModels.RedditNewsDetailRxViewModel
import com.kotlinnews.mvvm.viewModels.RedditNewsDetailViewModel
import com.kotlinnews.mvvm.viewModels.RedditNewsViewRxViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RedditNewsViewModel::class)
    abstract fun bindRedditNewsViewModel(viewModel: RedditNewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedditNewsViewRxViewModel::class)
    abstract fun bindRedditNewsRxViewModel(viewModel: RedditNewsViewRxViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedditNewsDetailViewModel::class)
    abstract fun bindNewsDetailViewModel(viewModelReddit: RedditNewsDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedditNewsDetailRxViewModel::class)
    abstract fun bindNewsDetailRxViewModel(viewModel: RedditNewsDetailRxViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}