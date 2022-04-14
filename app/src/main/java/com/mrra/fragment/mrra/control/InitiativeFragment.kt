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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InitiativeFragment : BaseFragment() {

    private var initMode = 0

    private lateinit var mode1: Button
    private lateinit var mode2: Button
    private lateinit var mode3: Button
    private lateinit var mode4: Button
    private lateinit var mode5: Button

    private lateinit var send: Button

    private lateinit var vm: ControlViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this).get(ControlViewModel::class.java)
    }

    @Suppress("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_initiative, container, false).apply {
            mode1 = findViewById(R.id.btn_init_1)
            mode2 = findViewById(R.id.btn_init_2)
            mode3 = findViewById(R.id.btn_init_3)
            mode4 = findViewById(R.id.btn_init_4)
            mode5 = findViewById(R.id.btn_init_5)

            send = findViewById(R.id.btn_init_send)

            mode1.setOnClickListener {
                showToast("${requireActivity().getString(R.string.mrra_control_init_mode_select)}\nNo.1")
                initMode = 1
            }

            mode2.setOnClickListener {
                showToast("${requireActivity().getString(R.string.mrra_control_init_mode_select)}\nNo.2")
                initMode = 2
            }

            mode3.setOnClickListener {
                showToast("${requireActivity().getString(R.string.mrra_control_init_mode_select)}\nNo.3")
                initMode = 3
            }

            mode4.setOnClickListener {
                showToast("${requireActivity().getString(R.string.mrra_control_init_mode_select)}\nNo.4")
                initMode = 4
            }

            mode5.setOnClickListener {
                showToast("${requireActivity().getString(R.string.mrra_control_init_mode_select)}\nNo.5")
                initMode = 5
            }

            send.setOnClickListener {
                val gatt = vm.bluetoothGatt.value
                val char = vm.bluetoothChar.value
                if (gatt == null || char == null) {
                    showToast(requireActivity().getString(R.string.mrra_control_not_connected))
                } else {
                    if (initMode == 0) {
                        showToast(requireActivity().getString(R.string.mrra_control_init_mode_select_first))
                    } else {
                        char.value = DataFrameFormatter.ByteArrayBuilder.setInitiative(initMode)
                        launch(Dispatchers.IO) {
                            vm.sendCharacteristic(char)
                        }
                    }
                }
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = InitiativeFragment()
    }
}