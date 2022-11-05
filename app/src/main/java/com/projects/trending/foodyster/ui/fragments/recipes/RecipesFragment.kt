package com.projects.trending.foodyster.ui.fragments.recipes

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
import com.projects.trending.foodyster.adapters.RecipesAdapter
import com.projects.trending.foodyster.adapters.SliderAdapter
import com.projects.trending.foodyster.databinding.FragmentRecipesBinding
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
@AndroidEntryPoint
class RecipesFragment : Fragment() {


    // Using safe args to access arguments passed from navigation
    private val args by navArgs<RecipesFragmentArgs>()


    private lateinit var networkListener: NetworkListener

    private var _binding: FragmentRecipesBinding? = null



    private val binding get() = _binding!!

    private val mAdapter by lazy { RecipesAdapter() }
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipesViewModel: RecipesViewModel

    lateinit var imageUrl: ArrayList<String>

    // Fixing Memory Leaks
    override fun onResume() {
        super.onResume()
        if (mainViewModel.recyclerViewState != null) {
            binding.recyclerview.layoutManager?.onRestoreInstanceState(mainViewModel.recyclerViewState)
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
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        // because in fragment recipes model we are going to use lifecycle models and lifecycle object


        binding.lifecycleOwner = this
        // SETTING CURRENT MAIN VIEW MODEL
        binding.mainViewModel = mainViewModel

        setHasOptionsMenu(true)
        setRecyclerView()
        setSliderLayout()

        recipesViewModel.readBackOnline.observe(viewLifecycleOwner) {
            recipesViewModel.backOnline = it
        }



        // Checking the status of Network Listener class
        // Used launchWhenStarted fixes issue of network connection state
        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect() { status ->
                    Log.d("Network Listener", status.toString())

                    // Setting and displaying the network status in recipes view model
                    recipesViewModel.networkStatus = status
                    recipesViewModel.showNetworkStatus()

                    // Whenever network changes we call Read Database
                    readDatabase()
                }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                // this action comes from nav graph joining
                findNavController().navigate(R.id.action_recipesFragment_to_recipesBottomSheet)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                if (query != null) {
                    searchApiData(query)
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
               return  true
            }
        })

        return binding.root
    }



    /*
    * First fetch the data each and every time from local db. if local db is
    * empty or we explicitally makes some other request then only make network call
    * */
    private fun readDatabase() {
        lifecycleScope.launch() {
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
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.recipes_menu,menu)
//        val search = menu.findItem(R.id.menu_search)
//        val searchView = search.actionView as? SearchView
//        searchView?.isSubmitButtonEnabled = true
//        searchView?.setOnQueryTextListener(this)
//
//    }


//    override fun onQueryTextSubmit(query: String?): Boolean {
//        if (query != null) {
//            searchApiData(query)
//        }
//        return true
//    }
//
//    override fun onQueryTextChange(newText: String?): Boolean {
//       return  true
//    }




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
        mainViewModel.recyclerViewState =
            binding.recyclerview.layoutManager?.onSaveInstanceState()
        _binding = null
    }


    private fun setSliderLayout() {

        imageUrl = ArrayList()

        binding.slider.apply {

            // on below line we are adding data to our image url array list.
            imageUrl =
                (imageUrl + "https://www.curiouscuisiniere.com/wp-content/uploads/2019/03/Mapo-Tofu-Serving-Bowl.450.jpg") as ArrayList<String>

            imageUrl =
                (imageUrl + "https://www.curiouscuisiniere.com/wp-content/uploads/2018/10/Pad-krapow-Gai-Thai-Basil-Chicken-2.450.jpg") as ArrayList<String>

            imageUrl =
                (imageUrl + "https://img.delicious.com.au/wtXjU6-1/w1200/del/2017/02/pumpkin-dhal-42336-3.jpg") as ArrayList<String>

            imageUrl =
                (imageUrl + "https://cook.fnr.sndimg.com/content/dam/images/cook/fullset/2011/1/26/0/CC_cooked-dumplings_s4x3.jpg.rend.hgtvcom.616.462.suffix/1357776171297.jpeg") as ArrayList<String>

            // on below line we are initializing our
            // slider adapter and adding our list to it.
            val sliderAdapter = SliderAdapter( imageUrl)

            // on below line we are setting auto cycle direction
            // for our slider view from left to right.
            this.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

            // on below line we are setting adapter for our slider.
            this.setSliderAdapter(sliderAdapter)

            // on below line we are setting scroll time
            // in seconds for our slider view.
            this.scrollTimeInSec = 3

            // on below line we are setting auto cycle
            // to true to auto slide our items.
            this.isAutoCycle = true

            // on below line we are calling start
            // auto cycle to start our cycle.
            this.startAutoCycle()

        }
    }
}

