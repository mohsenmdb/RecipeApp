package com.me.recipe.presentation.component

import com.me.recipe.presentation.component.FoodCategory.BEEF
import com.me.recipe.presentation.component.FoodCategory.CHICKEN
import com.me.recipe.presentation.component.FoodCategory.DESSERT
import com.me.recipe.presentation.component.FoodCategory.DONUT
import com.me.recipe.presentation.component.FoodCategory.MILK
import com.me.recipe.presentation.component.FoodCategory.PIZZA
import com.me.recipe.presentation.component.FoodCategory.SOUP
import com.me.recipe.presentation.component.FoodCategory.VEGAN
import com.me.recipe.presentation.component.FoodCategory.VEGETARIAN

enum class FoodCategory(val value: String) {
    CHICKEN("Chicken"),
    BEEF("Beef"),
    SOUP("Soup"),
    DESSERT("Dessert"),
    VEGETARIAN("Vegetarian"),
    MILK("Milk"),
    VEGAN("Vegan"),
    PIZZA("Pizza"),
    DONUT("Donut"),
}

fun getAllFoodCategories(): List<FoodCategory> {
    return listOf(CHICKEN, BEEF, SOUP, DESSERT, VEGETARIAN, MILK, VEGAN, PIZZA, DONUT)
}

fun getFoodCategory(value: String): FoodCategory {
    return FoodCategory.valueOf(value.uppercase())
}
