package com.me.recipe.di

import androidx.room.Room
import com.me.recipe.cache.RecipeDao
import com.me.recipe.cache.database.AppDatabase
import com.me.recipe.cache.model.RecipeEntityMapper
import com.me.recipe.presentation.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

  @Singleton
  @Provides
  fun provideDb(app: BaseApplication): AppDatabase {
    return Room
      .databaseBuilder(app, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
      .fallbackToDestructiveMigration()
      .build()
  }

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







