package com.mrra.utils

import java.text.SimpleDateFormat
import java.util.*

fun date(date: Long): String {
    return SimpleDateFormat("MM月dd日HH:mm", Locale.getDefault()).format(Date(date))
}

fun time(time: Long): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(Date(time))
}