package com.me.recipe.network.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    @Named("auth_token")
    fun provideApiToken() : String {
        return "Token 9c8b06d329136da358c2d00e76946b0111ce2c48"
    }
}