package com.projects.trending.foodyster.data

import com.projects.trending.foodyster.data.network.FoodRecipesApi
import com.projects.trending.foodyster.models.CategoryResponse
import com.projects.trending.foodyster.models.FoodJoke
import com.projects.trending.foodyster.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject


// Injecting Retrofit from Network Module

class RemoteDataSource @Inject constructor(private  val foodRecipesApi: FoodRecipesApi){

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.searchRecipes(searchQuery)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return foodRecipesApi.getFoodJoke(apiKey)
    }


}