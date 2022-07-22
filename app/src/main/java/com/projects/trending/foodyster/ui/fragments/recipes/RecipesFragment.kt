package com.projects.trending.foodyster.ui.fragments.recipes

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.trending.foodyster.viewmodels.MainViewModel
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.adapters.RecipesAdapter
import com.projects.trending.foodyster.databinding.FragmentRecipesBinding
import com.projects.trending.foodyster.utils.NetworkListener
import com.projects.trending.foodyster.utils.NetworkResult
import com.projects.trending.foodyster.utils.observeOnce
import com.projects.trending.foodyster.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.*
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi // For using Network Listener Class
@AndroidEntryPoint
class RecipesFragment : Fragment() , SearchView.OnQueryTextListener {


    // Using safe args to access arguments passed from navigation
    private val args by navArgs<RecipesFragmentArgs>()


    private lateinit var networkListener: NetworkListener

    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

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


        // Binding for recipes class fragment
        _binding = FragmentRecipesBinding.inflate(inflater,container,false)
        // because in fragment recipes model we are going to use lifecycle models and lifecycle object
        binding.lifecycleOwner = this
        // SETTING CURRENT MAIN VIEW MODEL
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
        setRecyclerView()


        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }

        // Checking the status of Network Listener class
        lifecycleScope.launch{
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect(){ status->
                    Log.d("Network Listener" , status.toString())

                    // Setting and displaying the network status in recipes view model
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()

                    // Whenever network changes we call Read Database
                    readDatabase()
                }
        }

        binding.recipesFab.setOnClickListener {
           if(recipesViewModel.networkStatus){
               // this action comes from nav graph joining
               findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
           }
            else{
                recipesViewModel.showNetworkStatus()
           }
        }

        return binding.root
    }

    /*
    * First fetch the data each and every time from local db. if local db is
    * empty or we explicitally makes some other request then only make network call
    * */
    private fun readDatabase() {
      lifecycleScope.launch(){
          // Function called only once due to my_extension function created in util class
          mainViewModel.readRecipes.observeOnce(viewLifecycleOwner) { database ->
              // everytime we get back from bottom sheet we will be request new data
              if (database.isNotEmpty() && !args.backFromBottomSheet) {
                  mAdapter.setData(database[0].foodRecipe)
                  hideShimmerEffect()
              } else {
                  requestApiData()
              }
          }
      }
    }

    // For Requesting the data from API
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
        binding.recyclerview.adapter = mAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    // Creating options Menu : Search
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu,menu)
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
       return  true
    }



    private fun showShimmerEffect() {
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.recyclerview.visibility = View.GONE
    }

    private fun hideShimmerEffect() {
        binding.shimmerFrameLayout.stopShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
    }

    // preventing memory leaks : whenever recipes binding is destroyed is set to null
    override fun onDestroy() {
        super.onDestroy()
        _binding  = null
    }



}