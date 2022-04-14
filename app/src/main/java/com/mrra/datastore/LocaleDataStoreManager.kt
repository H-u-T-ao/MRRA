package com.mrra.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mrra.base.BaseActivity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.*

object LocaleDataStoreManager {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("locale")

    private val lang by lazy { stringPreferencesKey("lang") }
    private val country by lazy { stringPreferencesKey("country") }

    fun BaseActivity.saveLocale(locale: Locale) {
        runBlocking {
            dataStore.edit {
                it[lang] = locale.language
                it[country] = locale.country
            }
        }
    }

    fun BaseActivity.loadLocale(): Locale {
        var locale = Locale("", "")
        runBlocking {
            dataStore.data.first {
                locale = Locale(it[lang] ?: "", it[country] ?: "")
                true
            }
        }
        return locale
    }

}