package com.me.recipe.domain.features.recipe.repository

import com.me.recipe.data.core.utils.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipe(
        recipeId: Int,
        token: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<Recipe>>
}