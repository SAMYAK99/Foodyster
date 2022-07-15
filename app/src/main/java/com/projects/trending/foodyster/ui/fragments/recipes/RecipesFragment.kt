package com.projects.trending.foodyster.ui.fragments.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.trending.foodyster.viewmodels.MainViewModel
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.adapters.RecipesAdapter
import com.projects.trending.foodyster.utils.Constants.Companion.API_KEY
import com.projects.trending.foodyster.utils.NetworkResult
import com.projects.trending.foodyster.utils.observeOnce
import com.projects.trending.foodyster.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mView: View
    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_recipes, container, false)
        setRecyclerView()


        readDatabase()

        return mView
    }

    /*
    * First fetch the data each and every time from local db. if local db is
    * empty or we explicitally makes some other request then only make network call
    * */
    private fun readDatabase() {
      lifecycleScope.launch(){
          // Function called only once due to my_extension function created in util class
          mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
              if (database.isNotEmpty()) {
                  mAdapter.setData(database[0].foodRecipe)
                  hideShimmerEffect()
              } else {
                  requestApiData()
              }
          }
      }
    }

    private fun requestApiData() {
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }

                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                       "Recipes Not Found",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        }
    }


    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner) { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database.first().foodRecipe)
                }
            }
        }
    }

    private fun setRecyclerView() {
        mView.recyclerview.adapter = mAdapter
        mView.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }


    private fun showShimmerEffect() {
        mView.shimmerFrameLayout.startShimmer()
    }

    private fun hideShimmerEffect() {
        mView.shimmerFrameLayout.stopShimmer()
        mView.shimmerFrameLayout.visibility = View.GONE
        mView.recyclerview.visibility = View.VISIBLE
    }

}