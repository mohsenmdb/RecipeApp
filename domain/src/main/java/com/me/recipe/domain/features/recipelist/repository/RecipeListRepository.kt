package com.me.recipe.domain.features.recipelist.repository

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.data.DataState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface RecipeListRepository {
    suspend fun search(page: Int, query: String): Flow<DataState<ImmutableList<Recipe>>>
    suspend fun slider(): Flow<DataState<ImmutableList<Recipe>>>
    suspend fun restore(page: Int, query: String): Flow<DataState<ImmutableList<Recipe>>>
    suspend fun getTopRecipe(): Recipe
}
