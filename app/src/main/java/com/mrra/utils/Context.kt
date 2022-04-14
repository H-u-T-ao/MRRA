package com.mrra.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment

private var toast: Toast? = null

fun Context.showToast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}

fun Fragment.showToast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT)
    toast?.show()
}

fun Context.restartApplication() {
    packageManager.getLaunchIntentForPackage(packageName)?.let {
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(it)

        // kill now application
        android.os.Process.killProcess(android.os.Process.myPid())
    }
}

inline fun <reified A : Activity> Context.startActivity(intent: Intent.() -> Unit = {}) {
    startActivity(
        Intent(this, A::class.java).apply { intent() }
    )
}