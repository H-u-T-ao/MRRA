package com.mrra.db

import android.bluetooth.BluetoothDevice
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mrra.entity.DeviceInformation

class TopDeviceDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    TABLE_NAME,
    null,
    1
) {

    companion object {
        private const val TABLE_NAME = "top_device_table"
        private const val DEVICE_NAME = "device_name"
        private const val ADDRESS = "address"
    }

    private val helper: SQLiteDatabase = TopDeviceDatabaseHelper(context).writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME($DEVICE_NAME VARCHAR, $ADDRESS VARCHAR)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    private val mLock = Any()

    fun insert(device: BluetoothDevice): Boolean {
        synchronized(mLock) {
            val list = query()
            // 最多插入三条数据
            if (list.size >= 3) {
                return false
            }
            // 不能插入重复设备
            list.map {
                if (it.address == device.address) {
                    return false
                }
            }
            // 正常插入
            val value = ContentValues()
            value.put(DEVICE_NAME, device.name ?: "未命名")
            value.put(ADDRESS, device.address ?: "未知")
            helper.insert(TABLE_NAME, null, value)
            return true
        }
    }

    fun query(): List<DeviceInformation> {
        synchronized(mLock) {
            val list = mutableListOf<DeviceInformation>()
            val cursor = helper.query(
                TABLE_NAME,
                null, null, null,
                null, null, null
            )
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val deviceInfo = DeviceInformation(
                    cursor.getString(0),
                    cursor.getString(1)
                )
                list.add(deviceInfo)
            }
            cursor.close()
            return list
        }
    }

    fun delete(deviceName: String, address: String) {
        synchronized(mLock) {
            helper.execSQL(
                "DELETE FROM $TABLE_NAME WHERE $DEVICE_NAME = '$deviceName' AND $ADDRESS = '$address'"
            )
        }
    }

}