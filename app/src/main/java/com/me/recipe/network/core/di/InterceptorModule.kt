package com.me.recipe.network.core.di

import com.me.recipe.cache.datastore.UserDataStore
import com.me.recipe.network.core.interceptors.AuthenticationInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    @Singleton
    @AuthenticationInterceptorQualifier
    internal fun provideAuthenticationInterceptor(userDataStore: UserDataStore): Interceptor =
        AuthenticationInterceptor(userDataStore)

    @Provides
    @Singleton
    @LoggingInterceptorQualifier
    internal fun provideLoggingInterceptor(): Interceptor =
        HttpLoggingInterceptor { message -> Timber.d(message) }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticationInterceptorQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingInterceptorQualifier