package com.me.recipe.cache.di

import android.content.Context
import com.me.recipe.cache.datastore.UserDataStore
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
    internal fun provideUserDataStore(@ApplicationContext context: Context): UserDataStore =
        UserDataStore(context)
}
