package com.projects.trending.foodyster.bindingAdapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.projects.trending.foodyster.data.database.entites.RecipesEntity
import com.projects.trending.foodyster.models.FoodRecipe
import com.projects.trending.foodyster.utils.NetworkResult

class RecipesBinding {

    companion object{

        // we will be reading 2 parameters from our fragment
        // require all : we need both variables
        @BindingAdapter("readApiResponse","readDatabase",requireAll = true)
        @JvmStatic
        fun handleReadDataErrors(
            view: View,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ){
            when (view){
                // It is error Image View Visiblity
                // Image is visible only when if Database is empty or network has error
                is ImageView ->{
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                }
                is TextView ->{
                    view.isVisible = apiResponse is NetworkResult.Error && database.isNullOrEmpty()
                    view.text = apiResponse?.message.toString()
                }
            }
        }
    }
}