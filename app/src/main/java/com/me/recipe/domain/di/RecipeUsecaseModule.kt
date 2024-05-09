package com.me.recipe.domain.di

import com.me.recipe.domain.features.recipe.repository.RecipeRepository
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUsecase
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.domain.features.recipelist.usecases.RestoreRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.TopRecipeUsecase
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
        recipeListRepository: RecipeListRepository,
    ): SearchRecipesUsecase {
        return SearchRecipesUsecase(recipeListRepository = recipeListRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideRestoreRecipesUsecase(
        recipeListRepository: RecipeListRepository,
    ): RestoreRecipesUsecase {
        return RestoreRecipesUsecase(recipeListRepository = recipeListRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideGetRecipeUsecase(
        recipeRepository: RecipeRepository,
    ): GetRecipeUsecase {
        return GetRecipeUsecase(recipeRepository = recipeRepository)
    }

    @ViewModelScoped
    @Provides
    fun provideTopRecipeUsecase(
        recipeListRepository: RecipeListRepository,
    ): TopRecipeUsecase {
        return TopRecipeUsecase(recipeListRepository = recipeListRepository)
    }
}
