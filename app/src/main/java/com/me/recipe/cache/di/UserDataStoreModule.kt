package com.me.recipe.cache.di

import android.content.Context
import com.me.recipe.core.datastore.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDataStoreModule {
    @Provides
    @Singleton
    internal fun provideUserDataStore(@ApplicationContext context: Context): com.me.recipe.core.datastore.UserDataStore =
        com.me.recipe.core.datastore.UserDataStore(context)
}
