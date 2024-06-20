package com.me.recipe.domain.features.recipe.repository

import com.me.recipe.shared.data.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    suspend fun getRecipe(
        recipeId: Int,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Recipe>>
}
