package com.projects.trending.foodyster.data.network

import com.projects.trending.foodyster.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject


// Injecting Retrofit from Network Module

class RemoteDataSource @Inject constructor(private  val foodRecipesApi: FoodRecipesApi){

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }
}