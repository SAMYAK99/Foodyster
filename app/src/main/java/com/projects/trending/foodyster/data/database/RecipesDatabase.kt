package com.projects.trending.foodyster.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.projects.trending.foodyster.data.database.entites.FavoritesEntity
import com.projects.trending.foodyster.data.database.entites.FoodJokeEntity
import com.projects.trending.foodyster.data.database.entites.RecipesEntity

@Database(
    entities = [RecipesEntity::class ,FavoritesEntity::class , FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}