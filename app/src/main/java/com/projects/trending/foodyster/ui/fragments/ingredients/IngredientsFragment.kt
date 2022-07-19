package com.projects.trending.foodyster.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.adapters.IngredientsAdapter
import com.projects.trending.foodyster.models.Result
import com.projects.trending.foodyster.utils.Constants
import kotlinx.android.synthetic.main.fragment_ingredients.view.*
import kotlinx.android.synthetic.main.fragment_recipes.view.*

class IngredientsFragment : Fragment() {

    private val mAdapter  : IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_ingredients, container, false)

        // Arguments of Result Bundle passed by safe args
        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        setUpRecylcerView(view)
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        return view
    }

    private fun setUpRecylcerView(view: View) {
            view.ingredientsRecyclerView.adapter = mAdapter
            view.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}