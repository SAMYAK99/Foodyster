package com.projects.trending.foodyster.ui.fragments.categories

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.adapters.CategoriesRecyclerAdapter
import com.projects.trending.foodyster.databinding.FragmentCategoriesBinding
import com.projects.trending.foodyster.models.Category
import com.projects.trending.foodyster.ui.fragments.recipes.RecipesFragmentDirections
import com.projects.trending.foodyster.viewmodels.CategoryViewModel


class CategoriesFragment : Fragment(R.layout.fragment_categories) {
    private lateinit var binding:FragmentCategoriesBinding
    private lateinit var myAdapter: CategoriesRecyclerAdapter
    private lateinit var categoryMvvm:CategoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = CategoriesRecyclerAdapter()
        categoryMvvm = ViewModelProviders.of(this)[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(inflater,container,false)
        onCategoryClick()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeCategories()
//        onCategoryClick()
    }

    private fun onCategoryClick() {
        myAdapter.onItemClicked(object : CategoriesRecyclerAdapter.OnItemCategoryClicked{
            override fun onClickListener(category: Category) {
                val action =
                    CategoriesFragmentDirections.actionCategoriesFragment2ToCategoriesDetailsFragment()
                findNavController().navigate(action)
            }
        })
    }

    private fun observeCategories() {
        categoryMvvm.observeCategories().observe(viewLifecycleOwner
        ) { t -> myAdapter.setCategoryList(t!!) }
    }

    private fun prepareRecyclerView() {
        binding.categoriesRecyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
        }
    }


}