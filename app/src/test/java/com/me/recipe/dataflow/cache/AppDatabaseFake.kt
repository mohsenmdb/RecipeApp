package com.me.recipe.dataflow.cache

import com.me.recipe.cache.features.recipe.model.RecipeEntity

class AppDatabaseFake {

    // fake for recipe table in local db
    val recipes = mutableListOf<RecipeEntity>()
}
