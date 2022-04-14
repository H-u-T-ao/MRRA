package com.mrra.fragment.mrra.control

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.mrra.R
import com.mrra.base.BaseFragment
import com.mrra.fragment.mrra.control.core.DataFrameFormatter
import com.mrra.utils.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StopFragment : BaseFragment() {

    private lateinit var send: Button

    private lateinit var vm: ControlViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(ControlViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_memory, container, false).apply {
            send = findViewById(R.id.btn_memory_send)

            send.setOnClickListener {
                val gatt = vm.bluetoothGatt.value
                val char = vm.bluetoothChar.value
                if (gatt == null || char == null) {
                    showToast(requireActivity().getString(R.string.mrra_control_not_connected))
                } else {
                    char.value = DataFrameFormatter.ByteArrayBuilder.setStop()
                    launch(Dispatchers.IO) {
                        vm.sendCharacteristic(char)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = StopFragment()
    }
}