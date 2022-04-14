package com.mrra.entity

import androidx.annotation.StringRes
import com.mrra.R

enum class ConnectStatus(@StringRes val label: Int) {
    NOT_CONNECTED(R.string.mrra_control_not_connected),
    TRY(R.string.mrra_control_try),
    ERROR(R.string.mrra_control_error),

    SUCCESS(R.string.mrra_connect_status_success)
}