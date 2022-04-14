package com.mrra.utils

import android.os.Build
import com.mrra.application.appContext
import com.mrra.base.BaseActivity
import com.mrra.datastore.LocaleDataStoreManager.loadLocale
import com.mrra.datastore.LocaleDataStoreManager.saveLocale
import java.util.*

internal val LOCALE_ZH = Locale("zh", "")
internal val LOCALE_EN = Locale("en", "")

private var mLocale = LOCALE_EN

fun BaseActivity.initLocale() {
    loadLocale().let {
        if (it == LOCALE_ZH || it == LOCALE_EN) {
            setLocale(it)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                appContext.resources.configuration.locales.get(0)
            } else {
                Locale.getDefault()
            }.let { locale ->
                mLocale = if (locale.language == "zh") {
                    LOCALE_ZH
                } else {
                    LOCALE_EN
                }
                saveLocale(mLocale)
            }
        }
    }
}

@Suppress("DEPRECATION")
fun BaseActivity.setLocale(target: Locale) {
    mLocale = if (target.language == "zh") {
        LOCALE_ZH
    } else {
        LOCALE_EN
    }
    resources.run {
        val config = configuration.apply { setLocale(mLocale) }
        updateConfiguration(config, displayMetrics)
    }
}

fun getLocale() = mLocale
