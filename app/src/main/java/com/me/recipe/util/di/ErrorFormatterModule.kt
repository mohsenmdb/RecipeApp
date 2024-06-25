package com.me.recipe.util.di

import com.me.recipe.util.errorformater.ErrorFormatter
import com.me.recipe.util.errorformater.ErrorFormatterImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ErrorFormatterModule {
    @Binds
    fun provideErrorFormatter(impl: ErrorFormatterImpl): ErrorFormatter
}
