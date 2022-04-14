package com.mrra.fragment.me

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mrra.R
import com.mrra.activity.setting.SettingActivity
import com.mrra.base.BaseFragment
import com.mrra.utils.getStatusBarHeight
import com.mrra.utils.startActivity

class MeFragment : BaseFragment() {

    private lateinit var statusBar: TextView

    private lateinit var settings: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_me, container, false).apply {
            statusBar = findViewById(R.id.tv_me_status_bar)

            settings = findViewById(R.id.tv_me_settings)

            settings.setOnClickListener {
                startActivity<SettingActivity>()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        statusBar.height = requireActivity().getStatusBarHeight()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MeFragment()
    }
}