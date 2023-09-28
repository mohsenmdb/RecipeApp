package com.me.recipe.data.core.di

import com.me.recipe.data.features.recipe.mapper.RecipeMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RecipeMapperModule {

    @Singleton
    @Provides
    fun provideRecipeMapper() : RecipeMapper {
        return RecipeMapper()
    }
}