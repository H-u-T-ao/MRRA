package com.mrra.utils

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

inline fun <reified A : Activity> Fragment.startActivity(intent: Intent.() -> Unit = {}) {
    requireContext().startActivity<A>(intent)
}