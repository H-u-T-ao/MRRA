package com.mrra.activity.setting

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.mrra.R
import com.mrra.base.BaseActivity
import com.mrra.datastore.LocaleDataStoreManager.saveLocale
import com.mrra.utils.*
import com.mrra.utils.LOCALE_EN
import com.mrra.utils.LOCALE_ZH
import com.mrra.weight.CircleView
import kotlinx.coroutines.runBlocking

class SettingActivity : BaseActivity() {

    private lateinit var en: CircleView
    private lateinit var cn: CircleView
    private lateinit var restart: Button

    private lateinit var customaryLang: String
    private lateinit var currentLang: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        en = findViewById(R.id.cv_setting_en)
        cn = findViewById(R.id.cv_setting_cn)
        restart = findViewById(R.id.btn_setting_restart)

        en.alpha = 0.4F
        cn.alpha = 0.4F

        en.setOnClickListener {
            if (currentLang == "en") return@setOnClickListener false
            runBlocking {
                setLocale(LOCALE_EN)
                saveLocale(LOCALE_EN)
                setHighLight(false)
                false
            }
        }

        cn.setOnClickListener {
            if (currentLang == "zh") return@setOnClickListener false
            runBlocking {
                setLocale(LOCALE_ZH)
                saveLocale(LOCALE_ZH)
                setHighLight(true)
                false
            }
        }

        restart.setOnClickListener {
            restartApplication()
        }

        getLocale().let { locale ->
            if (locale.language == "zh") {
                customaryLang = "zh"
                setHighLight(true)
            } else {
                customaryLang = "en"
                setHighLight(false)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarTextColor(!isLightTheme())
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && currentLang != customaryLang) {
            AlertDialog.Builder(this)
                .setMessage(resources.getString(R.string.setting_change_language))
                .setPositiveButton(resources.getString(R.string.setting_change_language_ok)) { _, _ ->
                    restartApplication()
                }
                .setNegativeButton(resources.getString(R.string.setting_change_language_cancel)) { _, _ ->
                    setHighLight(customaryLang == "zh")
                }
                .show()
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun setHighLight(isCN: Boolean) {
        val highlight: CircleView
        val formal: CircleView
        currentLang = if (isCN) {
            highlight = cn
            formal = en
            "zh"
        } else {
            highlight = en
            formal = cn
            "en"
        }
        highlight.isEnabled = false
        formal.isEnabled = true
        ObjectAnimator.ofFloat(
            highlight,
            "alpha",
            highlight.alpha,
            1F
        ).apply {
            duration = 200L
            setAutoCancel(true)
            start()
        }
        ObjectAnimator.ofFloat(
            formal,
            "alpha",
            formal.alpha,
            0.4F
        ).apply {
            duration = 200L
            setAutoCancel(true)
            start()
        }
        highlight.recoverCircleRadius()
        formal.reductionCircleRadius()
    }

}