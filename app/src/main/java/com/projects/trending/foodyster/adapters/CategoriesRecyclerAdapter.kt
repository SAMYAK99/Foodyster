package com.projects.trending.foodyster.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.databinding.CategoriesCardBinding
import com.projects.trending.foodyster.models.Category
import com.projects.trending.foodyster.utils.Constants
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class CategoriesRecyclerAdapter : RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoryViewHolder>() {
    private var categoryList:List<Category> = ArrayList()
    private lateinit var onItemClick: OnItemCategoryClicked


    fun setCategoryList(categoryList: List<Category>){
        this.categoryList = categoryList
        notifyDataSetChanged()
    }





    fun onItemClicked(onItemClick: OnItemCategoryClicked){
        this.onItemClick = onItemClick
    }

    class CategoryViewHolder(val binding:CategoriesCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoriesCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            tvCategoryName.text = categoryList[position].strCategory

            imgCategory.load(categoryList[position].strCategoryThumb){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }

        }

        holder.itemView.setOnClickListener {
            onItemClick.onClickListener(categoryList[position])
        }


    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    interface OnItemCategoryClicked{
        fun onClickListener(category:Category)
    }


}