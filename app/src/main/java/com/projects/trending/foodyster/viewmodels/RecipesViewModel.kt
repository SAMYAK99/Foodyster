package com.projects.trending.foodyster.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.projects.trending.foodyster.data.DataStoreRepository
import com.projects.trending.foodyster.data.MealAndDietType
import com.projects.trending.foodyster.utils.Constants
import com.projects.trending.foodyster.utils.Constants.Companion.API_KEY
import com.projects.trending.foodyster.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.projects.trending.foodyster.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.projects.trending.foodyster.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.projects.trending.foodyster.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.projects.trending.foodyster.utils.Constants.Companion.QUERY_API_KEY
import com.projects.trending.foodyster.utils.Constants.Companion.QUERY_DIET
import com.projects.trending.foodyster.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.projects.trending.foodyster.utils.Constants.Companion.QUERY_NUMBER
import com.projects.trending.foodyster.utils.Constants.Companion.QUERY_SEARCH
import com.projects.trending.foodyster.utils.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


// Injecting datastore repository into this Recipes View Models
@HiltViewModel
class RecipesViewModel @Inject constructor
     (application: Application , private val dataStoreRepository: DataStoreRepository)
    : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false


    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()


    fun saveMealAndDietType(mealType : String ,mealTypeId : Int , dietType :String ,dietTypeId : Int) =
        viewModelScope.launch(Dispatchers.IO) {
               dataStoreRepository.saveMealAndDietType(mealType,mealTypeId,dietType,dietTypeId)
        }


    private fun saveBackOnline(backOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }



     fun applyQueries(): HashMap<String, String> {

         // Retrieving the data to store in data store preferences
         viewModelScope.launch {
             readMealAndDietType.collect{ value ->
                 mealType = value.selectedMealType
                 dietType = value.selectedDietType
             }
         }



        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = Constants.API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
            // persist back online
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "Yeah!! Back online.", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}