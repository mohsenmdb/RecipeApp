package com.me.recipe.domain.di

import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUsecase
import com.me.recipe.domain.features.recipe_list.repository.RecipeListRepository
import com.me.recipe.domain.features.recipe_list.usecases.RestoreRecipesUsecase
import com.me.recipe.domain.features.recipe_list.usecases.SearchRecipesUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RecipeUsecaseModule {

    @ViewModelScoped
    @Provides
    fun provideSearchRecipeUsecase(
        recipeListRepository: RecipeListRepository
    ): SearchRecipesUsecase {
        return SearchRecipesUsecase(recipeListRepository = recipeListRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipesUsecase(
        recipeListRepository: RecipeListRepository
    ): RestoreRecipesUsecase {
        return RestoreRecipesUsecase(recipeListRepository = recipeListRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipeUsecase(
        recipeRepository: RecipeRepository
    ): GetRecipeUsecase {
        return GetRecipeUsecase(recipeRepository = recipeRepository)
    }

}











