package com.me.recipe.cache.di

import com.me.recipe.cache.database.AppDatabase
import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.cache.recipe.mapper.RecipeEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeModule {

    @Singleton
    @Provides
    fun provideRecipeDao(db: AppDatabase): RecipeDao {
        return db.recipeDao()
    }

    @Singleton
    @Provides
    fun provideCacheRecipeMapper(): RecipeEntityMapper {
        return RecipeEntityMapper()
    }
}
