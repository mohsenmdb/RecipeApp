package com.me.recipe.network.core.di.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val HTTP_TIMEOUT_S = 30

    @Provides
    @Singleton
    internal fun provideOkHttpClient(
        @AuthenticationInterceptorQualifier interceptor: Interceptor,
        @LoggingInterceptorQualifier interceptor2: Interceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(HTTP_TIMEOUT_S.toLong(), TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT_S.toLong(), TimeUnit.SECONDS)
            .writeTimeout(HTTP_TIMEOUT_S.toLong(), TimeUnit.SECONDS)

        builder.interceptors().add(interceptor2)
        builder.interceptors().add(interceptor)
        return builder.build()
    }

    @Singleton
    @Provides
    fun provideRecipeService(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://food2fork.ca/api/recipe/")
            .client(client)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            )
            .build()
    }
}