package com.me.recipe.data.core.di

import com.me.recipe.cache.features.recipe.RecipeDao
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.data.features.recipe.repository.RecipeRepositoryImpl
import com.me.recipe.network.features.recipe.RecipeService
import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.data.features.recipe_list.repository.RecipeListRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeRepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(recipeDao: RecipeDao, recipeService: RecipeService, recipeMapper: RecipeMapper, entityMapper: RecipeEntityMapper) : RecipeRepository {
        return RecipeRepositoryImpl(recipeDao, recipeService, entityMapper, recipeMapper)
    }
}