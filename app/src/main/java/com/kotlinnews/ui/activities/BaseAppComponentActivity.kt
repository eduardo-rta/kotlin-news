package com.kotlinnews.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlinnews.KotlinNewsApp
import com.kotlinnews.di.AppComponent

abstract class BaseAppComponentActivity : AppCompatActivity() {

    abstract fun inject(appComponent: AppComponent)

    override fun onCreate(savedInstanceState: Bundle?) {
        (this.application as? KotlinNewsApp)?.let {
            this.inject(it.appComponent)
        }
        super.onCreate(savedInstanceState)
    }
}