package com.me.recipe.ui.component.util

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory

typealias NavigateToRecipePage = (recipe: Recipe) -> Unit

typealias NavigateToRecipeListPage = (category: FoodCategory) -> Unit

typealias NavigateToHomePage = () -> Unit
