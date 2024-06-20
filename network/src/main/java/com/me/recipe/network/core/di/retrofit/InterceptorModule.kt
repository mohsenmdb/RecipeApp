package com.me.recipe.network.core.di.retrofit

import com.me.recipe.network.core.interceptors.AuthenticationInterceptor
import com.me.recipe.shared.datastore.UserDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

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
