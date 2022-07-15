package com.projects.trending.foodyster.bindingAdapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import coil.load
import com.projects.trending.foodyster.R
import java.lang.Exception

class RecipesRowBinding {

    companion object{


        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String) {
            imageView.load(imageUrl) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        }

        // Converting likes from Int to String
        @BindingAdapter("setNoOfLikes")
        @JvmStatic // so we can use this function anywhere in the project
        fun setNoOfLikes(textView: TextView , likes : Int){
             textView.text = likes.toString()
        }


        @BindingAdapter("setPrepTime")
        @JvmStatic // so we can use this function anywhere in the project
        fun setPrepTime(textView: TextView , likes : Int){
            textView.text = likes.toString()
        }

        // Changing the vegan color for both textview and Image View
        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean) {
            if (vegan) {
                when (view) {
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }


    }
}