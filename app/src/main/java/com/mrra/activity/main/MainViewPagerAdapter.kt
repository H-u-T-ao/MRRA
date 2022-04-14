package com.mrra.activity.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mrra.fragment.healthy.HealthyFragment
import com.mrra.fragment.me.MeFragment
import com.mrra.fragment.mrra.MRRAFragment

class MainViewPagerAdapter(
    fm: FragmentManager,
    private val healthyFragment: HealthyFragment,
    private val mrraFragment: MRRAFragment,
    private val meFragment: MeFragment
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> healthyFragment
            1 -> mrraFragment
            else -> meFragment
        }
    }

}