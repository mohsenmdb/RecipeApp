package com.me.recipe.di

import com.google.gson.GsonBuilder
import com.me.recipe.network.RecipeService
import com.me.recipe.network.model.RecipeDtoMapper
import com.me.recipe.repository.RecipeRepository
import com.me.recipe.repository.RecipeRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(recipeService: RecipeService, mapper: RecipeDtoMapper) : RecipeRepository{
        return RecipeRepositoryImpl(recipeService, mapper)
    }
}