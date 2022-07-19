package com.projects.trending.foodyster.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.models.Result
import com.projects.trending.foodyster.utils.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        // Arguments of Result Bundle passed by safe args
        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        // Loading Image with Coil
        view.main_imageView.load(myBundle?.image)
        view.title_textView.text = myBundle?.title
        view.likes_textView.text = myBundle?.aggregateLikes.toString()
        view.time_textView.text = myBundle?.readyInMinutes.toString()

        // Parsing html tags and showing only data without html tags
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.summary_textView.text = summary

        }


        if(myBundle?.vegan == true){
            view.vegan_imageView.setColorFilter(ContextCompat.getColor(requireContext() ,R.color.green))
            view.vegan_textView.setTextColor(ContextCompat.getColor(requireContext() ,R.color.green))
        }
        if(myBundle?.vegetarian == true){
            view.vegetarian_imageView.setColorFilter(ContextCompat.getColor(requireContext() ,R.color.green))
            view.vegetarian_textView.setTextColor(ContextCompat.getColor(requireContext() ,R.color.green))
        }
        if(myBundle?.glutenFree == true){
            view.gluten_free_imageView.setColorFilter(ContextCompat.getColor(requireContext() ,R.color.green))
            view.gluten_free_textView.setTextColor(ContextCompat.getColor(requireContext() ,R.color.green))
        }
        if(myBundle?.dairyFree == true){
            view.dairy_free_imageView.setColorFilter(ContextCompat.getColor(requireContext() ,R.color.green))
            view.dairy_free_textView.setTextColor(ContextCompat.getColor(requireContext() ,R.color.green))
        }
        if(myBundle?.veryHealthy == true){
            view.healthy_imageView.setColorFilter(ContextCompat.getColor(requireContext() ,R.color.green))
            view.healthy_textView.setTextColor(ContextCompat.getColor(requireContext() ,R.color.green))
        }
        if(myBundle?.cheap == true){
            view.cheap_imageView.setColorFilter(ContextCompat.getColor(requireContext() ,R.color.green))
            view.cheap_textView.setTextColor(ContextCompat.getColor(requireContext() ,R.color.green))
        }


        return view
    }

}