package com.me.recipe.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.cache.recipe.model.RecipeEntity

@Database(entities = [RecipeEntity::class ], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao

    companion object {
        val DATABASE_NAME: String = "recipe_db"
    }
}
