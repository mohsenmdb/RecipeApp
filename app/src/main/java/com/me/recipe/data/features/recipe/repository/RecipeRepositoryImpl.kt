package com.me.recipe.data.features.recipe.repository

import com.me.recipe.cache.features.recipe.RecipeDao
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.core.utils.DataState
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi,
    private val entityMapper: RecipeEntityMapper,
    private val recipeMapper: RecipeMapper,
) : RecipeRepository {
    override suspend fun getRecipe(
        recipeId: Int,
        isNetworkAvailable: Boolean
    ): Flow<DataState<Recipe>> = flow {
        try {
            emit(DataState.loading())

            // just to show loading, cache is fast
            delay(1000)

            var recipe = getRecipeFromCache(recipeId = recipeId)

            if (recipe != null) {
                emit(DataState.success(recipe))
            }
            // if the recipe is null, it means it was not in the cache for some reason. So get from network.
            else {

                if (isNetworkAvailable) {
                    // get recipe from network
                    val networkRecipe = getRecipeFromNetwork(recipeId) // dto -> domain

                    // insert into cache
                    recipeDao.insertRecipe(
                        // map domain -> entity
                        entityMapper.mapFromDomainModel(networkRecipe)
                    )
                }

                // get from cache
                recipe = getRecipeFromCache(recipeId = recipeId)

                // emit and finish
                if (recipe != null) {
                    emit(DataState.success(recipe))
                } else {
                    throw Exception("Unable to get recipe from the cache.")
                }
            }

        } catch (e: Exception) {
            emit(DataState.error(e.message ?: "Unknown Error"))
        }
    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity)
        }
    }

    private suspend fun getRecipeFromNetwork(recipeId: Int): Recipe {
        return recipeMapper.mapToDomainModel(recipeApi.get(recipeId))
    }
}