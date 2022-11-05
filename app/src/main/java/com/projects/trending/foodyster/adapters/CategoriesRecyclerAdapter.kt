package com.projects.trending.foodyster.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.databinding.CategoriesCardBinding
import com.projects.trending.foodyster.models.Category
import com.projects.trending.foodyster.ui.fragments.categories.CategoriesFragmentDirections


class CategoriesRecyclerAdapter(private val mList: List<Category>) : RecyclerView.Adapter<CategoriesRecyclerAdapter.CategoryViewHolder>() {


    class CategoryViewHolder(val binding:CategoriesCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(CategoriesCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.binding.apply {
            tvCategoryName.text =mList[position].title

            imgCategory.load(mList[position].image){
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }

        }

       holder.itemView.setOnClickListener{
           val action =
               CategoriesFragmentDirections.actionCategoriesFragment2ToCategoriesDetailsFragment(mList[position].title)
           it.findNavController().navigate(action)
       }

    }




    override fun getItemCount(): Int {
        return mList.size
    }

    interface OnItemCategoryClicked{
        fun onClickListener(category:Category)
    }


}