package com.mrra.fragment.healthy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mrra.R
import com.mrra.base.BaseFragment
import com.mrra.fragment.healthy.inner.adapter.HealthyInnerViewPagerAdapter
import com.mrra.utils.getStatusBarHeight

class HealthyFragment : BaseFragment() {

    private lateinit var statusBar: TextView

    private lateinit var tab: TabLayout
    private lateinit var vp: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_healthy, container, false).apply {
            statusBar = findViewById(R.id.tv_healthy_status_bar)

            tab = findViewById(R.id.tb_healthy_tab)
            vp = findViewById(R.id.vp_healthy_view_pager)

            vp.adapter = HealthyInnerViewPagerAdapter(this@HealthyFragment)
            vp.offscreenPageLimit = HealthyInnerViewPagerAdapter.titles.size
            vp.currentItem = 0

            tab.tabMode = TabLayout.MODE_SCROLLABLE
            TabLayoutMediator(tab, vp, true) { tab, position ->
                tab.setText(HealthyInnerViewPagerAdapter.titles[position])
            }.attach()

        }
    }

    override fun onResume() {
        super.onResume()
        statusBar.height = requireActivity().getStatusBarHeight()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HealthyFragment()
    }
}