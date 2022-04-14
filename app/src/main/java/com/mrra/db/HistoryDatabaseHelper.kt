package com.mrra.db

import android.bluetooth.BluetoothDevice
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mrra.entity.OperationMode
import com.mrra.entity.DeviceInformation
import com.mrra.entity.HistoryRecordInformation

class HistoryDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    TABLE_NAME,
    null,
    1
) {

    companion object {
        private const val TABLE_NAME = "history_table"
        private const val DEVICE_NAME = "device_name"
        private const val ADDRESS = "address"
        private const val OPERATION_MODE = "operation_mode"
        private const val START_TIME = "start_time"
        private const val TIME = "time"
    }

    private val helper: SQLiteDatabase = HistoryDatabaseHelper(context).writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME($DEVICE_NAME VARCHAR, $ADDRESS VARCHAR, $OPERATION_MODE VARCHAR, $START_TIME VARCHAR, $TIME VARCHAR)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    @Suppress("MissingPermission")
    fun insert(
        device: BluetoothDevice,
        operationMode: OperationMode,
        startTime: String,
        time: String
    ) {
        ContentValues().run {
            put(DEVICE_NAME, device.name)
            put(ADDRESS, device.address)
            put(OPERATION_MODE, operationMode.msg)
            put(START_TIME, startTime)
            put(TIME, time)
            helper.insert(TABLE_NAME, null, this)
        }
    }

    fun query(): List<HistoryRecordInformation> {
        val list = mutableListOf<HistoryRecordInformation>()
        return helper.query(
            TABLE_NAME,
            null, null, null,
            null, null, null
        ).run {
            moveToFirst()
            while (moveToNext()) {
                val historyRecordInfo = HistoryRecordInformation(
                    DeviceInformation(
                        getString(0),
                        getString(1)
                    ),
                    getString(2),
                    getString(3),
                    getString(4)
                )
                list.add(historyRecordInfo)
            }
            close()
            list
        }
    }

    fun delete(
        device: DeviceInformation,
        operationMode: OperationMode,
        startTime: String,
        time: String
    ) {
        helper.execSQL(
            "DELETE FROM $TABLE_NAME WHERE $DEVICE_NAME = '${device.deviceName}' AND $ADDRESS = '${device.address}' AND $OPERATION_MODE = '${operationMode.msg}' AND $START_TIME = '$startTime' AND $TIME = '$time' "
        )
    }

}