package com.kotlinnews.ui.activities.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.kotlinnews.R
import com.kotlinnews.di.AppComponent
import com.kotlinnews.repository.reddit.RedditDb
import com.kotlinnews.ui.activities.BaseActivity
import com.kotlinnews.ui.activities.news.NewsActivity
import com.kotlinnews.ui.activities.news.NewsRxActivity
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity() {
    private val compositeDisposable = CompositeDisposable()
    private var currentDialog: AlertDialog? = null

    @Inject
    lateinit var db: RedditDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        liveDataAsyncTaskButton.setOnClickListener {
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
        }

        liveDataCoroutineButton.setOnClickListener {
            this.currentDialog = AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(R.string.not_implemented_yet)
                .setPositiveButton(R.string.ok) { d, _ ->
                    d.dismiss()
                }
                .show()
        }

        rxButton.setOnClickListener {
            val intent = Intent(this, NewsRxActivity::class.java)
            startActivity(intent)
        }


        clearDatabaseButton.setOnClickListener {
            this.currentDialog = AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(R.string.clear_database_confirmation_message)
                .setPositiveButton(R.string.yes) { d, _ ->
                    this.clearDatabase()
                    d.dismiss()
                }
                .setNegativeButton(R.string.no) { d, _ ->
                    d.dismiss()
                }
                .show()
        }
    }

    override fun onStop() {
        this.currentDialog?.dismiss()
        this.currentDialog = null
        this.compositeDisposable.clear()
        super.onStop()
    }

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    private fun clearDatabase() {
        this.compositeDisposable.add(
            Single.fromCallable {
                this.db.clearAllTables()
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    this.currentDialog = AlertDialog.Builder(this)
                        .setTitle(R.string.done)
                        .setPositiveButton(R.string.ok) { d, _ ->
                            d.dismiss()
                        }
                        .show()
                }, {
                    Timber.e(it, "Error clearing database")
                })
        )
    }
}
