package com.mrra.fragment.mrra

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
import com.mrra.fragment.mrra.adapter.MRRAInnerViewPagerAdapter
import com.mrra.utils.getStatusBarHeight

class MRRAFragment : BaseFragment() {

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
        return inflater.inflate(R.layout.fragment_mrra, container, false).apply {
            statusBar = findViewById(R.id.tv_mrra_status_bar)
            tab = findViewById(R.id.tb_mrra_tab)
            vp = findViewById(R.id.vp_mrra_view_pager)

            vp.adapter = MRRAInnerViewPagerAdapter(this@MRRAFragment)
            vp.offscreenPageLimit = MRRAInnerViewPagerAdapter.titles.size
            vp.currentItem = 0

            tab.tabMode = TabLayout.MODE_FIXED
            TabLayoutMediator(tab, vp, true) { tab, position ->
                tab.setText(MRRAInnerViewPagerAdapter.titles[position])
            }.attach()
        }
    }

    override fun onResume() {
        super.onResume()
        statusBar.height = requireActivity().getStatusBarHeight()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MRRAFragment()
    }

}