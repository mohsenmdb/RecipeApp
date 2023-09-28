package com.me.recipe.domain.features.recipe.usecases

import com.me.recipe.data.core.utils.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Retrieve a recipe from the cache given it's unique id.
 */
class GetRecipeUsecase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(
        recipeId: Int,
        token: String,
        isNetworkAvailable: Boolean
    ): Flow<DataState<Recipe>> {
        return recipeRepository.getRecipe(recipeId, token, isNetworkAvailable)
    }
}