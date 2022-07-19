package com.projects.trending.foodyster.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.projects.trending.foodyster.models.FoodRecipe
import com.projects.trending.foodyster.models.Result

// Converting Complex Food Recipe Object into String to store it in Room DB
// and converting back from string to Food Recipe with the help of GSON Library

class RecipesTypeConverter {

    var gson = Gson()

    // For Offline Caching of Recipes in main Fragment
    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }


    // For saving and Retriving favourite Recipes
    @TypeConverter
    fun resultToString(result: Result): String {
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data: String): Result {
        val listType = object : TypeToken<Result>() {}.type
        return gson.fromJson(data, listType)
    }


}