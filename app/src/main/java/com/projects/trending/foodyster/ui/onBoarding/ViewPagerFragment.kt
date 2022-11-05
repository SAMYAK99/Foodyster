package com.projects.trending.foodyster.ui.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.projects.trending.foodyster.R
import com.projects.trending.foodyster.ui.onBoarding.screens.first_screen
import com.projects.trending.foodyster.ui.onBoarding.screens.second_screen
import com.projects.trending.foodyster.ui.onBoarding.screens.third_screen
import kotlinx.android.synthetic.main.fragment_view_pager.view.*


class viewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        val fragmentList = arrayListOf<Fragment>(
            first_screen(),
            second_screen(),
            third_screen()
        )

        val adapter = ViewPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        view.viewPager.adapter = adapter

        return view
    }

}