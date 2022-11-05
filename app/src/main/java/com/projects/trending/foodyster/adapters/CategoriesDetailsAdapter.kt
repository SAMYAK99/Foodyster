package com.projects.trending.foodyster.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.projects.trending.foodyster.bindingAdapters.DetailsRecipesRowBinding
import com.projects.trending.foodyster.databinding.DetailsRecipeRowLayoutBinding
import com.projects.trending.foodyster.databinding.FavoriteRecipesRowLayoutBinding
import com.projects.trending.foodyster.databinding.RecipesRowLayoutBinding
import com.projects.trending.foodyster.models.Result
import com.projects.trending.foodyster.models.FoodRecipe
import com.projects.trending.foodyster.utils.RecipesDiffUtil

class CategoriesDetailsAdapter : RecyclerView.Adapter<CategoriesDetailsAdapter.MyViewHolder>() {

    private var recipes = emptyList<Result>()


    class MyViewHolder(private val binding: DetailsRecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result){
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DetailsRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    // update recycler view each time we recive a new data from api
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)

    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    // Set data for DiffUtil
    fun setData(newData: FoodRecipe){
        val recipesDiffUtil  = RecipesDiffUtil(recipes,newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }
}