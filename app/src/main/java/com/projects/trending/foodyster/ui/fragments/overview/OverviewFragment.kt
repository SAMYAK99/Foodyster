package com.projects.trending.foodyster.ui.fragments.overview

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.databinding.FragmentOverviewBinding
import com.projects.trending.foodyster.databinding.FragmentRecipesBinding
import com.projects.trending.foodyster.models.Result
import com.projects.trending.foodyster.utils.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_overview.view.*
import org.jsoup.Jsoup
import org.w3c.dom.Text
import java.util.*

class OverviewFragment : Fragment(R.layout.fragment_overview) {


//    private var _binding : FragmentOverviewBinding ? = null
//    private val binding get() = _binding!!
      private lateinit var tts : TextToSpeech
      private lateinit var recipeSummary : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Animations
        val animation = TransitionInflater.from(requireContext()).inflateTransition(
            android.R.transition.move
        )
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

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
            recipeSummary = summary
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

//        binding.playBtn.setOnClickListener{
//            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
//            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say Something")
//            startActivityForResult(intent,100)
//        }

        view.playBtn.setOnClickListener{
            tts = TextToSpeech(requireContext(),TextToSpeech.OnInitListener {
                if(it == TextToSpeech.SUCCESS){
                    tts.setLanguage(Locale.ENGLISH)
                   tts.setSpeechRate(1.0f)
                    tts.speak(recipeSummary , TextToSpeech.QUEUE_ADD, null)
                }
            })
        }

        return view

    }


}