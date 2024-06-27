package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.shared.data.DataState
import com.me.recipe.shared.utils.FoodCategory
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class CategoriesRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) {
    suspend operator fun invoke(categories: ImmutableList<FoodCategory>): Flow<DataState<ImmutableList<CategoryRecipe>>> {
        return recipeListRepository.categoriesRecipes(categories)
    }
}
