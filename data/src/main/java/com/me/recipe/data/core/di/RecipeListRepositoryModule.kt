package com.me.recipe.data.core.di

import com.me.recipe.cache.recipe.RecipeDao
import com.me.recipe.data.features.recipe.mapper.RecipeDtoMapper
import com.me.recipe.data.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipelist.repository.RecipeListRepositoryImpl
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.network.features.recipe.RecipeApi
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
    fun provideRecipeListRepository(recipeDao: RecipeDao, recipeApi: RecipeApi, recipeDtoMapper: RecipeDtoMapper, entityMapper: RecipeEntityMapper): RecipeListRepository {
        return RecipeListRepositoryImpl(recipeDao, recipeApi, entityMapper, recipeDtoMapper)
    }
}
