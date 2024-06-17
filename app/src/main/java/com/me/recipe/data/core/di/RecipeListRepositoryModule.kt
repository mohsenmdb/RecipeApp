package com.me.recipe.data.core.di

import com.me.recipe.cache.features.recipe.RecipeDao
import com.me.recipe.cache.features.recipe.mapper.RecipeEntityMapper
import com.me.recipe.data.features.recipe.mapper.RecipeMapper
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
    fun provideRecipeListRepository(recipeDao: RecipeDao, recipeApi: RecipeApi, recipeMapper: RecipeMapper, entityMapper: RecipeEntityMapper): com.me.recipe.domain.features.recipelist.repository.RecipeListRepository {
        return RecipeListRepositoryImpl(recipeDao, recipeApi, entityMapper, recipeMapper)
    }
}
