package com.mrra.fragment.mrra.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mrra.entity.ControlMode
import com.mrra.fragment.mrra.control.InitiativeFragment
import com.mrra.fragment.mrra.control.MemoryFragment
import com.mrra.fragment.mrra.control.PassiveFragment
import com.mrra.fragment.mrra.control.StopFragment

class ControlModeViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    companion object {
        internal val titles = Array(4) {
            when (it) {
                0 -> ControlMode.STOP
                1 -> ControlMode.INITIATIVE
                2 -> ControlMode.PASSIVE
                else -> ControlMode.MEMORY
            }.label
        }
    }

    override fun getItemCount() = titles.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StopFragment.newInstance()
            1 -> InitiativeFragment.newInstance()
            2 -> PassiveFragment.newInstance()
            else -> MemoryFragment.newInstance()
        }
    }
}