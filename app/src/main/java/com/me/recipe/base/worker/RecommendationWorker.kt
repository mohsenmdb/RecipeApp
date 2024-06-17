package com.me.recipe.base.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.me.recipe.base.NotificationBuilder
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.TopRecipeUsecase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@HiltWorker
class RecommendationWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val topRecipeUsecase: TopRecipeUsecase,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val recipe = topRecipeUsecase.invoke()
                Timber.d("RecommendationWorker doWork recipe[%s]", recipe)
                if (recipe == Recipe.EMPTY) {
                    return@withContext Result.failure()
                }
                NotificationBuilder.showNotification(
                    id = recipe.id,
                    title = recipe.title,
                    message = recipe.publisher,
                    banner = recipe.featuredImage,
                    context = context,
                )
//                val output = workDataOf(KEY_WORKER_IMAGE_URI to imageUrl)
                Result.success()
            } catch (exception: Exception) {
                Result.failure()
            }
        }
    }
    companion object {
        const val TAG = "Recommendation"
    }
}
