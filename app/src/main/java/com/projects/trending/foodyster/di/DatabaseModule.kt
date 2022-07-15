package com.projects.trending.foodyster.di

import android.content.Context
import androidx.room.Room
import com.projects.trending.foodyster.data.database.RecipesDatabase
import com.projects.trending.foodyster.utils.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Telling the hilt library how to provide room database builder
// and recipes dao through this database

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_NAME
    ).build()


    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase) = database.recipesDao()

}