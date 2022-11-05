package com.projects.trending.foodyster.ui.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.adapters.CategoriesDetailsAdapter
import com.projects.trending.foodyster.adapters.RecipesAdapter
import com.projects.trending.foodyster.adapters.SliderAdapter
import com.projects.trending.foodyster.bindingAdapters.RecipesRowBinding
import com.projects.trending.foodyster.databinding.FragmentCategoriesDetailsBinding
import com.projects.trending.foodyster.utils.NetworkListener
import com.projects.trending.foodyster.utils.NetworkResult
import com.projects.trending.foodyster.utils.observeOnce
import com.projects.trending.foodyster.viewmodels.MainViewModel
import com.projects.trending.foodyster.viewmodels.RecipesViewModel
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi // For using Network Listener Class
class CategoriesDetailsFragment : Fragment() {


    // Using safe args to access arguments passed from navigation
    // get the arguments from the Registration fragment
    private val args : CategoriesDetailsFragmentArgs by navArgs()


    private lateinit var networkListener: NetworkListener

    private var _binding: FragmentCategoriesDetailsBinding? = null



    private val binding get() = _binding!!

    private val mAdapter by lazy { CategoriesDetailsAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    lateinit var imageUrl: ArrayList<String>

    // Fixing Memory Leaks
    override fun onResume() {
        super.onResume()
        if (mainViewModel.recyclerViewState != null) {
            binding.recyclerviewDetails.layoutManager?.onRestoreInstanceState(mainViewModel.recyclerViewState)
        }
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        recipesViewModel = ViewModelProvider(requireActivity())[RecipesViewModel::class.java]
//        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        // Binding for recipes class fragment
        _binding = FragmentCategoriesDetailsBinding.inflate(inflater, container, false)


        val title = args.data
        searchApiData(title)

        setHasOptionsMenu(true)
        setRecyclerView()


        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }


        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect() { status ->
                    Log.d("Network Listener", status.toString())

                    // Setting and displaying the network status in recipes view model
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()

                }
        }
        return binding.root
    }

    // For Searching data from the API
    private fun searchApiData(searchQuery: String) {
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
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
        binding.recyclerviewDetails.adapter = mAdapter
        binding.recyclerviewDetails.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        binding.shimmerFrameLayoutDetails.startShimmer()
        binding.shimmerFrameLayoutDetails.visibility = View.VISIBLE
        binding.recyclerviewDetails.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayoutDetails.stopShimmer()
        binding.shimmerFrameLayoutDetails.visibility = View.GONE
        binding.recyclerviewDetails.visibility = View.VISIBLE
    }

    // preventing memory leaks : whenever recipes binding is destroyed is set to null
    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.recyclerViewState =
            binding.recyclerviewDetails.layoutManager?.onSaveInstanceState()
        _binding = null
    }



}

