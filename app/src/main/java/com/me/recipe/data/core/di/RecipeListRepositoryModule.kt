package com.me.recipe.data.core.di

import com.me.recipe.cache.features.recipe.RecipeDao
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import com.me.recipe.network.features.recipe.RecipeApi
import com.me.recipe.data.features.recipe_list.repository.RecipeListRepositoryImpl
import com.me.recipe.domain.features.recipe_list.repository.RecipeListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeListRepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeListRepository(recipeDao: RecipeDao, recipeApi: RecipeApi, recipeMapper: RecipeMapper, entityMapper: RecipeEntityMapper) : RecipeListRepository {
        return RecipeListRepositoryImpl(recipeDao, recipeApi, entityMapper, recipeMapper)
    }
}