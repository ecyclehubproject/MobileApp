package com.cyclehub.di

import android.content.Context
import com.cyclehub.api.ApiService
import com.cyclehub.api.ECycleHubRemoteDataSource
import com.cyclehub.db.AppDatabase
import com.cyclehub.db.ECycleHubDao
import com.cyclehub.other.Constants
import com.cyclehub.repository.CycleHubRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    var YOUR_TOKEN = ""

    fun getCache(@ApplicationContext appContext: Context): Cache {
        val httpCacheDirectory: File = File(appContext.cacheDir, "responses")
        val cacheSize = 10 * 1024 * 1024 // 10 MiB

        return Cache(httpCacheDirectory, cacheSize.toLong())
    }

    @Provides
    fun provideBaseUrl() = Constants.BASE_URL

    @Provides
    fun provideAppContext(@ApplicationContext appContext: Context) = appContext

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext appContext: Context) =
        OkHttpClient.Builder().addInterceptor { chain ->
            val request =
                chain.request().newBuilder().addHeader("Authorization", "Bearer ${YOUR_TOKEN}")
                    .build()
            chain.proceed(request)
        }.cache(getCache(appContext)).build()

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideECycleHubRemoteDataSource(apiService: ApiService) =
        ECycleHubRemoteDataSource(apiService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.eCycleHubDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: ECycleHubRemoteDataSource,
        localDataSource: ECycleHubDao
    ) =
        CycleHubRepository(remoteDataSource, localDataSource)
}