package com.projects.trending.foodyster.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.projects.trending.foodyster.utils.Constants

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

}