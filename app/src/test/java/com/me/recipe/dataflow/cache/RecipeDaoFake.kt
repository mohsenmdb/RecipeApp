package com.me.recipe.dataflow.cache

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.cache.recipe.model.RecipeEntity

class RecipeDaoFake(
    private val appDatabaseFake: AppDatabaseFake,
) : com.me.recipe.cache.recipe.RecipeDao {

    override suspend fun insertRecipe(recipe: com.me.recipe.cache.recipe.model.RecipeEntity): Long {
        appDatabaseFake.recipes.add(recipe)
        return 1 // return success
    }

    override suspend fun insertRecipes(recipes: List<com.me.recipe.cache.recipe.model.RecipeEntity>): LongArray {
        appDatabaseFake.recipes.addAll(recipes)
        return longArrayOf(1) // return success
    }

    override suspend fun getRecipeById(id: Int): com.me.recipe.cache.recipe.model.RecipeEntity? {
        return appDatabaseFake.recipes.find { it.id == id }
    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        appDatabaseFake.recipes.removeIf { it.id in ids }
        return 1 // return success
    }

    override suspend fun deleteAllRecipes() {
        appDatabaseFake.recipes.clear()
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        appDatabaseFake.recipes.removeIf { it.id == primaryKey }
        return 1 // return success
    }

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int,
    ): List<com.me.recipe.cache.recipe.model.RecipeEntity> {
        return appDatabaseFake.recipes // return the entire list for simplicity
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<com.me.recipe.cache.recipe.model.RecipeEntity> {
        return appDatabaseFake.recipes // return the entire list for simplicity
    }

    override suspend fun restoreRecipes(
        query: String,
        page: Int,
        pageSize: Int,
    ): List<com.me.recipe.cache.recipe.model.RecipeEntity> {
        return appDatabaseFake.recipes // return the entire list for simplicity
    }

    override suspend fun restoreAllRecipes(page: Int, pageSize: Int): List<com.me.recipe.cache.recipe.model.RecipeEntity> {
        return appDatabaseFake.recipes // return the entire list for simplicity
    }
}
