package com.me.recipe.domain.features.recipe.usecases

import com.me.recipe.data.core.utils.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Retrieve a recipe from the cache given it's unique id.
 */
class GetRecipeUsecase @Inject constructor(
    private val recipeRepository: RecipeRepository,
) {
    suspend operator fun invoke(
        recipeId: Int,
        isNetworkAvailable: Boolean,
    ): Flow<DataState<Recipe>> {
        return recipeRepository.getRecipe(recipeId, isNetworkAvailable)
    }
}
