package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import javax.inject.Inject

class TopRecipeUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) {
    suspend operator fun invoke(): Recipe {
        return recipeListRepository.getTopRecipe()
    }
}
