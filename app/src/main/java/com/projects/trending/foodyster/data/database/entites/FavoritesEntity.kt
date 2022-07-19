package com.projects.trending.foodyster.data.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.projects.trending.foodyster.models.Result
import com.projects.trending.foodyster.utils.Constants.Companion.FAVORITE_RECIPES_TABLE

@Entity(tableName = FAVORITE_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: Result
)