package com.projects.trending.foodyster.data.database.entites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.projects.trending.foodyster.models.FoodRecipe
import com.projects.trending.foodyster.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(var foodRecipe: FoodRecipe) {

    // whenever we get new data we replace it with old data and didn't generate new tables
    // hence it will always fetch latest data which is present in db
    @PrimaryKey(autoGenerate = false)
    var id : Int = 0

}