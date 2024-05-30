package com.me.recipe.ui.component.util

import com.me.recipe.ui.component.util.FoodCategory.BEEF
import com.me.recipe.ui.component.util.FoodCategory.CHICKEN
import com.me.recipe.ui.component.util.FoodCategory.DESSERT
import com.me.recipe.ui.component.util.FoodCategory.DONUT
import com.me.recipe.ui.component.util.FoodCategory.MILK
import com.me.recipe.ui.component.util.FoodCategory.PIZZA
import com.me.recipe.ui.component.util.FoodCategory.SOUP
import com.me.recipe.ui.component.util.FoodCategory.VEGAN
import com.me.recipe.ui.component.util.FoodCategory.VEGETARIAN

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
