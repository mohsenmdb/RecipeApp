package com.me.recipe.util.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.me.recipe.R
import com.me.recipe.util.NotificationBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecommendationWorker(private val context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    companion object {
        const val TAG = "Recommendation"
        const val KEY_WORKER_IMAGE_URI = "KEY_WORKER_IMAGE_URI"
    }
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {
//                val output = workDataOf(KEY_WORKER_IMAGE_URI to imageUrl)
                NotificationBuilder.showNotification(
                    title = context.getString(R.string.app_name),
                    message = "KEY_WORKER_IMAGE_URI",
                    context = context
                )
                Result.success()
            } catch (exception: Exception) {
                Result.failure()
            }
        }
    }
}