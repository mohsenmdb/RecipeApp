package com.me.recipe.domain.features.recipe_list.usecases

import com.me.recipe.data.core.utils.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe_list.repository.RecipeListRepository
import kotlinx.coroutines.flow.Flow

/**
 * Restore a list of recipes after process death.
 */
class RestoreRecipesUsecase(
  private val recipeListRepository: RecipeListRepository
) {
  suspend operator fun invoke(
    page: Int,
    query: String
  ): Flow<DataState<List<Recipe>>> {
    return recipeListRepository.restore(page, query)
  }
}




