package com.me.recipe.domain.features.recipe.model

import androidx.compose.runtime.Immutable
import com.me.recipe.shared.utils.FoodCategory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CategoryRecipe(
    val category: FoodCategory,
    val recipes: ImmutableList<Recipe>,
) {
    companion object {
        fun testData() = CategoryRecipe(
            category = FoodCategory.CHICKEN,
            recipes = persistentListOf(Recipe.testData()),
        )
    }
}
