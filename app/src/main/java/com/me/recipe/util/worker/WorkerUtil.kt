package com.me.recipe.util.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.me.recipe.BuildConfig
import java.util.concurrent.TimeUnit
import timber.log.Timber

object WorkerUtil {
    fun startRecommendationWorker(context: Context) {
        Timber.d("startRecommendationWorker()")
        val constraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val builder = PeriodicWorkRequestBuilder<RecommendationWorker>(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 15, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            builder.setInitialDelay(10, TimeUnit.SECONDS)
        } else {
            builder.setInitialDelay(45, TimeUnit.SECONDS)
        }
        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                /* uniqueWorkName = */
                RecommendationWorker.TAG,
                /* existingPeriodicWorkPolicy = */
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                /* periodicWork = */
                builder.build(),
            )
    }
}
