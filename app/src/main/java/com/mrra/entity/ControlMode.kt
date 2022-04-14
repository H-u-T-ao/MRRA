package com.mrra.entity

import androidx.annotation.StringRes
import com.mrra.R

enum class ControlMode(@StringRes val label: Int) {
    NOT_CONNECTED(R.string.mrra_control_not_connected),
    TRY(R.string.mrra_control_try),
    ERROR(R.string.mrra_control_error),

    INITIATIVE(R.string.mrra_control_mode_initiative),
    PASSIVE(R.string.mrra_control_mode_passive),
    MEMORY(R.string.mrra_control_mode_memory),
    STOP(R.string.mrra_control_mode_stop)
}