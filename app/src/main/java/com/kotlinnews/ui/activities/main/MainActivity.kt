package com.kotlinnews.ui.activities.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kotlinnews.R
import com.kotlinnews.ui.fragments.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }
}
