package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.shared.data.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

/**
 * Restore a list of recipes after process death.
 */
class RestoreRecipesUsecase(
    private val recipeListRepository: RecipeListRepository,
) {
    suspend operator fun invoke(
        page: Int,
        query: String,
    ): Flow<DataState<ImmutableList<Recipe>>> {
        return recipeListRepository.restore(page, query)
    }
}
