package com.mrra.fragment.mrra.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mrra.R
import com.mrra.fragment.mrra.ContactFragment
import com.mrra.fragment.mrra.control.ControlFragment
import com.mrra.fragment.mrra.IntroductionFragment

class MRRAInnerViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    companion object {
        internal val titles = Array(3) {
            when (it) {
                0 -> R.string.mrra_control
                1 -> R.string.mrra_introduction
                2 -> R.string.mrra_contact_us
                else -> R.string.empty
            }
        }
    }

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ControlFragment.newInstance()
            1 -> IntroductionFragment.newInstance()
            else -> ContactFragment.newInstance()
        }
    }
}