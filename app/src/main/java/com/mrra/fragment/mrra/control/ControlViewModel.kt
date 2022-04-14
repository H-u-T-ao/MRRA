package com.mrra.fragment.mrra.control

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrra.application.appContext
import com.mrra.entity.ConnectStatus
import com.mrra.entity.ControlMode
import com.mrra.fragment.mrra.control.core.DataFrameFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class ControlViewModel : ViewModel() {

    companion object {
        private const val TAG = "ControlViewModel"
        private const val TARGET_UUID = "0000ffe1-0000-1000-8000-00805f9bffe1"
    }

    private var errorCount = 0

    // device, don't use this
    private val bluetoothDevice = MutableLiveData<BluetoothDevice>(null)

    internal val bluetoothGatt = MutableLiveData<BluetoothGatt>(null)

    internal val data = MutableLiveData<DataFrameFormatter>(null)

    // device name
    internal val deviceName = MutableLiveData<String>(null)

    // mac address
    internal val macAddress = MutableLiveData<String>(null)

    // connect status
    internal val connectStatus = MutableLiveData(ConnectStatus.NOT_CONNECTED)

    // control mode
    internal val mode = MutableLiveData(ControlMode.NOT_CONNECTED)

    // bluetooth char
    internal val bluetoothChar = MutableLiveData<BluetoothGattCharacteristic>(null)


    private fun changeStatusToError() {
        bluetoothDevice.postValue(null)
        bluetoothGatt.postValue(null)
        data.postValue(null)
        deviceName.postValue(null)
        macAddress.postValue(null)
        connectStatus.postValue(ConnectStatus.ERROR)
        mode.postValue(ControlMode.ERROR)
        bluetoothChar.postValue(null)
    }

    private fun changeStatusToNotConnected() {
        bluetoothDevice.postValue(null)
        bluetoothGatt.postValue(null)
        data.postValue(null)
        deviceName.postValue(null)
        macAddress.postValue(null)
        connectStatus.postValue(ConnectStatus.NOT_CONNECTED)
        mode.postValue(ControlMode.NOT_CONNECTED)
        bluetoothChar.postValue(null)
    }

    private val mMutex = Mutex()

    @Suppress("MissingPermission")
    internal suspend fun sendCharacteristic(characteristic: BluetoothGattCharacteristic) {
        mMutex.withLock {
            for (i in 0..10) {
                bluetoothGatt.value?.writeCharacteristic(characteristic)
                delay(200L)
            }
        }
    }

    @Suppress("MissingPermission")
    internal fun connectGattDevice(device: BluetoothDevice) {
        bluetoothDevice.postValue(device)
        deviceName.postValue(device.name)
        macAddress.postValue(device.address)
        connectStatus.postValue(ConnectStatus.TRY)
        mode.postValue(ControlMode.TRY)
        device.connectGatt(appContext, false,
            object : BluetoothGattCallback() {
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt,
                    status: Int,
                    newState: Int
                ) {
                    if (newState == BluetoothGatt.STATE_CONNECTED) {
                        gatt.discoverServices()
                    } else {
                        changeStatusToNotConnected()
                    }
                }

                override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                    gatt.services.let { services ->
                        for (i in services.indices) {
                            services[i].characteristics.let { chars ->
                                for (j in chars.indices) {
                                    if (chars[j].uuid.toString() == TARGET_UUID) {
                                        bluetoothGatt.postValue(gatt)
                                        connectStatus.postValue(ConnectStatus.SUCCESS)
                                        mode.postValue(ControlMode.STOP)
                                        gatt.readCharacteristic(chars[j])
                                        return
                                    }
                                }
                            }
                        }
                    }
                    changeStatusToError()
                }

                override fun onCharacteristicRead(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic,
                    status: Int
                ) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        bluetoothChar.postValue(characteristic)
                        try {
                            data.postValue(DataFrameFormatter(characteristic))
                            do {
                                val b = gatt.setCharacteristicNotification(
                                    characteristic,
                                    true
                                )
                            } while (!b)
                            errorCount = 0
                        } catch (e: Exception) {
                            errorCount++
                            if (errorCount >= 10) {
                                do {
                                    val b =
                                        gatt.setCharacteristicNotification(
                                            characteristic,
                                            false
                                        )
                                } while (!b)
                                changeStatusToError()
                            }
                        }
                    }
                }

                override fun onCharacteristicWrite(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic,
                    status: Int
                ) {

                }

                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic
                ) {
                    gatt.readCharacteristic(characteristic)
                }

            }
        )
    }

}