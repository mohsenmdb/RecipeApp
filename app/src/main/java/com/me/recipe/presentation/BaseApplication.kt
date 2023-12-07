package com.me.recipe.presentation

import android.app.Application
import com.me.recipe.BuildConfig
import com.me.recipe.util.worker.WorkerUtil
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Plant Timber logger in
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        WorkerUtil.startRecommendationWorker(this)
    }
}