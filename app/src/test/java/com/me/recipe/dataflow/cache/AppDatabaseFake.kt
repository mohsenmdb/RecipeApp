package com.me.recipe.dataflow.cache

import com.me.recipe.cache.recipe.model.RecipeEntity

class AppDatabaseFake {

    // fake for recipe table in local db
    val recipes = mutableListOf<com.me.recipe.cache.recipe.model.RecipeEntity>()
}
