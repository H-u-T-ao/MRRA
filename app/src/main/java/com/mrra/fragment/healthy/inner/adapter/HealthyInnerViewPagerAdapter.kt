package com.mrra.fragment.healthy.inner.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mrra.R
import com.mrra.fragment.healthy.inner.HealthyInnerFragment

class HealthyInnerViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    companion object {
        internal val titles = Array(6) {
            when (it) {
                0 -> R.string.healthy_item_0
                1 -> R.string.healthy_item_1
                2 -> R.string.healthy_item_2
                3 -> R.string.healthy_item_3
                4 -> R.string.healthy_item_4
                5 -> R.string.healthy_item_5
                else -> R.string.empty
            }
        }
    }

    private val fragmentList = mutableListOf(
        HealthyInnerFragment.newInstance(),
        HealthyInnerFragment.newInstance(),
        HealthyInnerFragment.newInstance(),
        HealthyInnerFragment.newInstance(),
        HealthyInnerFragment.newInstance(),
        HealthyInnerFragment.newInstance()
    )

    override fun getItemCount() = titles.size

    override fun createFragment(position: Int) = fragmentList[position]
}