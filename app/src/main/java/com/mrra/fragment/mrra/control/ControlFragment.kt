package com.mrra.fragment.mrra.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mrra.R
import com.mrra.activity.main.MainActivity
import com.mrra.base.BaseFragment
import com.mrra.entity.ConnectStatus
import com.mrra.fragment.mrra.adapter.ControlModeViewPagerAdapter
import com.mrra.fragment.mrra.control.core.DataFrameFormatter.Companion.formatPToAngle
import com.mrra.utils.showToast
import com.mrra.weight.TextViewMarqueeForever

class ControlFragment : BaseFragment() {

    private lateinit var connect: RelativeLayout
    private lateinit var clickToConnect: TextViewMarqueeForever
    private lateinit var mac: TextViewMarqueeForever
    private lateinit var name: TextViewMarqueeForever
    private lateinit var status: TextViewMarqueeForever
    private lateinit var mode: TextViewMarqueeForever
    private lateinit var angle0: TextView
    private lateinit var angle1: TextView
    private lateinit var angle2: TextView
    private lateinit var tab: TabLayout
    private lateinit var vp: ViewPager2

    private lateinit var vm: ControlViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(requireActivity()).get(ControlViewModel::class.java)
    }

    @Suppress("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_control, container, false).apply {
            connect = findViewById(R.id.rl_control_connect)
            clickToConnect = findViewById(R.id.tv_control_click_to_connect)
            mac = findViewById(R.id.tv_control_mac_address)
            name = findViewById(R.id.tv_control_device_name)
            status = findViewById(R.id.tv_control_connect_status)
            mode = findViewById(R.id.tv_control_mode)
            angle0 = findViewById(R.id.tv_control_angle_0)
            angle1 = findViewById(R.id.tv_control_angle_1)
            angle2 = findViewById(R.id.tv_control_angle_2)
            tab = findViewById(R.id.tb_control_mode_select)
            vp = findViewById(R.id.vp_control_view_pager)

            connect.setOnClickListener {
                (requireActivity() as MainActivity).showConnectCompose()
            }

            vp.adapter = ControlModeViewPagerAdapter(this@ControlFragment)
            vp.offscreenPageLimit = ControlModeViewPagerAdapter.titles.size
            vp.currentItem = 0

            tab.tabMode = TabLayout.MODE_AUTO
            TabLayoutMediator(tab, vp, true) { tab, position ->
                tab.setText(ControlModeViewPagerAdapter.titles[position])
            }.attach()

            vm.macAddress.observe(viewLifecycleOwner) {
                if (it != null) mac.text = it
                else mac.setText(R.string.mrra_control_not_connected)
            }

            vm.deviceName.observe(viewLifecycleOwner) {
                if (it != null) name.text = it
                else name.setText(R.string.mrra_control_not_connected)
            }

            vm.connectStatus.observe(viewLifecycleOwner) {
                if (it == ConnectStatus.NOT_CONNECTED || it == ConnectStatus.ERROR) {
                    showToast(requireActivity().getString(it.label))
                }
                status.setText(it.label)
            }

            vm.mode.observe(viewLifecycleOwner) {
                mode.setText(it.label)
            }

            vm.data.observe(viewLifecycleOwner) {
                if (it != null) {
                    angle0.text = "${it.p.first.formatPToAngle().toInt()}°"
                    angle1.text = "${it.p.second.formatPToAngle().toInt()}°"
                    angle2.text = "${it.p.third.formatPToAngle().toInt()}°"
                } else {
                    angle0.text = "-°"
                    angle1.text = "-°"
                    angle2.text = "-°"
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ControlFragment()
    }
}