package com.mrra.activity.main

import android.Manifest
import android.animation.ObjectAnimator
import android.bluetooth.le.ScanResult
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Switch
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrra.R
import com.mrra.application.bleManager
import com.mrra.fragment.healthy.HealthyFragment
import com.mrra.fragment.me.MeFragment
import com.mrra.fragment.mrra.MRRAFragment
import com.mrra.base.BaseActivity
import com.mrra.fragment.mrra.control.ControlViewModel
import com.mrra.fragment.mrra.control.adapter.ConnectListAdapter
import com.mrra.utils.*
import com.mrra.weight.ViewPagerForbidScroll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : BaseActivity() {

    companion object {
        private const val END_RADIUS = 9F
        private var connectBackground: Bitmap? = null
    }

    private lateinit var vp: ViewPagerForbidScroll
    private lateinit var healthy: LinearLayout
    private lateinit var mrra: LinearLayout
    private lateinit var me: LinearLayout

    private lateinit var switch: Switch
    private lateinit var connectCompose: RelativeLayout
    private lateinit var floatingBg: ImageView
    private lateinit var list: RecyclerView

    private val scanResultList = mutableListOf<ScanResult>()
    private lateinit var mAdapter: ConnectListAdapter

    private lateinit var vm: ControlViewModel

    @RequiresApi(Build.VERSION_CODES.S)
    private val permissions31 = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT
    )

    private val permissions21 = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
    )

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (!it) {
                showToast("请授予定位权限以进行蓝牙扫描")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            for (i in permissions31.indices) {
                requestPermissionLauncher.launch(permissions31[i])
            }
        } else {
            for (i in permissions21.indices) {
                requestPermissionLauncher.launch(permissions21[i])
            }
        }

        initLocale()
        setContentView(R.layout.activity_main)

        vp = findViewById(R.id.vp_main_view_pager)
        healthy = findViewById(R.id.ll_main_healthy)
        mrra = findViewById(R.id.ll_main_mrra)
        me = findViewById(R.id.ll_main_me)

        switch = findViewById(R.id.sw_connect_switch)
        connectCompose = findViewById(R.id.weight_floating_connect)
        floatingBg = findViewById(R.id.iv_connect_bg)
        list = findViewById(R.id.rv_connect_list)

        vm = ViewModelProvider(this).get(ControlViewModel::class.java)

        vp.adapter = MainViewPagerAdapter(
            supportFragmentManager,
            HealthyFragment.newInstance(),
            MRRAFragment.newInstance(),
            MeFragment.newInstance()
        )
        vp.offscreenPageLimit = 3
        vp.currentItem = 0

        healthy.setOnClickListener {
            vp.setCurrentItem(0, false)
        }

        mrra.setOnClickListener {
            vp.setCurrentItem(1, false)
        }

        me.setOnClickListener {
            vp.setCurrentItem(2, false)
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked != bleManager.getStatus()) {
                bleManager.setStatus(isChecked)
            }
        }

        floatingBg.setOnClickListener {
            hideConnectCompose()
        }

        mAdapter = ConnectListAdapter(scanResultList, this)
        list.adapter = mAdapter
        list.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        clearSystemWindowsExceptNavigateBar()
        setStatusBarTextColor(!isLightTheme())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (connectCompose.visibility == View.VISIBLE) {
            val size = scanResultList.size
            scanResultList.clear()
            mAdapter.notifyItemRangeRemoved(0, size)
            hideConnectCompose()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    internal fun showConnectCompose() {
        switch.isChecked = bleManager.getStatus()
        var size = scanResultList.size
        if (size != 0) {
            scanResultList.clear()
            mAdapter.notifyItemRangeRemoved(0, size)
        }
        bleManager.startScan {
            size = scanResultList.size
            if (size != 0) {
                scanResultList.clear()
                mAdapter.notifyItemRangeRemoved(0, size)
            }
            var count = 0
            it.map { pair ->
                scanResultList.add(pair.value)
                if (pair.key == vm.macAddress.value) {
                    Collections.swap(scanResultList, 0, count)
                }
                count++
            }
            mAdapter.notifyItemRangeInserted(0, scanResultList.size)
        }
        connectBackground = getScreenShot()
        connectCompose.visibility = View.VISIBLE
        ObjectAnimator.ofFloat(
            this,
            "blurBackground",
            connectCompose.alpha,
            1F
        ).run {
            setAutoCancel(true)
            duration = 400L
            start()
        }
    }

    fun hideConnectCompose() {
        ObjectAnimator.ofFloat(
            this,
            "blurBackground",
            connectCompose.alpha,
            0F
        ).run {
            setAutoCancel(true)
            duration = 400L
            start()
        }
        launch(Dispatchers.Main) {
            delay(400L)
            connectCompose.visibility = View.GONE
        }
    }

    @Suppress("UNUSED")
    private fun setBlurBackground(alpha: Float) {
        connectCompose.alpha = alpha
        val radius = alpha * END_RADIUS + 1
        connectBackground?.let {
            floatingBg.setImageDrawable(
                BitmapDrawable(resources, getBlurBitmap(radius, it))
            )
        }
    }

}