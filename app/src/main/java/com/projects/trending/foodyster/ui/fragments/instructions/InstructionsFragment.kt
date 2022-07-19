package com.projects.trending.foodyster.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.models.Result
import com.projects.trending.foodyster.utils.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_instructions, container, false)

        // Arguments of Result Bundle passed by safe args
        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        view.instructionsWebView.webViewClient =  object : WebViewClient(){}
        val websiteUrl: String = myBundle!!.sourceUrl
        view.instructionsWebView.loadUrl(websiteUrl)

        return view
    }

}