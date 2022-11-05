package com.projects.trending.foodyster.di

import com.projects.trending.foodyster.data.network.FoodRecipesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.projects.trending.foodyster.utils.Constants.Companion.BASE_URL
import com.projects.trending.foodyster.utils.Constants.Companion.BASE_URL_CATEGORY
import java.util.concurrent.TimeUnit

// Network Module : How To provide Retrofit builder to OUR remote data source

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    // Developing Okhttp client and gson Converter factory from above modules
    // to satisfy below Retrofit Module
    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }


    // Telling hilt library which class we want to inject later
    @Singleton // Only 1 instance for each dependency
    @Provides // using retrofit which is an external library
    fun provideApiService(retrofit: Retrofit): FoodRecipesApi {
        return retrofit.create(FoodRecipesApi::class.java)
    }






}