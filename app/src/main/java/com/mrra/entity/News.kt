package com.mrra.entity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class News(
    @DrawableRes val image: Int,
    @StringRes val title: Int,
    @StringRes val url: Int,
    @StringRes val updateDate: Int,
    @StringRes val from: Int
)
