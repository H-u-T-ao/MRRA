package com.mrra.fragment.mrra.control.adapter

import android.bluetooth.le.ScanResult
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mrra.R
import com.mrra.activity.main.MainActivity
import com.mrra.application.appContext
import com.mrra.fragment.mrra.control.ControlViewModel
import com.mrra.utils.showToast

class ConnectListAdapter(
    private val devices: List<ScanResult>,
    private val mainActivity: MainActivity
) : RecyclerView.Adapter<ConnectListAdapter.ViewHolder>() {

    private val vm = ViewModelProvider(mainActivity).get(ControlViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_connect_list, parent, false)
        return ViewHolder(view)
    }

    @Suppress("MissingPermission")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        devices[position].let { result ->
            val text = result.device.name
            val address = result.device.address
            if (position == 0 && address == vm.macAddress.value) {
                holder.itemView.setBackgroundColor(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        appContext.getColor(R.color.gray_transparent_50)
                    } else {
                        @Suppress("DEPRECATION")
                        appContext.resources.getColor(R.color.gray_transparent_50)
                    }
                )
            }
            if (text != null) holder.name.text = text
            else holder.name.setText(R.string.unnamed)
            holder.mac.text = address
            holder.itemView.setOnClickListener {
                mainActivity.hideConnectCompose()
                appContext.showToast("${mainActivity.getString(R.string.mrra_control_try)}...\n${result.device.address}")
                vm.connectGattDevice(result.device)
            }
        }
    }

    override fun getItemCount() = devices.size

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var name: TextView = v.findViewById(R.id.tv_connect_list_name)
        internal var mac: TextView = v.findViewById(R.id.tv_connect_list_mac)
    }

}