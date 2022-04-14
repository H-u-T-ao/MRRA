package com.mrra.application

import android.app.Application
import android.content.Context
import com.mrra.ble.BLEManager

lateinit var appContext: Context
lateinit var bleManager: BLEManager

class MRRAApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        bleManager = BLEManager(appContext)
    }

}