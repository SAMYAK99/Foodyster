package com.projects.trending.foodyster.ui.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.adapters.CategoriesRecyclerAdapter
import com.projects.trending.foodyster.databinding.FragmentCategoriesBinding
import com.projects.trending.foodyster.models.Category


class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private lateinit var binding:FragmentCategoriesBinding
    private lateinit var myAdapter: CategoriesRecyclerAdapter
//    private lateinit var categoryMvvm:CategoryViewModel
    private val mList = ArrayList<Category>()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater,container,false)
        prepareDataset()
        binding.categoriesRecyclerView.layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        myAdapter = CategoriesRecyclerAdapter(mList)
       binding.categoriesRecyclerView.adapter = myAdapter
        return binding.root
    }

    private fun prepareDataset() {
        mList.add(Category("https://www.themealdb.com/images/category/beef.png","Beef"))
        mList.add(Category("https://www.themealdb.com/images/category/dessert.png","Dessert"))
        mList.add(Category("https://www.themealdb.com/images/category/chicken.png","Chicken"))
        mList.add(Category("https://www.themealdb.com/images/category/miscellaneous.png","Miscellaneous"))
        mList.add(Category("https://www.themealdb.com/images/category/lamb.png","Lamb"))
        mList.add(Category("https://www.themealdb.com/images/category/pork.png","Pork"))
        mList.add(Category("https://www.themealdb.com/images/category/pasta.png","Pasta"))
        mList.add(Category("https://www.themealdb.com/images/category/seafood.png","Seafood"))
        mList.add(Category("https://www.themealdb.com/images/category/side.png","Side"))
        mList.add(Category("https://www.themealdb.com/images/category/starter.png","Starter"))
        mList.add(Category("https://www.themealdb.com/images/category/vegan.png","Vegan"))
        mList.add(Category("https://www.themealdb.com/images/category/vegetarian.png","Vegetarian"))
        mList.add(Category("https://www.themealdb.com/images/category/breakfast.png","Breakfast"))
        mList.add(Category("https://www.themealdb.com/images/category/goat.png","Goat"))

    }


}
