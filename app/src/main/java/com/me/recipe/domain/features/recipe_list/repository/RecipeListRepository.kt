package com.me.recipe.domain.features.recipe_list.repository

import com.me.recipe.data.core.utils.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeListRepository {
    suspend fun search(page: Int, query: String): Flow<DataState<List<Recipe>>>
    suspend fun restore(page: Int, query: String): Flow<DataState<List<Recipe>>>
}