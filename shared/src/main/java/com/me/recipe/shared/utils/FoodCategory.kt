package com.me.recipe.shared.utils

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

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

fun getAllFoodCategories(): ImmutableList<FoodCategory> {
    return FoodCategory.entries.toPersistentList()
}

fun getFoodCategory(value: String): FoodCategory {
    return FoodCategory.valueOf(value.uppercase())
}
