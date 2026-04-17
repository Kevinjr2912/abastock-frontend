package com.softgenix.abastock.core.di

import com.softgenix.abastock.core.data.local.TokenManager
import com.softgenix.abastock.core.data.remote.api.RefreshTokenApi
import com.softgenix.abastock.core.data.remote.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "http://172.20.10.7:3000/api/v1/"

    // Sin interceptor (solo para auth y refresh)
    @Provides
    @Singleton
    @AbastockRetrofit
    fun provideAbastockRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRefreshTokenApi(@AbastockRetrofit retrofit: Retrofit): RefreshTokenApi {
        return retrofit.create(RefreshTokenApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(
        tokenManager: TokenManager,
        refreshTokenApi: RefreshTokenApi
    ): AuthInterceptor {
        return AuthInterceptor(tokenManager, refreshTokenApi)
    }

    // Con interceptor (para el resto de la app)
    @Provides
    @Singleton
    @AppRetrofit
    fun provideAppRetrofit(authInterceptor: AuthInterceptor): Retrofit {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}