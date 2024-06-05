package com.me.recipe.ui.component.util

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
    return FoodCategory.entries
}

fun getFoodCategory(value: String): FoodCategory {
    return FoodCategory.valueOf(value.uppercase())
}
