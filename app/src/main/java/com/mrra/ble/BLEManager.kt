package com.mrra.ble

import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.os.Looper

@Suppress("MissingPermission")
class BLEManager(context: Context) {

    companion object {
        private const val TAG = "BLEManager"
        var scanTime = 3000L
    }

    private val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager

    private val adapter = manager.adapter

    private val scanner = adapter.bluetoothLeScanner

    private var scanning = false

    /**
     * 获取蓝牙状态
     */
    fun getStatus(): Boolean = adapter.isEnabled

    fun setStatus(isEnabled: Boolean) {
        if (isEnabled) {
            adapter.enable()
        } else {
            adapter.disable()
        }
    }

    private val mScanLock = Any()

    /**
     * 扫描
     */
    fun startScan(callback: (Map<String, ScanResult>) -> Unit): Boolean {
        synchronized(mScanLock) {
            if (scanning) {
                return false
            }
            scanning = true
            Handler(Looper.getMainLooper()).postDelayed({
                scanner.stopScan(mCallback)
                scanning = false
                callback(resultMap)
            }, scanTime)
            scanner.startScan(mCallback)
            return true
        }
    }

    private val resultMap = mutableMapOf<String, ScanResult>()

    private val mResultLock = Any()

    private val mCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            synchronized(mResultLock) {
                resultMap[result.device.address] = result
            }
        }

    }

}