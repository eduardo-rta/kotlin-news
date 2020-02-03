package com.kotlinnews.ui.activities

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlinnews.KotlinNewsApp
import com.kotlinnews.di.AppComponent

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        this.inject((this.application as KotlinNewsApp).appComponent)
        super.onCreate(savedInstanceState)

    }

    protected open fun inject(component: AppComponent) {

    }
}