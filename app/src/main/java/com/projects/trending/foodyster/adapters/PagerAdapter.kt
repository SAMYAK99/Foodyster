package com.projects.trending.foodyster.adapters

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.room.PrimaryKey
import com.projects.trending.foodyster.ui.fragments.ingredients.IngredientsFragment

open class PagerAdapter (
    private val resultBundle: Bundle ,
    private val fragment: ArrayList<Fragment> ,
    private val title : ArrayList<String> ,
    fm : FragmentManager
)  : FragmentPagerAdapter (fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    override fun getCount(): Int {
       return fragment.size
    }

    // Setting Result arguments to Fragments postion
    override fun getItem(position: Int): Fragment {
        fragment[position].arguments = resultBundle
        return fragment[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return title[position]
    }
}